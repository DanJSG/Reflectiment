package com.dtj503.gateway.api.types;

import com.dtj503.gateway.analysis.types.AnalysisScores;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Combined sentence, containing sentence text and both the lexical and ML analysis scores.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class CombinedSentence {

    @JsonProperty
    private String sentence;

    @JsonProperty
    private AnalysisScores lexicalScores;

    @JsonProperty
    private AnalysisScores mlScores;

    /**
     * Constructor which takes the sentence string and the lexical and ML analysis scores.
     *
     * @param sentence the sentence string
     * @param lexicalScores the lexical analysis scores
     * @param mlScores the ML analysis scores
     */
    public CombinedSentence(String sentence, AnalysisScores lexicalScores, AnalysisScores mlScores) {
        this.sentence = sentence;
        this.lexicalScores = lexicalScores;
        this.mlScores = mlScores;
    }

}
