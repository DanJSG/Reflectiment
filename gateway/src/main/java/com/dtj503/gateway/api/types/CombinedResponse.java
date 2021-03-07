package com.dtj503.gateway.api.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class CombinedResponse implements JsonObject {

    @JsonProperty
    private String text;

    @JsonProperty
    private List<CombinedAnalysedSentence> sentences;

    public CombinedResponse(String text, List<CombinedAnalysedSentence> sentences) {
        this.text = text;
        this.sentences = sentences;
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
