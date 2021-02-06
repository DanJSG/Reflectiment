package com.dtj503.lexicalanalyzer.common.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Class representing a scored sentence containing a collection of scored words with an overall calculated score.
 * @param <T> the word type contained in the sentence, must inherit from <code>ScoredWord</code>
 */
public class ScoredSentence<T extends ScoredWord> extends Sentence<T>{

    @JsonProperty
    private float score;

    /**
     * Constructor for a scored sentence.
     *
     * @param text the text in the sentence
     * @param words the tokenized words with associated scores that the sentence contains
     * @param score the score of the sentence
     */
    public ScoredSentence(String text, List<T> words, float score) {
        super(text, words);
        this.score = score;
    }

    public float getScore() {
        return score;
    }
}
