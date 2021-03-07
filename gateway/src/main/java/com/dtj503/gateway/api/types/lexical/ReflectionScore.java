package com.dtj503.gateway.api.types.lexical;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ReflectionScore {

    @JsonProperty
    private float score;

    @JsonProperty
    private Map<String, Float> categoryScores;

}
