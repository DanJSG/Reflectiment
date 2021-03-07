package com.dtj503.gateway.api.types.lexical;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LexicalSentimentScore {

    @JsonProperty
    private float score;

    @JsonProperty
    private String label;

}
