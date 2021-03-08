package com.dtj503.gateway.api.types;

import com.dtj503.gateway.analysis.types.AnalysisScores;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CombinedSentence {

    @JsonProperty
    private String sentence;

    @JsonProperty
    private AnalysisScores lexicalScores;

    @JsonProperty
    private AnalysisScores mlScores;

    public CombinedSentence(String sentence, AnalysisScores lexicalScores, AnalysisScores mlScores) {
        this.sentence = sentence;
        this.lexicalScores = lexicalScores;
        this.mlScores = mlScores;
    }

}
