package com.dtj503.lexicalanalyzer.api.types;

import com.dtj503.lexicalanalyzer.mood.types.MoodScoredSentence;
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

    @JsonProperty
    List<SentimentScoredSentence> sentimentScoredSentences;

    @JsonProperty
    List<MoodScoredSentence> moodScoredSentences;

    public AnalysisResponse(List<SentimentScoredSentence> sentimentScoredSentences,
                            List<MoodScoredSentence> moodScoredSentences) {
        this.sentimentScoredSentences = sentimentScoredSentences;
        this.moodScoredSentences = moodScoredSentences;
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

//    private List<String> getOriginalSentences(List<SentimentScoredSentence> sentences) {
//        List<String> sentencesText = new ArrayList<>();
//        for(SentimentScoredSentence sentence : sentences) {
//            sentencesText.add(sentence.getOriginalText());
//        }
//        return sentencesText;
//    }

}
