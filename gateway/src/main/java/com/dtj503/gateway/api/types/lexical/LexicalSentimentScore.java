package com.dtj503.gateway.api.types.lexical;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LexicalSentimentScore {

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private float score;

    @JsonProperty
    private String label;

}
