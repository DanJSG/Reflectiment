package com.dtj503.lexicalanalyzer.sentiment.parsers;

import com.dtj503.lexicalanalyzer.common.parsers.ScoreParser;
import com.dtj503.lexicalanalyzer.common.types.ScoredWord;
import com.dtj503.lexicalanalyzer.common.types.Sentence;
import com.dtj503.lexicalanalyzer.common.utils.ListMath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for parsing the sentiment score of a sentence using a lexicon-based approach.
 * @author
 */
public class SentimentScoreParser extends ScoreParser {

    /**
     * Method for parsing the scores of all the words in a sentence and calculating the overall sentiment polarity score
     * for the sentence.
     *
     * @param sentence the sentence to parse
     * @return a <code>float</code> between -1 and 1 (inclusive)
     */
    public static float parseSentenceScore(Sentence<ScoredWord> sentence) {

        // Get the adjective, verb and adverb positions from the sentence
        List<Integer> adjectivePositions = sentence.getAdjectivePositions();
        List<Integer> verbPositions = sentence.getVerbPositions();
        List<Integer> adverbPositions = sentence.getAdverbPositions();

        // Get the word scores from the sentence
        List<Float> scores = sentence.getScores();

        // Initialise a list of ones ready to use as a multiplication vector
        List<Float> modificationVector = new ArrayList<>(Collections.nCopies(scores.size(), 1f));

        // If there are adjectives and adverbs in the sentence, then find the adverbs which modify and/or negate the
        // adjectives and update the modification vector with the modification and negation values
        if(adjectivePositions != null && adjectivePositions.size() > 0 && adverbPositions != null) {
            setModifiersAndNegators(modificationVector, adjectivePositions, adverbPositions, scores);
        }

        // If there are verbs and adverbs in the sentence, then find the adverbs which modify and/or negate the
        // verbs and update the modification vector with the modification and negation values
        if(verbPositions != null && verbPositions.size() > 0 && adverbPositions != null) {
            setModifiersAndNegators(modificationVector, verbPositions, adverbPositions, scores);
        }

        // Remove the adverb scores from the sentence
        if(adverbPositions != null) {
            scores = removeAdverbs(scores, adverbPositions);
            modificationVector = removeAdverbs(modificationVector, adverbPositions);
        }

        // Calculate the hadamard product of the scores with the modification vector (piecewise multiplication)
        List<Float> modifiedScores = ListMath.hadamardProduct(modificationVector, scores);

        // Remove zero valued scores (unless they are all zero valued)
        modifiedScores = stripZeroScores(modifiedScores);

        // Calculate the overall sentence score by taking the mean of the scores and limiting between -1 and 1
        float sentenceScore = Math.max(-1, Math.min(ListMath.mean(modifiedScores), 1));

        return sentenceScore;

    }

}
