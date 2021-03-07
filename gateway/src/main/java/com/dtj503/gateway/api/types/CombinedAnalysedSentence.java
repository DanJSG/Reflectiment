package com.dtj503.gateway.api.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CombinedAnalysedSentence {

    @JsonProperty
    private String sentence;

    @JsonProperty
    private LexicalScores lexicalScores;

    public CombinedAnalysedSentence(String sentence, LexicalScores scores) {
        this.sentence = sentence;
        this.lexicalScores = scores;
    }

}
