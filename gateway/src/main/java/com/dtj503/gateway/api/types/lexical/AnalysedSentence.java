package com.dtj503.gateway.api.types.lexical;

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
    ReflectionModifiers reflectionModifier;

}
