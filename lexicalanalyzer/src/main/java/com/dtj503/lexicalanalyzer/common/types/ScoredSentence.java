package com.dtj503.lexicalanalyzer.common.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ScoredSentence<T extends ScoredWord> extends Sentence<T>{

    @JsonProperty
    private float score;

    public ScoredSentence(String text, List<T> words, float score) {
        super(text, words);
        this.score = score;
    }

    public float getScore() {
        return score;
    }
}
