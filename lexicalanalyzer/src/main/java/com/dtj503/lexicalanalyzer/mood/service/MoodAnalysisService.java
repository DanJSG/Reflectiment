package com.dtj503.lexicalanalyzer.mood.service;

import com.dtj503.lexicalanalyzer.libs.sql.MySQLRepository;
import com.dtj503.lexicalanalyzer.libs.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.libs.sql.SQLRepository;
import com.dtj503.lexicalanalyzer.libs.sql.SQLTable;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredWord;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredWordBuilder;
import com.dtj503.lexicalanalyzer.parsers.StringParser;
import com.dtj503.lexicalanalyzer.sentiment.service.SentimentAnalysisService;
import com.dtj503.lexicalanalyzer.sentiment.types.SentimentScoredWord;
import com.dtj503.lexicalanalyzer.sentiment.types.SentimentScoredWordBuilder;
import com.dtj503.lexicalanalyzer.types.Document;
import com.dtj503.lexicalanalyzer.types.Sentence;
import com.dtj503.lexicalanalyzer.types.Token;

import java.util.ArrayList;
import java.util.List;

public class MoodAnalysisService {

    public static void analyseMood(String text) {
        // TODO remove debugging prints
        System.out.println("Text  arrived at the analyseMood(x) function.");
        System.out.println(text);

        Document doc = StringParser.parseText(text);

        for(Sentence sentence : doc.getSentences()) {

            List<Token> words = sentence.getWords();

            List<MoodScoredWord> moodScoredWords = fetchMoodWordScores(words);
            List<SentimentScoredWord> modifierScoredWords = fetchModifierWordScores(words);

            System.out.println(moodScoredWords);
            System.out.println(modifierScoredWords);

        }

    }

    private static List<MoodScoredWord> fetchMoodWordScores(List<Token> words) {
        List<SQLColumn> cols = new ArrayList<>(words.size());
        List<String> wordStrings = new ArrayList<>(words.size());
        for(Token word : words) {
            wordStrings.add(word.getWord());
            cols.add(SQLColumn.WORD);
        }
        // Open the database connection and fetch the scores
        SQLRepository<MoodScoredWord> repo = new MySQLRepository<>(SQLTable.MOOD);
        return repo.findWhereEqualOr(cols, wordStrings, 0, new MoodScoredWordBuilder());
    }

    private static List<SentimentScoredWord> fetchModifierWordScores(List<Token> words) {
        List<SQLColumn> cols = new ArrayList<>(words.size());
        List<String> wordStrings = new ArrayList<>(words.size());
        for(Token word : words) {
            wordStrings.add(word.getWord());
            cols.add(SQLColumn.WORD);
        }
        // Open the database connection and fetch the scores
        SQLRepository<SentimentScoredWord> repo = new MySQLRepository<>(SQLTable.SENTIMENT);
        return repo.findWhereEqualOr(cols, wordStrings, 0, new SentimentScoredWordBuilder());
    }

}
