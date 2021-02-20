package com.dtj503.lexicalanalyzer.reflection.service;

import com.dtj503.lexicalanalyzer.common.services.AnalysisService;
import com.dtj503.lexicalanalyzer.common.sql.SQLTable;
import com.dtj503.lexicalanalyzer.common.types.*;
import com.dtj503.lexicalanalyzer.common.utils.ListMath;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredSentence;
import com.dtj503.lexicalanalyzer.reflection.parsers.ReflectionScoreParser;
import com.dtj503.lexicalanalyzer.reflection.types.*;
import com.dtj503.lexicalanalyzer.sentiment.types.SentimentScoredSentence;

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
     * @param doc the document to analyse
     * @return a list of sentences scored for reflection
     */
    public static List<ReflectionScoredSentence> analyseReflection(Document<Token> doc) {
        List<ReflectionScoredSentence> scoredSentences = new ArrayList<>();
        for(Sentence<Token> sentence : doc.getSentences()) {
            List<Token> words = sentence.getWords();
            // Fetch the scores of the reflection based words
            List<ReflectionScoredWord> reflectionScoredWords = fetchWordScores(words, SQLTable.REFLECTION, "ullman_ext", new ReflectionScoredWordBuilder());
            if(reflectionScoredWords == null) {
                scoredSentences.add(getZeroScoreSentence(sentence.getOriginalText()));
                continue;
            }
            // Map these words to their relevant emotions so that each separate category can be analysed
            Map<String, List<ReflectionScoredWord>> scoredWordMap = buildScoredWordMap(words, reflectionScoredWords);
            Map<String, Sentence<ReflectionScoredWord>> reflectionSentenceMap = buildReflectionSentenceMap(sentence,
                    scoredWordMap);
            // Fetch and choose the modifier word scores
            List<ScoredWord> modifierScoredWords = fetchWordScores(words, SQLTable.SENTIMENT, "sentiwords", new ScoredWordBuilder());
            modifierScoredWords = pickScoredWord(words, modifierScoredWords);
            // Convert the modifier words into a sentence
            Sentence<ScoredWord> modifierSentence = new Sentence<>(sentence.getOriginalText(), modifierScoredWords);
            Map<String, Float> reflectionMap = ReflectionScoreParser.parseSentenceScore(reflectionSentenceMap,
                    modifierSentence);
            // Calculate the average sentence reflection score as the mean of all other reflection scores
            float sentenceScore = ListMath.mean(Arrays.asList(reflectionMap.values().toArray(Float[]::new)));

            ReflectionScoredSentence scoredSentence = new ReflectionScoredSentence(sentence.getOriginalText(),
                    reflectionSentenceMap.get(ReflectionCategory.REFLECTION.toString()).getWords(), sentenceScore,
                    reflectionMap);

            scoredSentences.add(scoredSentence);
        }

        return scoredSentences;

    }

    /**
     * Method for calculating the reflection appraisal modifiers based on the mood and sentiment scores calculated for
     * the sentences.
     *
     * @param sentimentSentences the sentences with sentiment scores
     * @param moodSentences the sentences with mood scores
     * @return a list of reflection modifiers for each sentence
     */
    public static List<ReflectionModifier> getReflectionModifiers(List<SentimentScoredSentence> sentimentSentences,
                                                                  List<MoodScoredSentence> moodSentences,
                                                                  List<ReflectionScoredSentence> reflectionSentences) {
        final float e = 2.71828f;
        List<ReflectionModifier> modifiers = new ArrayList<>();
        for(int i = 0; i < sentimentSentences.size(); i++) {
            float sentimentScore = sentimentSentences.get(i).getScore();
            float moodScore = moodSentences.get(i).getScore();
            float reflectionScore = reflectionSentences.get(i).getScore();

            // Calculate a modifier weighting such that higher reflection scores weight the modifiers less than low
            // reflection scores. If you plot this equation, it is a negative decay.
            float modifierWeighting = (float) Math.sqrt(0.4 * (Math.pow(e, -1.5 * reflectionScore) + 0.7769) + 0.6);

            // Sentiment and mood are both weighted more heavily towards positive sentiment here
            float sentimentMultiplier = sentimentScore > 0 ? 1f : 0.315f;
            float moodMultiplier = sentimentScore > 0 ? 1f : 0.5f;

            // Sentiment and mood modifiers are calculated based on sentiment and mood scores and their multipliers, and
            // by the modifier weighting
            float sentimentModifier = (1f + ((0.704f * Math.abs(sentimentScore)) * sentimentMultiplier)) * modifierWeighting;
            float moodModifier = (1f + ((0.129f * Math.abs(moodScore)) * moodMultiplier)) * modifierWeighting;
            modifiers.add(new ReflectionModifier(sentimentModifier, moodModifier));
        }
        return modifiers;
    }

    /**
     * Method to get a reflection scored sentence with a zero score for all elements.
     *
     * @param originalText the sentence text
     * @return the new sentence
     */
    private static ReflectionScoredSentence getZeroScoreSentence(String originalText) {
        Map<String, Float> zeroScoreMap = new HashMap<>();
        for(ReflectionCategory value : ReflectionCategory.values()) {
            zeroScoreMap.put(value.toString(), 0f);
        }
        return new ReflectionScoredSentence(originalText, null, 0, zeroScoreMap);
    }

    /**
     * Method for building a map of sentences to their reflection category.
     *
     * @param sentence the sentence to analyse
     * @param scoredWordMap the map of sentences to their reflection categories
     * @return build a map of scored sentences, using the different reflection categories as the keys
     */
    private static Map<String, Sentence<ReflectionScoredWord>> buildReflectionSentenceMap(
            Sentence<Token> sentence, Map<String, List<ReflectionScoredWord>> scoredWordMap) {
        Map<String, Sentence<ReflectionScoredWord>> reflectionMap = new HashMap<>();
        List<Token> words = sentence.getWords();
        for(ReflectionCategory value : ReflectionCategory.class.getEnumConstants()) {
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
