package com.dtj503.lexicalanalyzer.reflection.parsers;

import com.dtj503.lexicalanalyzer.common.parsers.ScoreParser;
import com.dtj503.lexicalanalyzer.common.types.ScoredWord;
import com.dtj503.lexicalanalyzer.common.types.Sentence;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionScoredWord;

import java.util.Map;

public class ReflectionScoreParser extends ScoreParser {

    public static Map<String, Float> parseSentenceScore(
            Map<String, Sentence<ReflectionScoredWord>> reflectionSentenceMap, Sentence<ScoredWord> modifierSentence) {
        System.out.println("Parsing reflection scores of sentence...");

        reflectionSentenceMap.forEach((category, sentence) -> {
            System.out.println(category + ": ");
            System.out.println(sentence);
        });

        return null;
    }

}
