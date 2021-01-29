package com.dtj503.offlinedevelopment;

import com.dtj503.offlinedevelopment.parsers.DocumentParser;
import com.dtj503.offlinedevelopment.types.Document;
import com.dtj503.offlinedevelopment.types.Sentence;
import com.dtj503.offlinedevelopment.types.Word;

public class OfflineDevelopment {

    public static void main(String[] args) {

        String testString = "Good morning, how are you doing today? It is very warm and sunny. That's hers.";

        Document doc = DocumentParser.parseText(testString);
        for(Sentence sentence : doc.getSentences()) {
            System.out.println(sentence);
            for(Word word : sentence.getWords()) {
                System.out.println(word);
            }
        }


    }

}
