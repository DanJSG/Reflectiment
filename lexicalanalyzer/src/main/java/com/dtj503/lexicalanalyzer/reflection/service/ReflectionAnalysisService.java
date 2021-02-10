package com.dtj503.lexicalanalyzer.reflection.service;

import com.dtj503.lexicalanalyzer.common.parsers.StringParser;
import com.dtj503.lexicalanalyzer.common.services.AnalysisService;
import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.sql.SQLTable;
import com.dtj503.lexicalanalyzer.common.types.*;
import com.dtj503.lexicalanalyzer.common.utils.ListMath;
import com.dtj503.lexicalanalyzer.reflection.parsers.ReflectionScoreParser;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionCategories;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionScoredSentence;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionScoredWord;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionScoredWordBuilder;

import java.util.*;

/**
 * Class providing functionality to analyse reflection within a string of text. Parses the text and analyses the
 * different categories of reflection, and provides an overall reflection score for each sentence based on these scores.
 * The categories of reflection are:
 *
 *  <ul>
 *   <li>Reflection</li>
 *   <li>Experience</li>
 *   <li>Feeling</li>
 *   <li>Belief</li>
 *   <li>Difficulty</li>
 *   <li>Perspective</li>
 *   <li>Learning</li>
 *   <li>Intention</li>
 *  </ul>
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class ReflectionAnalysisService extends AnalysisService {

    /**
     * Method which analyses reflection in text, calculating the scores of the different categories of reflection and
     * the overall score for the sentence.
     *
     * @param text the text to analyse
     * @return a list of sentences scored for reflection
     */
    public static List<ReflectionScoredSentence> analyseReflection(String text) {
        Document<Token> doc = StringParser.parseText(text);
        List<ReflectionScoredSentence> scoredSentences = new ArrayList<>();
        for(Sentence<Token> sentence : doc.getSentences()) {
            List<Token> words = sentence.getWords();
            // Fetch the scores of the reflection based words
            List<ReflectionScoredWord> reflectionScoredWords = fetchWordScores(words, SQLTable.REFLECTION,
                    SQLColumn.WORD, new ReflectionScoredWordBuilder());
            if(reflectionScoredWords == null) {
                scoredSentences.add(getZeroScoreSentence(sentence.getOriginalText()));
                continue;
            }
            // Map these words to their relevant emotions so that each separate category can be analysed
            Map<String, List<ReflectionScoredWord>> scoredWordMap = buildScoredWordMap(words, reflectionScoredWords);
            Map<String, Sentence<ReflectionScoredWord>> reflectionSentenceMap = buildReflectionSentenceMap(sentence,
                    scoredWordMap);
            // Fetch and choose the modifier word scores
            List<ScoredWord> modifierScoredWords = fetchWordScores(words, SQLTable.SENTIMENT, SQLColumn.WORD,
                    new ScoredWordBuilder());
            modifierScoredWords = pickScoredWord(words, modifierScoredWords);
            // Convert the modifier words into a sentence
            Sentence<ScoredWord> modifierSentence = new Sentence<>(sentence.getOriginalText(), modifierScoredWords);
            Map<String, Float> reflectionMap = ReflectionScoreParser.parseSentenceScore(reflectionSentenceMap,
                    modifierSentence);
            // Calculate the average sentence reflection score as the mean of all other reflection scores
            float sentenceScore = ListMath.mean(Arrays.asList(reflectionMap.values().toArray(Float[]::new)));

            ReflectionScoredSentence scoredSentence = new ReflectionScoredSentence(sentence.getOriginalText(),
                    reflectionSentenceMap.get(ReflectionCategories.REFLECTION.toString()).getWords(), sentenceScore,
                    reflectionMap);

            scoredSentences.add(scoredSentence);
        }

        return scoredSentences;

    }

    /**
     * Method to get a reflection scored sentence with a zero score for all elements.
     *
     * @param originalText the sentence text
     * @return the new sentence
     */
    private static ReflectionScoredSentence getZeroScoreSentence(String originalText) {
        Map<String, Float> zeroScoreMap = new HashMap<>();
        for(ReflectionCategories value : ReflectionCategories.values()) {
            zeroScoreMap.put(value.toString(), 0f);
        }
        return new ReflectionScoredSentence(originalText, null, 0, zeroScoreMap);
    }

    /**
     * Method for building a map of sentences to their reflection category.
     *
     * @param sentence the sentence to analyse
     * @param scoredWordMap the map of sentences to their reflection categories
     * @return
     */
    private static Map<String, Sentence<ReflectionScoredWord>> buildReflectionSentenceMap(
            Sentence<Token> sentence, Map<String, List<ReflectionScoredWord>> scoredWordMap) {
        Map<String, Sentence<ReflectionScoredWord>> reflectionMap = new HashMap<>();
        List<Token> words = sentence.getWords();
        for(ReflectionCategories value : ReflectionCategories.class.getEnumConstants()) {
            List<ReflectionScoredWord> currentScoredWords = pickReflectionScoredWords(words, value.toString(),
                    scoredWordMap);
            reflectionMap.put(value.toString(), new Sentence<>(sentence.getOriginalText(), currentScoredWords));
        }
        return reflectionMap;
    }

    /**
     * Method to pick the correct reflection scored word based on an input list of possible scored words.
     *
     * @param words the words to match
     * @param category the category of words
     * @param reflectionScoredWordMap the map of words to pick the correctly scored word from the list
     * @return a list of chosen words
     */
    private static List<ReflectionScoredWord> pickReflectionScoredWords(List<Token> words, String category,
            Map<String, List<ReflectionScoredWord>> reflectionScoredWordMap) {
        List<ReflectionScoredWord> pickedWords = new ArrayList<>();
        for(Token word : words) {
            List<ReflectionScoredWord> scoredWords = reflectionScoredWordMap.get(word.getWord());
            int index = -1;
            for(int i = 0; i < scoredWords.size(); i++) {
                if(scoredWords.get(i).getCategory().contentEquals(category)) {
                    index = i;
                    break;
                }
            }
            if(index == -1) {
                pickedWords.add(new ReflectionScoredWord(word.getWord(), word.getPartOfSpeech(),  0, category));
            } else {
                pickedWords.add(scoredWords.get(index));
            }
        }
        return pickedWords;
    }



}
