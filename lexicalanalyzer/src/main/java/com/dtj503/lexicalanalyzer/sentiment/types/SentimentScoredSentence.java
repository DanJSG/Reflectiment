package com.dtj503.lexicalanalyzer.sentiment.types;

import com.dtj503.lexicalanalyzer.common.types.ScoredSentence;
import com.dtj503.lexicalanalyzer.common.types.ScoredWord;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Class representing a sentence with a calculated sentiment score.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class SentimentScoredSentence extends ScoredSentence<ScoredWord> {

    @JsonProperty
    private String label;

    public SentimentScoredSentence(String text, List<ScoredWord> words, List<String> sentenceSubjects, float score) {
        super(text, words, sentenceSubjects, score);
        this.label = pickLabel(score);
    }

    public String getLabel() {
        return label;
    }

    private static String pickLabel(float score) {
        if(score > -0.33 && score < 0.33) {
            return "neutral";
        } else if(score > 0) {
            return "positive";
        } else {
            return "negative";
        }
    }

}
