package com.dtj503.lexicalanalyzer.reflection.service;

import com.dtj503.lexicalanalyzer.common.parsers.StringParser;
import com.dtj503.lexicalanalyzer.common.services.AnalysisService;
import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.sql.SQLTable;
import com.dtj503.lexicalanalyzer.common.types.Document;
import com.dtj503.lexicalanalyzer.common.types.Sentence;
import com.dtj503.lexicalanalyzer.common.types.Token;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionCategories;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionScoredSentence;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionScoredWord;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionScoredWordBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionAnalysisService extends AnalysisService {

    public static void analyseReflection(String text) {
        System.out.println("In reflection analysis service!");
        System.out.println(text);

        Document<Token> doc = StringParser.parseText(text);
        List<ReflectionScoredSentence> scoredSentences = new ArrayList<>();
        for(Sentence<Token> sentence : doc.getSentences()) {
            List<Token> words = sentence.getWords();
            List<ReflectionScoredWord> reflectionScoredWords = fetchWordScores(words, SQLTable.REFLECTION,
                                                                               SQLColumn.WORD,
                                                                               new ReflectionScoredWordBuilder());
            if(reflectionScoredWords == null) {
                scoredSentences.add(getZeroScoreSentence(sentence.getOriginalText()));
                continue;
            }
            Map<String, List<ReflectionScoredWord>> scoredWordMap = buildScoredWordMap(words, reflectionScoredWords);
            System.out.println(reflectionScoredWords);
        }

    }

    private static ReflectionScoredSentence getZeroScoreSentence(String originalText) {
        Map<String, Float> zeroScoreMap = new HashMap<>();
        zeroScoreMap.put(ReflectionCategories.REFLECTION, 0f);
        zeroScoreMap.put(ReflectionCategories.EXPERIENCE, 0f);
        zeroScoreMap.put(ReflectionCategories.FEELING, 0f);
        zeroScoreMap.put(ReflectionCategories.BELIEF, 0f);
        zeroScoreMap.put(ReflectionCategories.DIFFICULTY, 0f);
        zeroScoreMap.put(ReflectionCategories.PERSPECTIVE, 0f);
        zeroScoreMap.put(ReflectionCategories.LEARNING, 0f);
        zeroScoreMap.put(ReflectionCategories.INTENTION, 0f);
        return new ReflectionScoredSentence(originalText, null, 0, zeroScoreMap);
    }



}
