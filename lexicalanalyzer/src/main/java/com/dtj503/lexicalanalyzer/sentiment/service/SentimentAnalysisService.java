package com.dtj503.lexicalanalyzer.sentiment.service;

import com.dtj503.lexicalanalyzer.common.parsers.StringParser;
import com.dtj503.lexicalanalyzer.common.services.AnalysisService;
import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.sql.SQLTable;
import com.dtj503.lexicalanalyzer.common.types.*;
import com.dtj503.lexicalanalyzer.sentiment.parsers.SentimentScoreParser;
import com.dtj503.lexicalanalyzer.sentiment.types.SentimentScoredSentence;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for analysing sentiment from a submitted string of text.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class SentimentAnalysisService extends AnalysisService {

    /**
     * Method for analysing the sentiment from a string of text.
     *
     * @param text the string of text
     * @return
     */
    public static List<SentimentScoredSentence> analyseSentiment(String text) {
        // Parse the string of text into a document, split into sentences and words
        Document<Token> doc = StringParser.parseText(text);
        List<SentimentScoredSentence> scoredSentences = new ArrayList<>();
        // Loop over each sentence in the document
        for(Sentence<Token> sentence : doc.getSentences()) {
            // Get the words from the sentence
            List<Token> words = sentence.getWords();
            // Get the scores for each word
            List<ScoredWord> scoredWords = fetchWordScores(words, SQLTable.SENTIMENT, SQLColumn.WORD, new ScoredWordBuilder());
            // If there are no associated scores for any of the words, then score the sentence 0
            if(scoredWords == null) {
                scoredSentences.add(new SentimentScoredSentence(sentence.getOriginalText(), null, 0));
                continue;
            }
            // Pick the correct score for each word
            scoredWords = pickScoredWord(words,  scoredWords);
            // Rebuild the sentence with the scored words in it
            Sentence<ScoredWord> sentenceWithScoredWords = new Sentence<>(sentence.getOriginalText(), scoredWords);
            // Parse the overall score of the sentence
            float score = SentimentScoreParser.parseSentenceScore(sentenceWithScoredWords);
            // Add a new scored sentence with the given score to the list ready to return
            scoredSentences.add(new SentimentScoredSentence(sentence.getOriginalText(), scoredWords, score));
        }
        return scoredSentences;
    }

}
