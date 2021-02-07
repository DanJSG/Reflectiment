package com.dtj503.lexicalanalyzer.api.types;

import com.dtj503.lexicalanalyzer.mood.types.MoodScoredSentence;
import com.dtj503.lexicalanalyzer.sentiment.types.SentimentScoredSentence;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class for a fully analysed sentence, containing a scored sentence for sentiment, mood and reflection, along with
 * the original text.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class AnalysedSentence {

    @JsonProperty("mood")
    private final MoodScoredSentence moodScoredSentence;

    @JsonProperty("sentiment")
    private final SentimentScoredSentence sentimentScoredSentence;

    @JsonProperty("sentence")
    private final String text;

    /**
     * Constructor for a fully analysed sentence.
     *
     * @param sentimentScoredSentence the sentiment scoring of the sentence
     * @param moodScoredSentence the mood scoring of the sentence
     */
    public AnalysedSentence(SentimentScoredSentence sentimentScoredSentence, MoodScoredSentence moodScoredSentence) {
        this.sentimentScoredSentence = sentimentScoredSentence;
        this.moodScoredSentence = moodScoredSentence;
        this.text = sentimentScoredSentence.getOriginalText();
    }

}
