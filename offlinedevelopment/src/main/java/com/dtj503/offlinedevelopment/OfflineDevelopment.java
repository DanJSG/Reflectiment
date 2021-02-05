package com.dtj503.offlinedevelopment;

import com.dtj503.offlinedevelopment.parsers.DocumentParser;
import com.dtj503.offlinedevelopment.parsers.ScoredWordParser;
import com.dtj503.offlinedevelopment.types.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class OfflineDevelopment {

    public static void main(String[] args) {

//        String testString = "Good morning, how are you doing today? It is very warm and sunny. That's hers.";
//
//        Document doc = DocumentParser.parseText(testString);
//        for(Sentence sentence : doc.getSentences()) {
//            System.out.println(sentence);
//            for(Word word : sentence.getWords()) {
//                System.out.println(word);
//            }
//        }

//        String testString = "I really really loved the movie!";
        String testString = "I really loved the documentary, it was very very interesting and extremely enjoyable.";
        Document doc = DocumentParser.parseText(testString);
        List<List<Token>> scoredWords = new ArrayList<>();
        scoredWords = new ArrayList<>();
        int count = 0;
        for(Sentence sentence : doc.getSentences()) {
            scoredWords.add(new ArrayList<>());
            System.out.println(sentence.getNounPositions());
            for(Token word : sentence.getWords()) {
                scoredWords.get(count).add(new ScoredWord(word.getWord(), word.getPartOfSpeech(), -1 + new Random().nextFloat() * (2)));
            }
            count++;
        }

        for(int i = 0; i < doc.getSentences().size(); i++) {
            Sentence scoredSentence = new Sentence(doc.getSentences().get(i).getOriginalText(), scoredWords.get(i));
            ScoredWordParser.parseScoredWords(scoredSentence);
        }

//        String text = "The work in team, from my point of view, is usually more complex than working alone because there are new parameters to consider";
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize,ssplit,pos");
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//        CoreDocument annotatedText = new CoreDocument(text);
//        pipeline.annotate(annotatedText);
//        for(CoreSentence sentence : annotatedText.sentences()) {
//            for(int i = 0; i < sentence.tokens().size(); i++) {
//                System.out.println(sentence.tokensAsStrings().get(i) + ": " + sentence.posTags().get(i));
//            }
//        }


    }

}
