package com.dtj503.offlinedevelopment.types;

public class Word {

    private String word;
    private String pos;

    public Word(String word, String pos) {
        this.word = word;
        this.pos = pos;
    }

    public String getWord() {
        return word;
    }

    public String getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", pos='" + pos + '\'' +
                '}';
    }
}
