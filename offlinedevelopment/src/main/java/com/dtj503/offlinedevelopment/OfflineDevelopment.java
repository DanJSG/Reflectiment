package com.dtj503.offlinedevelopment;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

public class OfflineDevelopment {

    public static void main(String[] args) {
        System.out.println("Test");
        String testString = "Good morning, how are you doing today? It is very warm and sunny.";


        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument stringDoc = new CoreDocument(testString);
        pipeline.annotate(stringDoc);
        for(CoreSentence sentence : stringDoc.sentences()) {
            System.out.println(sentence.tokensAsStrings());
            System.out.println(sentence.posTags());
        }

    }

}
