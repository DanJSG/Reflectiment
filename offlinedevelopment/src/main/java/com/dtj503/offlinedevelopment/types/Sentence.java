package com.dtj503.offlinedevelopment.types;

import java.util.List;

public class Sentence {

    private String originalText;
    private List<Word> words;

    public Sentence(String text, List<Word> words) {
        this.originalText = text;
        this.words = words;
    }

    public List<Word> getWords() {
        return words;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "originalText='" + originalText + '\'' +
                ", words=" + words +
                '}';
    }

    public String getOriginalText() {
        return originalText;
    }
}
