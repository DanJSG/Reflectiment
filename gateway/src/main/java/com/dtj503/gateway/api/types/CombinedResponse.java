package com.dtj503.gateway.api.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * A JSON serializable response containing the submitted text and each sentences sentiment, mood and reflection analysis
 * scores.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class CombinedResponse implements JsonObject {

    @JsonProperty
    private String text;

    @JsonProperty
    private List<CombinedSentence> sentences;

    /**
     * Constructor which takes the full text and the scored sentences.
     *
     * @param text the full submission text
     * @param sentences the analysed sentences
     */
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
