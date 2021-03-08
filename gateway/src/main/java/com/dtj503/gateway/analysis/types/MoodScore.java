package com.dtj503.gateway.analysis.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class MoodScore {

    @JsonProperty
    private float score;

    @JsonProperty
    private String label;

    @JsonProperty
    private Map<String, Float> mixedScores;

}
