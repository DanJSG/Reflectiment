package com.dtj503.gateway.analysis;

import com.dtj503.gateway.analysis.types.*;

import java.util.HashMap;
import java.util.Map;

public class AnalysisAverage {

    public static AnalysisScores averageScores(AnalysedSentence a, AnalysedSentence b) {
        SentimentScore sentimentScore = averageSentimentScores(a.getSentimentScores(), b.getSentimentScores());
        MoodScore moodScore = averageMoodScores(a.getMoodScores(), b.getMoodScores());
        ReflectionScore reflectionScore = averageReflectionScore(a.getReflectionScores(), b.getReflectionScores());
        return new AnalysisScores(sentimentScore, moodScore, reflectionScore);
    }

    private static MoodScore averageMoodScores(MoodScore a, MoodScore b) {
        Map<String, Float> aMixedScores = a.getMixedScores();
        Map<String, Float> bMixedScores = b.getMixedScores();

        Map<String, Float> newMixedScores = new HashMap<>();
        String label = "none";
        float highestScore = 0;

        for(String key : aMixedScores.keySet()) {
            float average = (aMixedScores.get(key) + bMixedScores.get(key)) / 2;
            label = (average > highestScore) ? key : label;
            highestScore = Math.max(average, highestScore);
            newMixedScores.put(key, average);
        }

        return new MoodScore(highestScore, label, newMixedScores);

    }

    private static SentimentScore averageSentimentScores(SentimentScore a, SentimentScore b) {
        float average = (a.getScore() + b.getScore()) / 2;
        String label;
        if(average >= -1 && average < -0.33) {
            label = "negative";
        } else if(average >= -0.33 && average < 0.33) {
            label = "neutral";
        } else {
            label = "negative";
        }
        return new SentimentScore(average, label);
    }

    private static ReflectionScore averageReflectionScore(ReflectionScore a, ReflectionScore b) {
        float average = (a.getScore() + b.getScore()) / 2;
        return new ReflectionScore(average);
    }

}
