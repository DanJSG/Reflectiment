package com.dtj503.gateway.analysis.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Reflection score containing a float score and a map of reflection categories with individual scores.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReflectionScore {

    @JsonProperty
    private float score;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Float> categoryScores;

    // Blank creator used for JSON deserialization by JSON library
    @JsonCreator
    private ReflectionScore() {}

    public ReflectionScore(float score) {
        this.score = score;
    }

    /**
     * Constructor which takes the float score and the category score map.
     *
     * @param score the reflection score
     * @param categoryScores the score of each reflection category
     */
    public ReflectionScore(float score, Map<String, Float> categoryScores) {
        this.score = score;
        this.categoryScores = categoryScores;
    }

    /**
     * Get the float score value.
     *
     * @return <code>float</code> score
     */
    public float getScore() {
        return score;
    }

    /**
     * Get the category score map.
     *
     * @return <code>Map</code> of categories to scores
     */
    public Map<String, Float> getCategoryScores() {
        return categoryScores;
    }
}
