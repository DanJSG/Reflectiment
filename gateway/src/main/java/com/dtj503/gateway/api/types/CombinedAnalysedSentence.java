package com.dtj503.gateway.api.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CombinedAnalysedSentence {

    @JsonProperty
    private String sentence;

    @JsonProperty
    private AnalysisScores lexicalScores;

    @JsonProperty
    private AnalysisScores mlScores;

    public CombinedAnalysedSentence(String sentence, AnalysisScores lexicalScores, AnalysisScores mlScores) {
        this.sentence = sentence;
        this.lexicalScores = lexicalScores;
        this.mlScores = mlScores;
    }

}
