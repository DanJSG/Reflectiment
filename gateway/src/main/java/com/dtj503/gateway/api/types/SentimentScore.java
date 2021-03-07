package com.dtj503.gateway.api.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SentimentScore {

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private float score;

    @JsonProperty
    private String label;

}
