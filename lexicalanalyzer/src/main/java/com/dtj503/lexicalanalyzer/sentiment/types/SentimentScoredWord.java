package com.dtj503.lexicalanalyzer.sentiment.types;

import com.dtj503.lexicalanalyzer.types.Word;

/**
 * Class for a word with an associated sentiment score.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class SentimentScoredWord extends Word {

    private float score;

    /**
     * Constructor for a word with a default scoring of 0.
     *
     * @param word the word
     * @param partOfSpeech the part of speech tag
     */
    public SentimentScoredWord(String word, String partOfSpeech) {
        this(word, partOfSpeech, 0);
    }

    /**
     * Constructor for a word with a specified scoring.
     *
     * @param word the word
     * @param partOfSpeech the part of speech tag
     * @param score the scoring of the word
     */
    public SentimentScoredWord(String word, String partOfSpeech, float score) {
        super(word, partOfSpeech);
        this.score = score;
    }

    public float getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "ScoredWord{" +
                "word='" + this.getWord() + '\'' +
                ", pos='" + this.getPartOfSpeech() + '\'' +
                ", score=" + score +
                '}';
    }
}
