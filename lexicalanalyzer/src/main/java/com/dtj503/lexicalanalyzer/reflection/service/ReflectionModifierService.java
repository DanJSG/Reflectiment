package com.dtj503.lexicalanalyzer.reflection.service;

import com.dtj503.lexicalanalyzer.sentiment.types.SentimentScoredSentence;

import java.util.ArrayList;
import java.util.List;

public class ReflectionModifierService {

    public static List<Float> getReflectionModifiers(List<SentimentScoredSentence> sentences) {
        List<Float> modifierList = new ArrayList<>();
        System.out.println("Function { getReflectionModifiers(x) } called.");
        for(SentimentScoredSentence sentence : sentences) {
            System.out.println(sentence.getSentenceSubjects());
        }
        return null;

    }

}
