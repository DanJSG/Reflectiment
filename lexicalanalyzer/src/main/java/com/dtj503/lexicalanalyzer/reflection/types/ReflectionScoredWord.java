package com.dtj503.lexicalanalyzer.reflection.types;

import com.dtj503.lexicalanalyzer.common.types.ScoredWord;

/**
 * Class representing a word which is scored for its level of reflection. Contains the word, its associated score and
 * Part of Speech (PoS) tag and the category of reflection it represents.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class ReflectionScoredWord extends ScoredWord {

    private String category;

    /**
     * Constructor for a scored word which takes the word, part of speech, category of reflection which it represents
     * and its score.
     *
     * @param word the word
     * @param partOfSpeech the PoS tag
     * @param score the score of the word
     * @param category the category of reflection the score represents
     */
    public ReflectionScoredWord(String word, String partOfSpeech, float score, String category) {
        super(word, partOfSpeech, score);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "ReflectionScoredWord{" +
                "word='" + this.getWord() + '\'' +
                ", pos='" + this.getPartOfSpeech() + '\'' +
                ", score=" + getScore() + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

}
