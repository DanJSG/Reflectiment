package com.dtj503.lexicalanalyzer.sentiment.parsers;

import com.dtj503.lexicalanalyzer.common.parsers.ScoreParser;
import com.dtj503.lexicalanalyzer.common.types.ScoredWord;
import com.dtj503.lexicalanalyzer.common.types.Sentence;
import com.dtj503.lexicalanalyzer.common.utils.ListMath;

import java.util.List;

/**
 * Class for parsing the sentiment score of a sentence using a lexicon-based approach.
 * @author Dan Jackson (dtj503@york.ac.uk)
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

        // Get the word scores from the sentence
        List<Float> scores = sentence.getScores();

        // Generate the modification vector
        List<Float> modificationVector = createModificationVector(sentence, scores);

        // Get the adverb positions from the sentence
        List<Integer> adverbPositions = sentence.getAdverbPositions();

        // Remove the adverb scores from the sentence
        if(adverbPositions != null) {
            scores = removeAdverbs(scores, adverbPositions);
            modificationVector = removeAdverbs(modificationVector, adverbPositions);
        }

        if(scores.size() == 0) {
            return 0;
        }

        // Calculate the hadamard product of the scores with the modification vector (piecewise multiplication)
        List<Float> modifiedScores = ListMath.hadamardProduct(modificationVector, scores);

        // Remove zero valued scores (unless they are all zero valued)
        modifiedScores = stripZeroScores(modifiedScores);

        // Calculate the overall sentence score by taking the mean of the scores and limiting between -1 and 1
        return Math.max(-1, Math.min(ListMath.mean(modifiedScores), 1));

    }

}
