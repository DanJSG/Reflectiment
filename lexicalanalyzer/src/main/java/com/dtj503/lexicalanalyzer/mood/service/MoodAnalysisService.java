package com.dtj503.lexicalanalyzer.mood.service;

import com.dtj503.lexicalanalyzer.common.parsers.StringParser;
import com.dtj503.lexicalanalyzer.common.services.AnalysisService;
import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.sql.SQLTable;
import com.dtj503.lexicalanalyzer.common.types.*;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredWord;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredWordBuilder;

import java.util.List;

public class MoodAnalysisService extends AnalysisService {

    public static void analyseMood(String text) {
        // TODO remove debugging prints
        System.out.println("Text  arrived at the analyseMood(x) function.");
        System.out.println(text);

        Document doc = StringParser.parseText(text);

        for(Sentence sentence : doc.getSentences()) {

            List<Token> words = sentence.getWords();

            List<MoodScoredWord> moodScoredWords = fetchWordScores(words, SQLTable.MOOD, SQLColumn.WORD, new MoodScoredWordBuilder());
            List<ScoredWord> modifierScoredWords = fetchWordScores(words, SQLTable.SENTIMENT, SQLColumn.WORD, new ScoredWordBuilder());

            System.out.println(moodScoredWords);
            System.out.println(modifierScoredWords);

        }

    }

}
