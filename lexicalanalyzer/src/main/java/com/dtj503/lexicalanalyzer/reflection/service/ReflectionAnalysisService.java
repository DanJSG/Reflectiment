package com.dtj503.lexicalanalyzer.reflection.service;

import com.dtj503.lexicalanalyzer.common.parsers.StringParser;
import com.dtj503.lexicalanalyzer.common.services.AnalysisService;
import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.sql.SQLTable;
import com.dtj503.lexicalanalyzer.common.types.*;
import com.dtj503.lexicalanalyzer.reflection.parsers.ReflectionScoreParser;
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
                    SQLColumn.WORD, new ReflectionScoredWordBuilder());
            if(reflectionScoredWords == null) {
                scoredSentences.add(getZeroScoreSentence(sentence.getOriginalText()));
                continue;
            }
            Map<String, List<ReflectionScoredWord>> scoredWordMap = buildScoredWordMap(words, reflectionScoredWords);
            Map<String, Sentence<ReflectionScoredWord>> reflectionSentenceMap = buildReflectionSentenceMap(sentence,
                    scoredWordMap);

            // Fetch and choose the modifier word scores
            List<ScoredWord> modifierScoredWords = fetchWordScores(words, SQLTable.SENTIMENT, SQLColumn.WORD,
                    new ScoredWordBuilder());
            modifierScoredWords = pickScoredWord(words, modifierScoredWords);
            // Convert the modifier words into a sentence
            Sentence<ScoredWord> modifierSentence = new Sentence<>(sentence.getOriginalText(), modifierScoredWords);
            Map<String, Float> reflectionMap = ReflectionScoreParser.parseSentenceScore(reflectionSentenceMap,
                    modifierSentence);

        }

    }

    private static ReflectionScoredSentence getZeroScoreSentence(String originalText) {
        Map<String, Float> zeroScoreMap = new HashMap<>();
        for(ReflectionCategories value : ReflectionCategories.values()) {
            zeroScoreMap.put(value.toString(), 0f);
        }
        return new ReflectionScoredSentence(originalText, null, 0, zeroScoreMap);
    }

    private static Map<String, Sentence<ReflectionScoredWord>> buildReflectionSentenceMap(
            Sentence<Token> sentence, Map<String, List<ReflectionScoredWord>> scoredWordMap) {
        Map<String, Sentence<ReflectionScoredWord>> reflectionMap = new HashMap<>();
        List<Token> words = sentence.getWords();
        for(ReflectionCategories value : ReflectionCategories.class.getEnumConstants()) {
            List<ReflectionScoredWord> currentScoredWords = pickReflectionScoredWords(words, value.toString(),
                    scoredWordMap);
            reflectionMap.put(value.toString(), new Sentence<>(sentence.getOriginalText(), currentScoredWords));
        }
        return reflectionMap;
    }

    private static List<ReflectionScoredWord> pickReflectionScoredWords(List<Token> words, String category,
            Map<String, List<ReflectionScoredWord>> reflectionScoredWordMap) {
        List<ReflectionScoredWord> pickedWords = new ArrayList<>();
        for(Token word : words) {
            List<ReflectionScoredWord> scoredWords = reflectionScoredWordMap.get(word.getWord());
            int index = -1;
            for(int i = 0; i < scoredWords.size(); i++) {
                if(scoredWords.get(i).getCategory().contentEquals(category)) {
                    index = i;
                    break;
                }
            }
            if(index == -1) {
                pickedWords.add(new ReflectionScoredWord(word.getWord(), word.getPartOfSpeech(),  0, category));
            } else {
                pickedWords.add(scoredWords.get(index));
            }
        }
        return pickedWords;
    }



}
