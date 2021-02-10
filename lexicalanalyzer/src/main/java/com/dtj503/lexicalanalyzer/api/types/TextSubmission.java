package com.dtj503.lexicalanalyzer.api.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

/**
 * Class for a JSON format text submission to be analysed by the application.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class TextSubmission implements JsonObject {

    @JsonProperty
    private String text;

    @JsonProperty
    // THIS MUST TAKE A DATE STRING IN ISO FORMAT SO THE FRONT END MUST USE SOMETHING SUCH AS
    //  new Date().toISOString()
    // FOR THE DATE TO WORK PROPERLY AND NOT THROW AN ERROR
    private LocalDateTime timestamp;

    @JsonCreator
    /**
     * Empty private constructor for the JSON deserialization library to use to automatically generate the object from
     * a JSON string
     */
    private TextSubmission() {}

    /**
     * Constructor to create a text submission object containing some text and a given timestamp.
     * @param text the submission text
     * @param timestamp the time the submission was made
     */
    public TextSubmission(String text, LocalDateTime timestamp) {
        this.text = text;
        this.timestamp = timestamp;
    }

    /**
     * Constructor to create a text submission object containing some text and the current local time.
     * @param text the submission text
     */
    public TextSubmission(String text) {
        this(text, LocalDateTime.now());
    }

    /**
     * Method to get the text of the submission.
     * @return the submission text
     */
    public String getText() {
        return text;
    }

    /**
     * Method to get the timestamp of the submission.
     * @return the time of submission
     */
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
