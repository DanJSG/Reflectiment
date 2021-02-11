package com.dtj503.lexicalanalyzer.reflection.service;

import com.dtj503.lexicalanalyzer.mood.types.MoodScoredSentence;
import com.dtj503.lexicalanalyzer.sentiment.types.SentimentScoredSentence;

import java.util.ArrayList;
import java.util.List;

// TODO think about renaming this class + method
public class ReflectionMultiplierService {

    public static List<Float> getReflectionModifiers(List<SentimentScoredSentence> sentimentScoredSentences,
                                                     List<MoodScoredSentence> moodScoredSentences) {
        List<Float> reflectionModifiers = new ArrayList<>();
        for(int i = 0; i < sentimentScoredSentences.size(); i++) {
            float sentimentScore = sentimentScoredSentences.get(i).getScore();
            float moodScore = moodScoredSentences.get(i).getScore();
            float sentimentMultiplier = sentimentScore > 0 ? 1f : 0.315f;
            float moodMultiplier = sentimentScore > 0 ? 1f : 0.5f;
            float modifier = 1f + ((0.704f * Math.abs(sentimentScore)) * sentimentMultiplier);
            modifier *= (1f + ((0.129f * Math.abs(moodScore)) * moodMultiplier));
            reflectionModifiers.add(modifier);
        }
        return reflectionModifiers;
    }

}
