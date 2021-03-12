package com.dtj503.gateway.api.types;

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
    private String timestamp;

    /**
     * Empty private constructor for the JSON deserialization library to use to automatically generate the object from
     * a JSON string
     */
    @JsonCreator
    private TextSubmission() {}

    /**
     * Method to get the text of the submission.
     * @return the submission text
     */
    public String getText() {
        return text;
    }

    @Override
    public String writeValueAsString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
