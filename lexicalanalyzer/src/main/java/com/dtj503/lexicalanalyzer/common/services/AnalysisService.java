package com.dtj503.lexicalanalyzer.common.services;

import com.dtj503.lexicalanalyzer.common.sql.*;
import com.dtj503.lexicalanalyzer.common.types.ScoredWord;
import com.dtj503.lexicalanalyzer.common.types.Token;
import com.dtj503.lexicalanalyzer.common.types.Word;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredWord;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionScoredSentence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for an analysis service containing shared methods for sentiment, mood and reflection analysis.
 */
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

    // TODO sort this out so that this actually works -- need to fix the SQL repo method findWhereEqualOr
    //      from old method: return repo.findWhereEqualOr(cols, wordStrings, 0, builder);
    protected static <V extends SQLEntityBuilder<T>, T extends U, U extends Token> List<T> fetchWordScores(List<U> words, SQLTable table, V builder) {
        return fetchWordScores(words, table, null, builder);
    }

    /**
     * Method for fetching the scores for each word within a list from the database.
     *
     * @param <U> the input word type, must inherit from <code>token</code>
     * @param <T> the output word type, must inherit from <code>U</code>
     * @param <V> the builder for the fetched objects, must inherit from <code>SQLEntityBuilder</code>
     * @param words the words to fetch the scores for
     * @param table the SQL database table the scores are in
     * @param tag the tag the words have in the database table
     * @param builder the entity builder
     * @return a list of scored word tokens containing the words and associated scores (may contain duplicate words
     *         where there are multiple scores for a word)
     */
    protected static <V extends SQLEntityBuilder<T>, T extends U, U extends Token> List<T> fetchWordScores(List<U> words, SQLTable table, String tag, V builder) {
        List<String> wordStrings = new ArrayList<>(words.size());
        List<String> tagList = new ArrayList<>(words.size());
        for(U word : words) {
            wordStrings.add(word.getWord());
            tagList.add(tag);
        }
        SQLRepository<T> repo = new MySQLRepository<>(table);
        return repo.findWhereEqualAndOr(SQLColumn.WORD, SQLColumn.TAG, wordStrings, tagList, 0, builder);
    }

    /**
     * Method for picking the desired scored word from the list of words - solves the problem of duplicates. This picks
     * words based on the word and the part of speech.
     *
     * @param words the words in the sentence
     * @param scoredWords the scored words containing potential duplicates
     * @return a list of scored words without duplicates
     */
    protected static <T extends ScoredWord> List<ScoredWord> pickScoredWord(List<Token> words, List<T> scoredWords) {
        // Create a scored word map, where the key is the original word and the object is a list of associated scored
        // words
        Map<String, List<T>> scoredWordMap = buildScoredWordMap(words, scoredWords);
        List<ScoredWord> updatedScoredWords = new ArrayList<>(words.size());
        // Loop over each token in the sentence
        for(Token word : words) {
            // If the word does not have an associated score, score it zero
            if (scoredWordMap.get(word.getWord()).size() == 0) {
                updatedScoredWords.add(new ScoredWord(word.getWord(), word.getPartOfSpeech()));
                continue;
            }
            int index = -1;
            float wordScoreSum = 0;
            int numWords = scoredWordMap.get(word.getWord()).size();
            // Loop through each scored version of the word, and if one is found with a matching PoS tag then store the
            // index
            for (int i = 0; i < numWords; i++) {
                if (scoredWordMap.get(word.getWord()).get(i).getPartOfSpeech().contentEquals(word.getPartOfSpeech())) {
                    index = i;
                    break;
                }
            }
            // If a word with a matching PoS is not found then take the mean of the matching words with different PoS
            // tags
            if (index == -1) {
                for (ScoredWord scoredWord : scoredWordMap.get(word.getWord())) {
                    wordScoreSum += scoredWord.getScore();
                }
                float meanScore = wordScoreSum / numWords;
                updatedScoredWords.add(new ScoredWord(word.getWord(), word.getPartOfSpeech(), meanScore));
            } else {
                updatedScoredWords.add(scoredWordMap.get(word.getWord()).get(index));
            }
        }
        return updatedScoredWords;
    }

}
