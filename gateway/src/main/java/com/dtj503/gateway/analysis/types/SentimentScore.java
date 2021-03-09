package com.dtj503.gateway.analysis.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Sentiment score, containing a float score and an associated label.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class SentimentScore {

    @JsonProperty
    private float score;

    @JsonProperty
    private String label;

    @JsonCreator
    private SentimentScore() {}

    public SentimentScore(float score, String label) {
        this.score = score;
        this.label = label;
    }

    /**
     * Get the float score value.
     *
     * @return the <code>float</code> score value
     */
    public float getScore() {
        return score;
    }
}
