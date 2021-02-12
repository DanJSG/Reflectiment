package com.dtj503.lexicalanalyzer.reflection.types;

import com.dtj503.lexicalanalyzer.common.types.ScoredSentence;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Map;

/**
 * Class representing a sentence which has a score representing its level of reflection, as well as individual scores
 * for different categories which are deemed to make up reflection.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
@JsonPropertyOrder({"score", "label", "categoryScores"})
public class ReflectionScoredSentence extends ScoredSentence<ReflectionScoredWord> {

    @JsonProperty("categoryScores")
    private final Map<String, Float> categoryScoreMap;

    /**
     * Constructor for a scored sentence which takes the original sentence text, the set of scored words, the sentence's
     * score and a map of different scores to the different categories of reflection.
     *
     * @param text  the text in the sentence
     * @param words the tokenized words with associated scores that the sentence contains
     * @param score the score of the sentence
     */
    public ReflectionScoredSentence(String text, List<ReflectionScoredWord> words, float score,
                                    Map<String, Float> categoryScoreMap) {
        super(text, words, score);
        this.categoryScoreMap = categoryScoreMap;
    }

}
