package com.dtj503.lexicalanalyzer.common.types;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing a sentence. Contains the original sentence text and a list of word tokens.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class Sentence<T extends Token> {

    @JsonIgnore
    private String originalText;

    @JsonIgnore
    private List<T> words;

    @JsonIgnore
    private Map<String, List<Integer>> posPositions;

    public Sentence(String text, List<T> words) {
        System.out.println("Words: " + words);
        this.originalText = text;
        this.words = words;
        this.posPositions = markPosPositions(words);
    }

    public List<T> getWords() {
        return words;
    }

    @JsonIgnore
    public List<Float> getScores() {
        List<Float> scores = new ArrayList<>();
        for(T word : words) {
            scores.add(word.getScore());
        }
        return scores;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "originalText='" + originalText + '\'' +
                ", words=" + words +
                '}';
    }

    @JsonIgnore
    public String getOriginalText() {
        return originalText;
    }

    @JsonIgnore
    public List<Integer> getVerbPositions() {
        return posPositions.get("v");
    }

    @JsonIgnore
    public List<Integer> getNounPositions() {
        return posPositions.get("n");
    }

    @JsonIgnore
    public List<Integer> getAdjectivePositions() {
        return posPositions.get("a");
    }

    @JsonIgnore
    public List<Integer> getAdverbPositions() {
        return posPositions.get("r");
    }

    /**
     * Method to mark the part of speech positions in the sentence in a map. The key is the part of speech abbreviation
     * ie. "v", "r", "a" or "n" and the object is a list of the <code>integer</code> indices of these parts of speech
     * in the sentence.
     *
     * @return a map of pos tags to word indices
     */
    private Map<String, List<Integer>> markPosPositions(List<T> words) {
        if(words == null) {
            return null;
        }
        Map<String, List<Integer>> positions = new HashMap<>();
        for(int i = 0; i < words.size(); i++) {
            if(!positions.containsKey(words.get(i).getPartOfSpeech())) {
                positions.put(words.get(i).getPartOfSpeech(), new ArrayList<>());
            }
            positions.get(words.get(i).getPartOfSpeech()).add(i);
        }
        return positions;
    }

}
