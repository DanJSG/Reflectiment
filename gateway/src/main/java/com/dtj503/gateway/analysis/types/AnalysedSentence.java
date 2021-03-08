package com.dtj503.gateway.analysis.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An analysed sentence containing the sentence text and analysis details for sentiment, mood, and reflection.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
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

    /**
     * Get the sentence text.
     *
     * @return the sentence text
     */
    public String getText() {
        return sentence;
    }

    /**
     * Get the sentiment analysis details for the sentence.
     *
     * @return <code>SentimentScore</code> object with the sentiment analysis details
     */
    public SentimentScore getSentimentScores() {
        return sentiment;
    }

    /**
     * Get the mood analysis details for the sentence.
     *
     * @return <code>MoodScore</code> object with the mood analysis details
     */
    public MoodScore getMoodScores() {
        return mood;
    }

    /**
     * Get the reflection analysis details for the sentence.
     *
     * @return <code>ReflectionScore</code> object with the reflection analysis details.
     */
    public ReflectionScore getReflectionScores() {
        return reflection;
    }

    /**
     * Get the reflection modifier values for the sentence.
     *
     * @return <code>ReflectionModifier</code> object with the reflection modifiers, or <code>null</code>.
     */
    public ReflectionModifier getReflectionModifier() {
        return reflectionModifier;
    }
}
