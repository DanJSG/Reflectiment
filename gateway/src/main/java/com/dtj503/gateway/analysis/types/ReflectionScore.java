package com.dtj503.gateway.analysis.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReflectionScore {

    @JsonProperty
    private float score;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Float> categoryScores;

    @JsonCreator
    private ReflectionScore() {}

    public ReflectionScore(float score, Map<String, Float> categoryScores) {
        this.score = score;
        this.categoryScores = categoryScores;
    }

    public float getScore() {
        return score;
    }

    public Map<String, Float> getCategoryScores() {
        return categoryScores;
    }
}
