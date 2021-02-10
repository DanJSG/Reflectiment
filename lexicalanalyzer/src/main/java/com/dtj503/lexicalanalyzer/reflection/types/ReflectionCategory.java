package com.dtj503.lexicalanalyzer.reflection.types;

/**
 * Enum for the different categories of reflection outlined in Thomas Ullman's literature. String constants defined
 * using an Enum to allow easy looping from the analysis methods.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public enum ReflectionCategory {

    REFLECTION("reflection"),
    EXPERIENCE("experience"),
    FEELING("feeling"),
    BELIEF("belief"),
    DIFFICULTY("difficulty"),
    PERSPECTIVE("perspective"),
    LEARNING("learning"),
    INTENTION("intention");

    private final String category;

    /**
     * Private constructor used for setting the enum's category to allow the toString method to work properly. This is
     * implicitly called when something such as ReflectionCategories.FEELING is called.
     *
     * @param category the category of the enum instance
     */
    private ReflectionCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return this.category;
    }

}
