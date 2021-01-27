package com.dtj503.lexicalanalyzer.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Date;

public class AnalysisRequest implements JsonObject {

    @JsonProperty
    private String analysisText;

    @JsonProperty
    private LocalDateTime timestamp;

    @JsonCreator
    private AnalysisRequest() {}

    public AnalysisRequest(String analysisText, LocalDateTime timestamp) {
        this.analysisText = analysisText;
        this.timestamp = timestamp;
    }

    public AnalysisRequest(String analysisText) {
        this(analysisText, LocalDateTime.now());
    }

    @Override
    public String writeValueAsString() {
        return null;
    }
}
