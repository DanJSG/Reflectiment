package com.dtj503.lexicalanalyzer.types;

import com.dtj503.lexicalanalyzer.sentiment.types.ScoredSentence;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Class representing an analysis response JSON object. This is used for returning the analysis response to the user in
 * a JSON format following an API request.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class AnalysisResponse implements JsonObject {

    @JsonProperty
    List<ScoredSentence> scoredSentences;

    public AnalysisResponse(List<ScoredSentence> scoredSentences) {
        this.scoredSentences = scoredSentences;
    }

    @Override
    public String writeValueAsString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(this);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}