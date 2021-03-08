package com.dtj503.gateway.analysis.types;

import com.dtj503.gateway.api.types.JsonObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Analysis response containing the full string of submitted text, and then a list containing each sentence from this
 * text with its corresponding analysis details.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class AnalysisResponse implements JsonObject {

    @JsonProperty
    private String fullText;

    @JsonProperty
    private List<AnalysedSentence> sentences;

    /**
     * Get the full string of submitted text.
     *
     * @return the full submitted text
     */
    public String getFullText() {
        return fullText;
    }

    /**
     * Get the list of analysed sentences.
     *
     * @return a list of sentences with their corresponding analysis details.
     */
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
