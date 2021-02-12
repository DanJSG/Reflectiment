package com.dtj503.lexicalanalyzer.mood.types;

public enum Emotion {

    ANGER("anger"),
    FEAR("fear"),
    SADNESS("sadness"),
    JOY("joy");

    private final String emotion;

    Emotion(String emotion) {
        this.emotion = emotion;
    }

    @Override
    public String toString() {
        return emotion;
    }
}
