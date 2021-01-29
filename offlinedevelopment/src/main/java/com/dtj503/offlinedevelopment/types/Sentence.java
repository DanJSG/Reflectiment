package com.dtj503.offlinedevelopment.types;

import java.util.ArrayList;
import java.util.List;

public class Sentence {

    private String text;
    private List<Word> words;

    public Sentence(String sentenceString, List<String> tokens, List<String> posTags) throws Exception {

        if(tokens.size() != posTags.size()) {
            throw new Exception("The number of tokens and PoS tags are not equal. Cannot create sentence.");
        }

        text = sentenceString;
        words = new ArrayList<>(tokens.size());

        for(int i = 0; i < tokens.size(); i++) {
            words.add(new Word(tokens.get(i), posTags.get(i)));
        }

    }

    public List<Word> getWords() {
        return words;
    }

    @Override
    public String toString() {
        return text;
    }
}
