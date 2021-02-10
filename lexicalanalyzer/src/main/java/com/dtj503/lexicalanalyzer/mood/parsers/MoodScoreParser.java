package com.dtj503.lexicalanalyzer.mood.parsers;

import com.dtj503.lexicalanalyzer.common.parsers.ScoreParser;
import com.dtj503.lexicalanalyzer.common.types.ScoredWord;
import com.dtj503.lexicalanalyzer.common.types.Sentence;
import com.dtj503.lexicalanalyzer.common.utils.ListMath;
import com.dtj503.lexicalanalyzer.mood.types.Emotion;
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

        // Get the word scores from the modifier sentence
        List<Float> modifierScores = modifierSentence.getScores();
        // Create the modification vector based on the modifying adverbs in the modifier sentence
        List<Float> modificationVector = createModificationVector(modifierSentence, modifierScores);

        // Calculate the modified emotion scores and store them in a map where the score for each sentence is calculated
        // for each emotion
        Map<String, Float> moodScoreMap = new HashMap<>();
        for(Emotion emotion : Emotion.values()) {
            List<Float> currentScores = moodSentenceMap.get(emotion.toString()).getScores();
            List<Float> modifiedScores = stripZeroScores(ListMath.hadamardProduct(modificationVector, currentScores));
            float currentScore = Math.max(0, Math.min(ListMath.mean(modifiedScores), 1));
            moodScoreMap.put(emotion.toString(), currentScore);
        }

        return moodScoreMap;
    }

}
