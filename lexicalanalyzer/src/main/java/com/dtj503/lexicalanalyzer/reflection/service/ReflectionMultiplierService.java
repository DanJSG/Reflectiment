package com.dtj503.lexicalanalyzer.reflection.service;

import com.dtj503.lexicalanalyzer.sentiment.types.SentimentScoredSentence;

import java.util.ArrayList;
import java.util.List;

// TODO think about renaming this class + method
public class ReflectionMultiplierService {

    public static List<Float> getReflectionModifiers(List<SentimentScoredSentence> sentences) {
        List<Float> reflectionModifiers = new ArrayList<>();
        for(SentimentScoredSentence sentence : sentences) {
            float score = sentence.getScore();
            float multiplier = score > 0 ? 1f : 0.315f;
            float reflectionModifier = 1f + ((0.704f * Math.abs(score)) * multiplier);
            reflectionModifiers.add(reflectionModifier);
        }
        return reflectionModifiers;
    }

}
