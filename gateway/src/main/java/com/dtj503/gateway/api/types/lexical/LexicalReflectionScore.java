package com.dtj503.gateway.api.types.lexical;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class LexicalReflectionScore {

    @JsonProperty
    private float score;

    @JsonProperty
    private Map<String, Float> categoryScores;

    @JsonCreator
    private LexicalReflectionScore() {}

    public LexicalReflectionScore(float score, Map<String, Float> categoryScores) {
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
