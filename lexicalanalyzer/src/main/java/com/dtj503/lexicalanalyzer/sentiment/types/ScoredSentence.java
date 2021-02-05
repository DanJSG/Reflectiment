package com.dtj503.lexicalanalyzer.sentiment.types;

import com.dtj503.lexicalanalyzer.types.JsonObject;
import com.dtj503.lexicalanalyzer.types.Sentence;
import com.dtj503.lexicalanalyzer.types.Token;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ScoredSentence extends Sentence {

    @JsonProperty
    private float score;

    public ScoredSentence(String text, List<Token> words, float score) {
        super(text, words);
        this.score = score;
    }

    public float getScore() {
        return score;
    }

}
