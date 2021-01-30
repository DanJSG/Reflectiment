package com.dtj503.lexicalanalyzer.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a sentence. Contains the original sentence text and a list of word tokens.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
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
