package com.dtj503.gateway.api.types;

import com.dtj503.gateway.api.types.lexical.LexicalMoodScore;
import com.dtj503.gateway.api.types.lexical.LexicalReflectionScore;
import com.dtj503.gateway.api.types.lexical.LexicalSentimentScore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LexicalScores {

    @JsonProperty
    private LexicalSentimentScore sentiment;

    @JsonProperty
    private LexicalMoodScore mood;

    @JsonProperty
    private LexicalReflectionScore reflection;

    public LexicalScores(LexicalSentimentScore sentiment, LexicalMoodScore mood, LexicalReflectionScore reflection) {
        this.sentiment = sentiment;
        this.mood = mood;
        this.reflection = reflection;
    }

}
