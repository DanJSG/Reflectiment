package com.dtj503.gateway.analysis.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SentimentScore {


    // TODO remove include non-null once sentiment model retrained
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private float score;

    @JsonProperty
    private String label;

}
