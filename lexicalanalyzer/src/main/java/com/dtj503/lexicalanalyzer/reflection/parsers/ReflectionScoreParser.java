package com.dtj503.lexicalanalyzer.reflection.parsers;

import com.dtj503.lexicalanalyzer.common.parsers.ScoreParser;
import com.dtj503.lexicalanalyzer.common.types.ScoredWord;
import com.dtj503.lexicalanalyzer.common.types.Sentence;
import com.dtj503.lexicalanalyzer.common.utils.ListMath;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionCategories;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionScoredWord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionScoreParser extends ScoreParser {

    public static Map<String, Float> parseSentenceScore(
            Map<String, Sentence<ReflectionScoredWord>> reflectionSentenceMap, Sentence<ScoredWord> modifierSentence) {
        System.out.println("Parsing reflection scores of sentence...");

        reflectionSentenceMap.forEach((category, sentence) -> {
            System.out.println(category + ": ");
            System.out.println(sentence);
        });

        Map<String, List<Float>> categoryScores = new HashMap<>();

        for(ReflectionCategories category : ReflectionCategories.values()) {
            categoryScores.put(category.toString(), reflectionSentenceMap.get(category.toString()).getScores());
        }

        List<Integer> adjectivePositions = modifierSentence.getAdjectivePositions();
        List<Integer> verbPositions = modifierSentence.getVerbPositions();
        List<Integer> adverbPositions = modifierSentence.getAdverbPositions();
        List<Float> modifierScores = modifierSentence.getScores();
        List<Float> modificationVector = createModificationVector(adjectivePositions, verbPositions, adverbPositions,
                modifierScores);

        Map<String, List<Float>> modifiedCategoryScores = new HashMap<>();
        for(ReflectionCategories value : ReflectionCategories.values()) {
            List<Float> newScores = ListMath.hadamardProduct(categoryScores.get(value.toString()), modificationVector);
            modifiedCategoryScores.put(value.toString(), newScores);
        }

        Map<String, Float> scoreMap = new HashMap<>();

        for(ReflectionCategories value : ReflectionCategories.values()) {
            float categoryScore = Math.max(0, Math.min(ListMath.mean(modifiedCategoryScores.get(value.toString())), 1));
            scoreMap.put(value.toString(), categoryScore);
        }

        return scoreMap;
    }

}
