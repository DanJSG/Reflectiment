package com.dtj503.lexicalanalyzer.reflection.types;

import com.dtj503.lexicalanalyzer.common.types.ScoredWord;

public class ReflectionScoredWord extends ScoredWord {

    private String category;

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
