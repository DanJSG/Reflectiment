package com.dtj503.lexicalanalyzer.api.types;

import com.dtj503.lexicalanalyzer.mood.types.MoodScoredSentence;
import com.dtj503.lexicalanalyzer.sentiment.types.SentimentScoredSentence;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AnalysedSentence {

    @JsonProperty("mood")
    private final MoodScoredSentence moodScoredSentence;

    @JsonProperty("sentiment")
    private final SentimentScoredSentence sentimentScoredSentence;

    @JsonProperty("sentence")
    private final String text;

    public AnalysedSentence(SentimentScoredSentence sentimentScoredSentence, MoodScoredSentence moodScoredSentence) {
        this.sentimentScoredSentence = sentimentScoredSentence;
        this.moodScoredSentence = moodScoredSentence;
        this.text = sentimentScoredSentence.getOriginalText();
    }

}
