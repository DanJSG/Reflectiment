package com.dtj503.offlinedevelopment;

import com.dtj503.offlinedevelopment.types.Document;
import com.dtj503.offlinedevelopment.types.Sentence;
import com.dtj503.offlinedevelopment.types.Word;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.List;
import java.util.Properties;

public class OfflineDevelopment {

    public static void main(String[] args) {
        System.out.println("Test");
        String testString = "Good morning, how are you doing today? It is very warm and sunny.";


        try {
            Document doc = new Document(testString);
            List<Sentence> sentences = doc.getSentences();
            for(Sentence sentence : sentences) {
                System.out.println(sentence.toString());
                List<Word> words = sentence.getWords();
                for(Word word : words) {
                    System.out.println(word.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
