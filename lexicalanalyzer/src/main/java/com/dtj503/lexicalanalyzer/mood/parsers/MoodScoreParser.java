package com.dtj503.lexicalanalyzer.mood.parsers;

import com.dtj503.lexicalanalyzer.common.parsers.ScoreParser;
import com.dtj503.lexicalanalyzer.common.types.ScoredWord;
import com.dtj503.lexicalanalyzer.common.types.Sentence;
import com.dtj503.lexicalanalyzer.common.utils.ListMath;
import com.dtj503.lexicalanalyzer.mood.types.Emotions;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredWord;

import java.util.*;

/**
 * Class for parsing the mood scores of a sentence using a lexicon-based approach.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class MoodScoreParser extends ScoreParser {

    /**
     * Method for parsing the mood scores of all the words in a sentence for the emotions fear, anger, sadness and joy.
     * The overall score for each emotion is then calculated, and a map of scores returned.
     *
     * @param moodSentenceMap a map which maps emotion names to lists of scored words
     * @param modifierSentence a list of words which contain possible modifiers
     * @return a <code>Map</code> which maps emotion names to a single score for the sentence
     */
    public static Map<String, Float> parseSentenceScore(Map<String, Sentence<MoodScoredWord>> moodSentenceMap,
                                          Sentence<ScoredWord> modifierSentence) {
        // Get the scores for each emotion
        List<Float> fearScores = moodSentenceMap.get(Emotions.FEAR).getScores();
        List<Float> angerScores = moodSentenceMap.get(Emotions.ANGER).getScores();
        List<Float> sadnessScores = moodSentenceMap.get(Emotions.SADNESS).getScores();
        List<Float> joyScores = moodSentenceMap.get(Emotions.JOY).getScores();
        // Get the adjective, verb and adverb indices from the modifier sentence
        List<Integer> adjectivePositions = modifierSentence.getAdjectivePositions();
        List<Integer> verbPositions = modifierSentence.getVerbPositions();
        List<Integer> adverbPositions = modifierSentence.getAdverbPositions();
        // Get the word scores from the modifier sentence
        List<Float> modifierScores = modifierSentence.getScores();
        // Create the modification vector based on the modifying adverbs in the modifier sentence
        List<Float> modificationVector = createModificationVector(adjectivePositions, verbPositions, adverbPositions,
                                                                  modifierScores);
        // Calculate the updated emotion scores based on the hadamard product of the individual scores and the mood
        // scores
        List<Float> modifiedFearScores = ListMath.hadamardProduct(modificationVector, fearScores);
        List<Float> modifiedAngerScores = ListMath.hadamardProduct(modificationVector, angerScores);
        List<Float> modifiedSadnessScores = ListMath.hadamardProduct(modificationVector, sadnessScores);
        List<Float> modifiedJoyScores = ListMath.hadamardProduct(modificationVector, joyScores);
        // Remove zero scores from each of the mood scores, unless they are all zero in which case retain
        modifiedFearScores = stripZeroScores(modifiedFearScores);
        modifiedAngerScores = stripZeroScores(modifiedAngerScores);
        modifiedSadnessScores = stripZeroScores(modifiedSadnessScores);
        modifiedJoyScores = stripZeroScores(modifiedJoyScores);
        // Calculate the overall sentence scores for each mood and limit between -1 and 1
        float sentenceFearScore = Math.max(0, Math.min(ListMath.mean(modifiedFearScores), 1));
        float sentenceAngerScore = Math.max(0, Math.min(ListMath.mean(modifiedAngerScores), 1));
        float sentenceSadnessScore = Math.max(0, Math.min(ListMath.mean(modifiedSadnessScores), 1));
        float sentenceJoyScore = Math.max(0, Math.min(ListMath.mean(modifiedJoyScores), 1));
        // Build the output map containing a scored sentence for each emotion
        Map<String, Float> moodScoreMap = new HashMap<>();
        moodScoreMap.put(Emotions.FEAR, sentenceFearScore);
        moodScoreMap.put(Emotions.ANGER, sentenceAngerScore);
        moodScoreMap.put(Emotions.SADNESS, sentenceSadnessScore);
        moodScoreMap.put(Emotions.JOY, sentenceJoyScore);
        // Return the mood score map
        return moodScoreMap;
    }

}
