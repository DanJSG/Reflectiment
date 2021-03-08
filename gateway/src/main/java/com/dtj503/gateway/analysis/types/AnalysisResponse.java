package com.dtj503.gateway.analysis.types;

import com.dtj503.gateway.api.types.JsonObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class AnalysisResponse implements JsonObject {

    @JsonProperty
    private String fullText;

    @JsonProperty
    private List<AnalysedSentence> sentences;

    public String getFullText() {
        return fullText;
    }

    public List<AnalysedSentence> getSentences() {
        return sentences;
    }

    @Override
    public String writeValueAsString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
