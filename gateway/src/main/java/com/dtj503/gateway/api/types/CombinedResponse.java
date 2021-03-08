package com.dtj503.gateway.api.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class CombinedResponse implements JsonObject {

    @JsonProperty
    private String text;

    @JsonProperty
    private List<CombinedSentence> sentences;

    protected CombinedResponse(String text, List<CombinedSentence> sentences) {
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
