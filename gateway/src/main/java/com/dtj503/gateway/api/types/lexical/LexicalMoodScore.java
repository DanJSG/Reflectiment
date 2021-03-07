package com.dtj503.gateway.api.types.lexical;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class LexicalMoodScore {

    @JsonProperty
    private float score;

    @JsonProperty
    private String label;

    @JsonProperty
    private Map<String, Float> mixedScores;

}
