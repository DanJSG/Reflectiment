package com.dtj503.lexicalanalyzer.reflection.types;

public enum  ReflectionCategories {

    REFLECTION("reflection"),
    EXPERIENCE("experience"),
    FEELING("feeling"),
    BELIEF("belief"),
    DIFFICULTY("difficulty"),
    PERSPECTIVE("perspective"),
    LEARNING("learning"),
    INTENTION("intention");

    private final String category;

    private ReflectionCategories(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return this.category;
    }

}
