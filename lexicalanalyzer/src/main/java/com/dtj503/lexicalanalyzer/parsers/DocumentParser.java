package com.dtj503.lexicalanalyzer.parsers;

import com.dtj503.lexicalanalyzer.types.Document;
import com.dtj503.lexicalanalyzer.types.Sentence;
import com.dtj503.lexicalanalyzer.types.StopWords;
import com.dtj503.lexicalanalyzer.types.Word;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentParser {

    private static final Pattern NO_PUNCTUATION = Pattern.compile("^[0-9a-zA-Z_-]*$");

    public static Document parseText(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument annotatedText = new CoreDocument(text);
        pipeline.annotate(annotatedText);

        List<Sentence> sentences = parseSentences(annotatedText.sentences());

        return new Document(text, sentences);

    }

    private static List<Sentence> parseSentences(List<CoreSentence> coreSentences) {
        List<Sentence> sentences = new ArrayList<>(coreSentences.size());
        for(CoreSentence sentence : coreSentences) {
            List<Word> words = parseWords(sentence);
            sentences.add(new Sentence(sentence.text(), words));
        }
        return sentences;
    }

    private static List<Word> parseWords(CoreSentence sentence) {
        List<Word> words = new ArrayList<>(sentence.tokens().size());
        for(int i = 0; i < sentence.tokens().size(); i++) {
            String token = sentence.tokensAsStrings().get(i).toLowerCase();
            String posTag = PartOfSpeechReducer.simplifyPartOfSpeechTag(sentence.posTags().get(i));
            if(!isPunctuation(token) && !StopWords.isStopWord(token) && posTag != null) {
                words.add(new Word(token, posTag));
            }
        }
        return words;
    }

    private static boolean isPunctuation(String token) {
        Matcher matcher = NO_PUNCTUATION.matcher(token);
        return !matcher.find();
    }

}
