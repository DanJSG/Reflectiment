package com.dtj503.lexicalanalyzer;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SentenceTests {

    @Test
    void createSentence() {
        String testString = "Good morning, how are you doing today?";
        StanfordCoreNLP pipeline = new StanfordCoreNLP();
        CoreDocument stringDoc = new CoreDocument(testString);
        pipeline.annotate(stringDoc);
        System.out.println(stringDoc.toString());
    }

}
