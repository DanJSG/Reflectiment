package com.dtj503.gateway.api.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnalysedSentence {

    @JsonProperty
    String sentence;

    @JsonProperty
    SentimentScore sentiment;

    @JsonProperty
    MoodScore mood;

    @JsonProperty
    ReflectionScore reflection;

    @JsonProperty
    ReflectionModifier reflectionModifier;

    public String getText() {
        return sentence;
    }

    public SentimentScore getSentimentScores() {
        return sentiment;
    }

    public MoodScore getMoodScores() {
        return mood;
    }

    public ReflectionScore getReflectionScores() {
        return reflection;
    }

    public ReflectionModifier getReflectionModifier() {
        return reflectionModifier;
    }
}
