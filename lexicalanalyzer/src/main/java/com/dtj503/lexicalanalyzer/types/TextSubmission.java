package com.dtj503.lexicalanalyzer.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

public class TextSubmission implements JsonObject {

    @JsonProperty
    private String text;

    @JsonProperty
    // THIS MUST TAKE A DATE STRING IN ISO FORMAT SO THE FRONT END MUST USE SOMETHING SUCH AS
    //  new Date().toISOString()
    // FOR THE DATE TO WORK PROPERLY AND NOT THROW AN ERROR
    private LocalDateTime timestamp;

    @JsonCreator
    private TextSubmission() {}

    public TextSubmission(String text, LocalDateTime timestamp) {
        this.text = text;
        this.timestamp = timestamp;
    }

    public TextSubmission(String text) {
        this(text, LocalDateTime.now());
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
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
