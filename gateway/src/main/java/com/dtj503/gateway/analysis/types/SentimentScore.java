package com.dtj503.gateway.analysis.types;

import com.fasterxml.jackson.annotation.JsonInclude;
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

}
