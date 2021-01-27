package com.dtj503.lexicalanalyzer.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

public class AnalysisRequest implements JsonObject {

    @JsonProperty
    private String text;

    @JsonProperty
    private LocalDateTime timestamp;

    @JsonCreator
    private AnalysisRequest() {}

    public AnalysisRequest(String text, LocalDateTime timestamp) {
        this.text = text;
        this.timestamp = timestamp;
    }

    public AnalysisRequest(String text) {
        this(text, LocalDateTime.now());
    }

    @Override
    public String writeValueAsString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(this);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
