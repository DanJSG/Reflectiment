package com.dtj503.lexicalanalyzer.reflection.service;

import com.dtj503.lexicalanalyzer.common.parsers.StringParser;
import com.dtj503.lexicalanalyzer.common.services.AnalysisService;
import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.sql.SQLTable;
import com.dtj503.lexicalanalyzer.common.types.Document;
import com.dtj503.lexicalanalyzer.common.types.Sentence;
import com.dtj503.lexicalanalyzer.common.types.Token;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionScoredWord;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionScoredWordBuilder;

import java.util.List;

public class ReflectionAnalysisService extends AnalysisService {

    public static void analyseReflection(String text) {
        System.out.println("In reflection analysis service!");
        System.out.println(text);

        Document<Token> doc = StringParser.parseText(text);

        for(Sentence<Token> sentence : doc.getSentences()) {
            List<Token> words = sentence.getWords();
            List<ReflectionScoredWord> reflectionScoredWords = fetchWordScores(words, SQLTable.REFLECTION,
                                                                               SQLColumn.WORD,
                                                                               new ReflectionScoredWordBuilder());
            System.out.println(reflectionScoredWords);
        }

    }

}
