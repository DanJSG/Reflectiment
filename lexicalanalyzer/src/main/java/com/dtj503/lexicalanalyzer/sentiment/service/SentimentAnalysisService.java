package com.dtj503.lexicalanalyzer.sentiment.service;

import com.dtj503.lexicalanalyzer.libs.sql.MySQLRepository;
import com.dtj503.lexicalanalyzer.parsers.DocumentParser;
import com.dtj503.lexicalanalyzer.types.Document;
import com.dtj503.lexicalanalyzer.types.Sentence;
import com.dtj503.lexicalanalyzer.types.Word;

import java.util.List;

public class SentimentAnalysisService {

    public static float analyseSentiment(String text) {
        System.out.println(text);
        Document doc = DocumentParser.parseText(text);
        for(Sentence sentence : doc.getSentences()) {

            System.out.println(sentence);

            List<Word> words = sentence.getWords();



//            for(Word word : sentence.getWords()) {
//
//                System.out.println(word);
//
//            }
        }

        return 0;
    }

}
