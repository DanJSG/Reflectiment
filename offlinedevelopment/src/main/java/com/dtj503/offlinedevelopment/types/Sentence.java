package com.dtj503.offlinedevelopment.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sentence {

    private String originalText;
    private List<Token> words;
    private Map<String, List<Integer>> posPositions;

    public Sentence(String text, List<Token> words) {
        this.originalText = text;
        this.words = words;
        this.posPositions = markPosPositions();
    }

    public List<Token> getWords() {
        return words;
    }

    public List<Float> getScores() {
        List<Float> scores = new ArrayList<>();
        for(Token word : words) {
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

    public String getOriginalText() {
        return originalText;
    }

    public List<Integer> getVerbPositions() {
        return posPositions.get("v");
    }

    public List<Integer> getNounPositions() {
        return posPositions.get("n");
    }

    public List<Integer> getAdjectivePositions() {
        return posPositions.get("a");
    }

    public List<Integer> getAdverbPositions() {
        return posPositions.get("r");
    }

    private Map<String, List<Integer>> markPosPositions() {
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
