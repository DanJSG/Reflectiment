package com.dtj503.lexicalanalyzer;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LexicalAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LexicalAnalyzerApplication.class, args);
        System.out.println("Lexical Analyzer microservice running...");
    }

}
