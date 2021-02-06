package com.dtj503.lexicalanalyzer.sentiment.types;

import com.dtj503.lexicalanalyzer.types.Sentence;
import com.dtj503.lexicalanalyzer.types.Token;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Class representing a sentence with a calculated sentiment score.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class SentimentScoredSentence extends Sentence<SentimentScoredWord> {

    @JsonProperty
    private float score;

    public SentimentScoredSentence(String text, List<SentimentScoredWord> words, float score) {
        super(text, words);
        this.score = score;
    }

    public float getScore() {
        return score;
    }

}
