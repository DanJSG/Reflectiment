package com.dtj503.lexicalanalyzer.sentiment.service;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class SentimentAnalysisService {

    public static float analyseSentiment(String text) {
        System.out.println(text);

        String testString = "Good morning, how are you doing today?";
        StanfordCoreNLP pipeline = new StanfordCoreNLP();
        CoreDocument stringDoc = new CoreDocument(testString);
        pipeline.annotate(stringDoc);

        return 0;
    }

}
