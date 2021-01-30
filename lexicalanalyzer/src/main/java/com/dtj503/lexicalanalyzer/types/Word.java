package com.dtj503.lexicalanalyzer.types;

public class Word {

    private String word;
    private String partOfSpeech;

    public Word(String word, String partOfSpeech) {
        this.word = word;
        this.partOfSpeech = partOfSpeech;
    }

    public String getWord() {
        return word;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", pos='" + partOfSpeech + '\'' +
                '}';
    }
}
