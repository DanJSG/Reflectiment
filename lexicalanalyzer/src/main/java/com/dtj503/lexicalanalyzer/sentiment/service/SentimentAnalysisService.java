package com.dtj503.lexicalanalyzer.sentiment.service;

import com.dtj503.lexicalanalyzer.common.services.AnalysisService;
import com.dtj503.lexicalanalyzer.common.sql.MySQLRepository;
import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.sql.SQLRepository;
import com.dtj503.lexicalanalyzer.common.sql.SQLTable;
import com.dtj503.lexicalanalyzer.common.parsers.StringParser;
import com.dtj503.lexicalanalyzer.sentiment.parsers.SentimentScoreParser;
import com.dtj503.lexicalanalyzer.sentiment.types.SentimentScoredSentence;
import com.dtj503.lexicalanalyzer.common.types.ScoredWordBuilder;
import com.dtj503.lexicalanalyzer.common.types.ScoredWord;
import com.dtj503.lexicalanalyzer.common.types.Document;
import com.dtj503.lexicalanalyzer.common.types.Sentence;
import com.dtj503.lexicalanalyzer.common.types.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        Document doc = StringParser.parseText(text);
        List<SentimentScoredSentence> scoredSentences = new ArrayList<>();
        // Loop over each sentence in the document
        for(Sentence sentence : doc.getSentences()) {
            // Get the words from the sentence
            List<Token> words = sentence.getWords();
            // Get the scores for each word
            List<ScoredWord> scoredWords = fetchWordScores(words);
            // Pick the correct score for each word
            scoredWords = pickScoredWord(words,  scoredWords);
            // Rebuild the sentence with the scored words in it
            Sentence sentenceWithScoredWords = new Sentence(sentence.getOriginalText(), scoredWords);
            // Parse the overall score of the sentence
            float score = SentimentScoreParser.parseSentenceScore(sentenceWithScoredWords);
            // Add a new scored sentence with the given score to the list ready to return
            scoredSentences.add(new SentimentScoredSentence(sentence.getOriginalText(), scoredWords, score));
        }
        return scoredSentences;
    }

    /**
     * Method for fetching the scores for each word within a list from the database.
     *
     * @param words the words to fetch the scores for
     * @return a list of scored word tokens containing the words and associated scores (may contain duplicate words
     *         where their are multiple scores for a word)
     */
    private static List<ScoredWord> fetchWordScores(List<Token> words) {
        List<SQLColumn> cols = new ArrayList<>(words.size());
        List<String> wordStrings = new ArrayList<>(words.size());
        for(Token word : words) {
            wordStrings.add(word.getWord());
            cols.add(SQLColumn.WORD);
        }
        // Open the database connection and fetch the scores
        SQLRepository<ScoredWord> repo = new MySQLRepository<>(SQLTable.SENTIMENT);
        return repo.findWhereEqualOr(cols, wordStrings, 0, new ScoredWordBuilder());
    }

    /**
     * Method for picking the desired scored word from the list of words - solves the problem of duplicates. This picks
     * words based on the word and the part of speech.
     *
     * @param words the words in the sentence
     * @param scoredWords the scored words containing potential duplicates
     * @return a list of scored words without duplicates
     */
    private static List<ScoredWord> pickScoredWord(List<Token> words, List<ScoredWord> scoredWords) {
        // Create a scored word map, where the key is the original word and the object is a list of associated scored
        // words
        Map<String, List<ScoredWord>> scoredWordMap = buildScoredWordMap(words, scoredWords);
        List<ScoredWord> updatedScoredWords = new ArrayList<>(words.size());
        // Loop over each token in the sentence
        for(Token word : words) {
            // If the word does not have an associated score, score it zero
            if (scoredWordMap.get(word.getWord()).size() == 0) {
                updatedScoredWords.add(new ScoredWord(word.getWord(), word.getPartOfSpeech()));
                continue;
            }
            int index = -1;
            float wordScoreSum = 0;
            int numWords = scoredWordMap.get(word.getWord()).size();
            // Loop through each scored version of the word, and if one is found with a matching PoS tag then store the
            // index
            for (int i = 0; i < numWords; i++) {
                if (scoredWordMap.get(word.getWord()).get(i).getPartOfSpeech().contentEquals(word.getPartOfSpeech())) {
                    index = i;
                    break;
                }
            }
            // If a word with a matching PoS is not found then take the mean of the matching words with different PoS
            // tags
            if (index == -1) {
                for (ScoredWord scoredWord : scoredWordMap.get(word.getWord())) {
                    wordScoreSum += scoredWord.getScore();
                }
                float meanScore = wordScoreSum / numWords;
                updatedScoredWords.add(new ScoredWord(word.getWord(), word.getPartOfSpeech(), meanScore));
            } else {
                updatedScoredWords.add(scoredWordMap.get(word.getWord()).get(index));
            }
        }
        return updatedScoredWords;
    }

}
