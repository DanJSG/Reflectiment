package com.dtj503.lexicalanalyzer.common.parsers;

import com.dtj503.lexicalanalyzer.common.pools.PipelineType;
import com.dtj503.lexicalanalyzer.common.types.Document;
import com.dtj503.lexicalanalyzer.common.types.Sentence;
import com.dtj503.lexicalanalyzer.common.types.Token;
import com.dtj503.lexicalanalyzer.common.types.Word;
import com.dtj503.lexicalanalyzer.common.pools.PipelinePool;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import javax.annotation.processing.SupportedSourceVersion;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class containing static methods for parsing a string and generating a Document object made up of multiple sentences
 * which are composed of Part of Speech (PoS) tagged word tokens.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class StringParser {

    // Regex pattern for matching everything except punctuation
    private static final Pattern NO_PUNCTUATION = Pattern.compile("^[0-9a-zA-Z_-]*$");

    /**
     * Method for parsing a string of text and converting it into a document object.
     *
     * @param text the text to parse
     * @return the document object
     */
    public static <T extends Token> Document<T> parseText(String text, boolean includeEntityTags) {

        // Fetch a CoreNLP pipeline
        StanfordCoreNLP pipeline =
                includeEntityTags ? PipelinePool.get(PipelineType.FULL) : PipelinePool.get(PipelineType.FAST);

        // Annotate the input text
        CoreDocument annotatedText = new CoreDocument(text);
        pipeline.annotate(annotatedText);

        // Parse the text and extract sentences
        List<Sentence<Token>> sentences = parseSentences(annotatedText.sentences(), includeEntityTags);

        // Generate a document from these sentences
        return new Document(text, sentences);

    }

    /**
     * Method for parsing sentences into PoS tagged tokenized sentences.
     *
     * @param coreSentences list of CoreNLP sentences
     * @return list of tagged sentences
     */
    private static List<Sentence<Token>> parseSentences(List<CoreSentence> coreSentences, boolean includeEntityTags) {
        List<Sentence<Token>> sentences = new ArrayList<>(coreSentences.size());
        // Loop through each sentence
        for(CoreSentence sentence : coreSentences) {

            List<String> sentenceSubjects = null;
            if(includeEntityTags) {
                sentenceSubjects = getEntityTypes(sentence);
            }

            // Parse the words in each sentence and add them to output list
            List<Token> words = parseWords(sentence);

            if(sentenceSubjects == null) {
                sentences.add(new Sentence<Token>(sentence.text(), words));
            } else {
                sentences.add(new Sentence<Token>(sentence.text(), words, sentenceSubjects));
            }


        }
        return sentences;
    }

    private static List<String> getEntityTypes(CoreSentence sentence) {
        List<String> sentenceSubjects = new ArrayList<>();
        for(CoreEntityMention entity : sentence.entityMentions()) {
            System.out.println(entity + ": " + entity.entityType());
            sentenceSubjects.add(entity.entityType());
        }
        return sentenceSubjects;
//        return sentenceSubjects.size() != 0 ? sentenceSubjects : null;
    }

    /**
     * Method for tokenizing sentences and tagging the words with the PoS. Converts words to lower case, removes
     * punctuation and removes all words which are not a noun, verb, adverb or adjective.
     *
     * @param sentence the input sentence
     * @return a list of tagged words
     */
    private static List<Token> parseWords(CoreSentence sentence) {
        List<Token> words = new ArrayList<>(sentence.tokens().size());
        for(int i = 0; i < sentence.tokens().size(); i++) {
            String token = sentence.tokensAsStrings().get(i).toLowerCase();
            String posTag = PartOfSpeechReducer.simplifyPartOfSpeechTag(sentence.posTags().get(i));
            if(!isPunctuation(token) && posTag != null) {
                words.add(new Word(token, posTag));
            }
        }
        return words;
    }

    /**
     * Method to check if a string is punctuation or not.
     *
     * @param token the word to check
     * @return <code>true</code> if the word is punctuation, <code>false</code> otherwise
     */
    private static boolean isPunctuation(String token) {
        Matcher matcher = NO_PUNCTUATION.matcher(token);
        return !matcher.find();
    }

}
