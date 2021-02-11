package com.dtj503.lexicalanalyzer.mood.types;

import com.dtj503.lexicalanalyzer.common.types.ScoredSentence;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Class representing a sentence with a set of emotion intensity scores.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class MoodScoredSentence extends ScoredSentence<MoodScoredWord> {

    @JsonProperty("label")
    // Label for the strongest emotion
    private String strongestEmotionLabel;

    @JsonProperty("mixedScores")
    // The scores for each emotion (anger, fear, sadness, joy)
    private Map<String, Float> moodScoreMap;

    /**
     * Constructor which stores the original sentence text, the scored words, the String label of the strongest emotion,
     * the score of the strongest emotion, and a map containing the scores for each emotion.
     *
     * @param text the original sentence text
     * @param words the list of scored, PoS tagged words
     * @param strongestEmotionScore the highest emotional intensity score
     * @param strongestEmotionLabel the highest emotional intensity label
     * @param moodScoreMap a map of the scores for each emotion (anger, fear, sadness, joy)
     */
    public MoodScoredSentence(String text, List<MoodScoredWord> words, List<String> sentenceSubjects,
                              float strongestEmotionScore, String strongestEmotionLabel,
                              Map<String, Float> moodScoreMap) {
        super(text, words, sentenceSubjects, strongestEmotionScore);
        this.strongestEmotionLabel = strongestEmotionLabel;
        this.moodScoreMap = moodScoreMap;
    }

}
