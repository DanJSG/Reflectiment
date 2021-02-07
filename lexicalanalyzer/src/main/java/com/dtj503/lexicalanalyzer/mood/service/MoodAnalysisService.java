package com.dtj503.lexicalanalyzer.mood.service;

import com.dtj503.lexicalanalyzer.common.parsers.StringParser;
import com.dtj503.lexicalanalyzer.common.services.AnalysisService;
import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.sql.SQLTable;
import com.dtj503.lexicalanalyzer.common.types.*;
import com.dtj503.lexicalanalyzer.mood.parsers.MoodScoreParser;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredSentence;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredWord;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredWordBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for analysing the mood of a sentence from a submitted string of text.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class MoodAnalysisService extends AnalysisService {

    /**
     * Method which analysis the mood intensity in each sentence in a string of text.
     *
     * @param text the text to analyse
     * @return a list of sentences scored with their mood intensities
     */
    public static List<MoodScoredSentence> analyseMood(String text) {
        // Parse the submitted text into a set of word tokens with PoS tags
        Document<Token> doc = StringParser.parseText(text);
        // Create a new list for storing the scored sentences
        List<MoodScoredSentence> moodScoredSentences = new ArrayList<>();
        // Loop over each sentence in the document
        for(Sentence<Token> sentence : doc.getSentences()) {
            // Get the tokenized words from the sentence
            List<Token> words = sentence.getWords();
            // Fetch the scores for each word from the database
            List<MoodScoredWord> moodScoredWords = fetchWordScores(words, SQLTable.MOOD, SQLColumn.WORD, new MoodScoredWordBuilder());
            // If there are no scored words in the database, then score the sentence as 0 for all emotions and continue
            // the loop
            if(moodScoredWords == null) {
                moodScoredSentences.add(getZeroScoreSentence(sentence.getOriginalText()));
                continue;
            }
            // Build a map of words to scored words
            Map<String, List<MoodScoredWord>> scoredWordMap = buildScoredWordMap(words, moodScoredWords);
            // Build a map of emotions to sentences of scored words
            Map<String, Sentence<MoodScoredWord>> moodSentenceMap = buildMoodSentenceMap(sentence, scoredWordMap);
            // Fetch and choose the modifier word scores
            List<ScoredWord> modifierScoredWords = fetchWordScores(words, SQLTable.SENTIMENT, SQLColumn.WORD, new ScoredWordBuilder());
            modifierScoredWords = pickScoredWord(words, modifierScoredWords);
            // Convert the modifier words into a sentence
            Sentence<ScoredWord> modifierSentence = new Sentence<>(sentence.getOriginalText(), modifierScoredWords);
            // Parse the mood scores of the sentence into a map of scores, with a score per emotion
            Map<String, Float> moodMap = MoodScoreParser.parseSentenceScore(moodSentenceMap, modifierSentence);
            // Find the strongest emotion and return the emotion label and the intensity score
            Pair<String, Float> strongestEmotionProperties = pickStrongestEmotion(moodMap);
            // Create a new sentence scored with mood intensities and labelled with the strongest emotion intensity
            MoodScoredSentence scoredSentence = new MoodScoredSentence(sentence.getOriginalText(),
                                                moodSentenceMap.get(strongestEmotionProperties.getLeft()).getWords(),
                                                strongestEmotionProperties.getRight(),
                                                strongestEmotionProperties.getLeft(), moodMap);
            // Add the mood scored sentence to the list of sentences
            moodScoredSentences.add(scoredSentence);
        }
        return moodScoredSentences;
    }

    /**
     * Method for building a map of emotion tags to a sentence of scored words with emotion intensity scores
     * representing that emotion.
     *
     * @param originalSentence the tokenized version of the original sentence
     * @param scoredMoodWordMap a map of emotion tags to a list of scored words
     * @return a map containing emotion tags mapped to a sentence containing scored words
     */
    private static Map<String, Sentence<MoodScoredWord>> buildMoodSentenceMap(Sentence<Token> originalSentence,
                                                                              Map<String, List<MoodScoredWord>> scoredMoodWordMap) {
        Map<String, Sentence<MoodScoredWord>> moodSentenceMap = new HashMap<>();
        List<Token> words = originalSentence.getWords();
        // Add the words for fear to the map
        List<MoodScoredWord> fearWords = pickMoodScoredWords(words, "fear", scoredMoodWordMap);
        moodSentenceMap.put("fear", new Sentence<>(originalSentence.getOriginalText(), fearWords));
        // Add the words for anger to the map
        List<MoodScoredWord> angerWords = pickMoodScoredWords(words, "anger", scoredMoodWordMap);
        moodSentenceMap.put("anger", new Sentence<>(originalSentence.getOriginalText(), angerWords));
        // Add the words for sadness to the map
        List<MoodScoredWord> sadnessWords = pickMoodScoredWords(words, "sadness", scoredMoodWordMap);
        moodSentenceMap.put("sadness", new Sentence<>(originalSentence.getOriginalText(), sadnessWords));
        // Add the words for joy to the map
        List<MoodScoredWord> joyWords = pickMoodScoredWords(words, "joy", scoredMoodWordMap);
        moodSentenceMap.put("joy", new Sentence<>(originalSentence.getOriginalText(), joyWords));
        return moodSentenceMap;
    }

    /**
     * Method for picking the correct scored words out of a selection of scored words based on a given emotion.
     *
     * @param words the words without scores
     * @param emotion the emotion tag
     * @param scoredMoodWordMap the words scored and tagged for each emotion
     * @return a list of relevant scored words
     */
    private static List<MoodScoredWord> pickMoodScoredWords(List<Token> words, String emotion,
                                                            Map<String, List<MoodScoredWord>> scoredMoodWordMap) {
        List<MoodScoredWord> pickedWords = new ArrayList<>();
        // Loop over every word
        for(Token word : words) {
            List<MoodScoredWord> scoredWords = scoredMoodWordMap.get(word.getWord());
            int index = -1;
            // Search for the index of the word matching the emotion and store the index if it is found
            for(int i = 0; i < scoredWords.size(); i++) {
                if(scoredWords.get(i).getEmotion().contentEquals(emotion)) {
                    index = i;
                }
            }
            // If no word is found, add the word with a score of zero, otherwise add the found word
            if(index == -1) {
                pickedWords.add(new MoodScoredWord(word.getWord(), word.getPartOfSpeech(), emotion, 0));
            } else {
                pickedWords.add(scoredWords.get(index));
            }
        }
        return pickedWords;
    }

    /**
     * Method for picking the emotion with the highest intensity scoring from a map of intensity scored emotions.
     *
     * @param moodScoreMap the map of intensity scored emotions
     * @return a pair of values, containing a <code>String</code> lable and a <code>Float</code> intensity score
     */
    private static Pair<String, Float> pickStrongestEmotion(Map<String, Float> moodScoreMap) {
        float highestScore = -1f;
        String label = null;
        String[] moods = moodScoreMap.keySet().toArray(String[]::new);
        // Loop over each mood, find the score and check if it is larger than the previous scores
        for(String mood : moods) {
            float score = moodScoreMap.get(mood);
            if(score > highestScore) {
                highestScore = score;
                label = mood;
            }
        }
        // Return results as a pair
        return new ImmutablePair<>(label, highestScore);
    }

    /**
     * Method for creating a sentence with a specific original text and a mood score of zero for all emotion tags.
     *
     * @param originalText the original text for the sentence to contain
     * @return a zero scored sentence
     */
    private static MoodScoredSentence getZeroScoreSentence(String originalText) {
        Map<String, Float> zeroScoreMap = new HashMap<>();
        zeroScoreMap.put("fear", 0f);
        zeroScoreMap.put("anger", 0f);
        zeroScoreMap.put("sadness", 0f);
        zeroScoreMap.put("joy", 0f);
        return new MoodScoredSentence(originalText, null, 0f, "none", zeroScoreMap);
    }

}
