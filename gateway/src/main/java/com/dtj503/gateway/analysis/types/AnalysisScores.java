package com.dtj503.gateway.analysis.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Grouped analysis scores containing sentiment, mood and reflection scores.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class AnalysisScores {

    @JsonProperty
    private SentimentScore sentiment;

    @JsonProperty
    private MoodScore mood;

    @JsonProperty
    private ReflectionScore reflection;

    /**
     * Constructor for the grouped analysis scores, taking in each of the individual analysis scores.
     *
     * @param sentiment the sentiment analysis scores
     * @param mood the mood analysis scores
     * @param reflection the reflection analysis scores
     */
    public AnalysisScores(SentimentScore sentiment, MoodScore mood, ReflectionScore reflection) {
        this.sentiment = sentiment;
        this.mood = mood;
        this.reflection = reflection;
    }

}
