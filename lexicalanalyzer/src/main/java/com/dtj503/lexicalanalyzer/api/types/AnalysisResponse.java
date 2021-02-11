package com.dtj503.lexicalanalyzer.api.types;

import com.dtj503.lexicalanalyzer.mood.types.MoodScoredSentence;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionScoredSentence;
import com.dtj503.lexicalanalyzer.sentiment.types.SentimentScoredSentence;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an analysis response JSON object. This is used for returning the analysis response to the user in
 * a JSON format following an API request.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class AnalysisResponse implements JsonObject {

    @JsonProperty("sentences")
    private List<AnalysedSentence> analysedSentences;

    @JsonProperty
    private String fullText;

    public AnalysisResponse(String fullText,
                            List<SentimentScoredSentence> sentimentScoredSentences,
                            List<MoodScoredSentence> moodScoredSentences,
                            List<ReflectionScoredSentence> reflectionScoredSentences,
                            List<Float> reflectionModifiers) {
        this.fullText = fullText;
        analysedSentences = new ArrayList<>();
        for(int i = 0; i < sentimentScoredSentences.size(); i++) {
            analysedSentences.add(new AnalysedSentence(sentimentScoredSentences.get(i), moodScoredSentences.get(i),
                    reflectionScoredSentences.get(i), reflectionModifiers.get(i)));
        }
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
