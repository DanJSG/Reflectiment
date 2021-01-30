package com.dtj503.lexicalanalyzer.types;

import com.dtj503.lexicalanalyzer.parsers.PartOfSpeechReducer;

/**
 * Class representing a word as well as the tag for the relevant part of speech.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class Word {

    private String word;
    private String partOfSpeech;
    private int partOfSpeechIndex;

    public Word(String word, String partOfSpeech) {
        this.word = word;
        this.partOfSpeech = partOfSpeech;
        this.partOfSpeechIndex = PartOfSpeechReducer.getPartOfSpeechIndex(partOfSpeech);
    }

    public String getWord() {
        return word;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public int getPartOfSpeechIndex() {
        return partOfSpeechIndex;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", pos='" + partOfSpeech + '\'' +
                '}';
    }
}
