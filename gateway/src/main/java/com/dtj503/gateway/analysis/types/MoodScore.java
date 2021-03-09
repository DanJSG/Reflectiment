package com.dtj503.gateway.analysis.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Mood score, containing a floating point score value, the strongest mood label and a map of moods to each of their
 * corresponding scores.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class MoodScore {

    @JsonProperty
    private float score;

    @JsonProperty
    private String label;

    @JsonProperty
    private Map<String, Float> mixedScores;

    @JsonCreator
    private MoodScore() {}

    /**
     * Create a mood score object with a specific score, label and map of mood labels to scores.
     * @param score the strongest mood score
     * @param label the strongest mood label
     * @param mixedScores a map of mood labels to scores
     */
    public MoodScore(float score, String label, Map<String, Float> mixedScores) {
        this.score = score;
        this.label = label;
        this.mixedScores = mixedScores;
    }

    /**
     * Get the map of labels to scores.
     *
     * @return the map of mood labels to scores
     */
    public Map<String, Float> getMixedScores() {
        return mixedScores;
    }
}
