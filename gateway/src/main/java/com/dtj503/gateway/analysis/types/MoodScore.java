package com.dtj503.gateway.analysis.types;

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

}
