package com.dtj503.lexicalanalyzer.mood.types;

import com.dtj503.lexicalanalyzer.common.types.ScoredSentence;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class MoodScoredSentence extends ScoredSentence<MoodScoredWord> {

    @JsonProperty("label")
    private String strongestEmotionLabel;

    @JsonProperty("mixedScores")
    private Map<String, Float> moodScoreMap;

    public MoodScoredSentence(String text, List<MoodScoredWord> words, float strongestEmotionScore,
                              String strongestEmotionLabel, Map<String, Float> moodScoreMap) {
        super(text, words, strongestEmotionScore);
        this.strongestEmotionLabel = strongestEmotionLabel;
        this.moodScoreMap = moodScoreMap;
    }

}
