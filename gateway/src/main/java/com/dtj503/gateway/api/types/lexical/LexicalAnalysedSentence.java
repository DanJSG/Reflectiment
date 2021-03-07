package com.dtj503.gateway.api.types.lexical;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LexicalAnalysedSentence {

    @JsonProperty
    String sentence;

    @JsonProperty
    LexicalSentimentScore sentiment;

    @JsonProperty
    LexicalMoodScore mood;

    @JsonProperty
    LexicalReflectionScore reflection;

    @JsonProperty
    ReflectionModifier reflectionModifier;

    public String getText() {
        return sentence;
    }

    public LexicalSentimentScore getSentimentScores() {
        return sentiment;
    }

    public LexicalMoodScore getMoodScores() {
        return mood;
    }

    public LexicalReflectionScore getReflectionScores() {
        return reflection;
    }

    public ReflectionModifier getReflectionModifier() {
        return reflectionModifier;
    }
}
