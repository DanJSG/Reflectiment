package com.dtj503.offlinedevelopment.types;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Document {

    private String text;
    private List<Sentence> sentences;

    public Document(String text) throws Exception {
        this.text = text;
        this.sentences = extractSentences(text);
    }

    private List<Sentence> extractSentences(String text) throws Exception {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        CoreDocument annotatedText = new CoreDocument(text);
        pipeline.annotate(annotatedText);

        List<Sentence> sentences = new ArrayList<>(annotatedText.sentences().size());
        for(CoreSentence sentence : annotatedText.sentences()) {
            String sentenceString = sentence.text();
            List<String> tokens = sentence.tokensAsStrings();
            List<String> tags = sentence.posTags();
            sentences.add(new Sentence(sentenceString, tokens, tags));
        }
        return sentences;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    @Override
    public String toString() {
        return text;
    }

}
