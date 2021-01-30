package com.dtj503.lexicalanalyzer.sentiment.service;

import com.dtj503.lexicalanalyzer.libs.sql.MySQLRepository;
import com.dtj503.lexicalanalyzer.libs.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.libs.sql.SQLRepository;
import com.dtj503.lexicalanalyzer.libs.sql.SQLTable;
import com.dtj503.lexicalanalyzer.parsers.DocumentParser;
import com.dtj503.lexicalanalyzer.sentiment.types.ScoredWordBuilder;
import com.dtj503.lexicalanalyzer.sentiment.types.ScoredWord;
import com.dtj503.lexicalanalyzer.types.Document;
import com.dtj503.lexicalanalyzer.types.Sentence;
import com.dtj503.lexicalanalyzer.types.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentimentAnalysisService {

    public static float analyseSentiment(String text) {
        System.out.println(text);
        Document doc = DocumentParser.parseText(text);
        for(Sentence sentence : doc.getSentences()) {

            System.out.println(sentence);

            List<Word> words = sentence.getWords();

            List<ScoredWord> scoredWords = fetchWordScores(words);
            scoredWords = pickScoredWord(words,  scoredWords);

            float sum = 0;
            for(int i = 0; i < scoredWords.size(); i++) {
                System.out.println(scoredWords.get(i));
                sum += scoredWords.get(i).getScore();
            }
            System.out.println("Sentence sum:" + sum);
            System.out.println("Sentence average: " + (sum / scoredWords.size()));

        }

        return 0;
    }

    private static List<ScoredWord> fetchWordScores(List<Word> words) {
        List<SQLColumn> cols = new ArrayList<>(words.size());
        List<String> wordStrings = new ArrayList<>(words.size());

        for(Word word : words) {
            wordStrings.add(word.getWord());
            cols.add(SQLColumn.WORD);
        }

        SQLRepository<ScoredWord> repo = new MySQLRepository<>(SQLTable.SENTIMENT);
        return repo.findWhereEqualOr(cols, wordStrings, 0, new ScoredWordBuilder());
    }

    private static List<ScoredWord> pickScoredWord(List<Word> words, List<ScoredWord> scoredWords) {
        Map<String, List<ScoredWord>> scoredWordMap = buildScoredWordMap(words, scoredWords);
        List<ScoredWord> updatedScoredWords = new ArrayList<>(words.size());
        for(Word word : words) {
            if (scoredWordMap.get(word.getWord()).size() == 0) {
                updatedScoredWords.add(new ScoredWord(word.getWord(), word.getPartOfSpeech()));
                continue;
            }
            int index = -1;
            float wordScoreSum = 0;
            int numWords = scoredWordMap.get(word.getWord()).size();
            for (int i = 0; i < numWords; i++) {
                if (scoredWordMap.get(word.getWord()).get(i).getPartOfSpeech().contentEquals(word.getPartOfSpeech())) {
                    index = i;
                    break;
                }
            }
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

    private static Map<String, List<ScoredWord>> buildScoredWordMap(List<Word> words, List<ScoredWord> scoredWords) {
        Map<String, List<ScoredWord>> scoredWordMap = new HashMap<>();
        for(Word word : words) {
            scoredWordMap.put(word.getWord(), new ArrayList<>());
        }
        for(ScoredWord word : scoredWords) {
            scoredWordMap.get(word.getWord()).add(word);
        }
        return scoredWordMap;
    }

}
