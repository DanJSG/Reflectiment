package com.dtj503.lexicalanalyzer.common.services;

import com.dtj503.lexicalanalyzer.common.types.ScoredWord;
import com.dtj503.lexicalanalyzer.common.types.Token;
import com.dtj503.lexicalanalyzer.sentiment.types.SentimentScoredSentence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AnalysisService {

    /**
     * Method for building a scored word map. This generates a map where the keys are words and the objects are lists of
     * associated scored words.
     *
     * @param words the words for the keys
     * @param scoredWords the scored words
     * @return
     */
    protected static <T extends ScoredWord> Map<String, List<T>> buildScoredWordMap(List<Token> words, List<T> scoredWords) {
        Map<String, List<T>> scoredWordMap = new HashMap<>();
        // Loop over each word and add it to the map
        for(Token word : words) {
            scoredWordMap.put(word.getWord(), new ArrayList<>());
        }
        // Loop over each scored word and add it to its associated list in the map
        for(T word : scoredWords) {
            scoredWordMap.get(word.getWord()).add(word);
        }
        return scoredWordMap;
    }


}
