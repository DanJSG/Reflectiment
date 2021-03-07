package com.dtj503.gateway.api.types.lexical;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SentimentScore {

    @JsonProperty
    private float score;

    @JsonProperty
    private String label;

}
