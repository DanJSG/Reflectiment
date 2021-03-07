package com.dtj503.gateway.api.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnalysisScores {

    @JsonProperty
    private SentimentScore sentiment;

    @JsonProperty
    private MoodScore mood;

    @JsonProperty
    private ReflectionScore reflection;

    public AnalysisScores(SentimentScore sentiment, MoodScore mood, ReflectionScore reflection) {
        this.sentiment = sentiment;
        this.mood = mood;
        this.reflection = reflection;
    }

}
