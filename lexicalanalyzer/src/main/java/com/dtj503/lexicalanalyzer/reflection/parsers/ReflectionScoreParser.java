package com.dtj503.lexicalanalyzer.reflection.parsers;

import com.dtj503.lexicalanalyzer.common.parsers.ScoreParser;
import com.dtj503.lexicalanalyzer.common.types.ScoredWord;
import com.dtj503.lexicalanalyzer.common.types.Sentence;
import com.dtj503.lexicalanalyzer.common.utils.ListMath;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionCategory;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionScoredWord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class providing functionality to parse the reflection score from a collection of sentences containing scored words
 * and a sentence containing scored modifier words.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class ReflectionScoreParser extends ScoreParser {

    /**
     * Method for parsing the score of the a sentence for the different categories of reflection, making use of adverb
     * modifiers.
     * @param reflectionSentenceMap the collection of sentences for the different categories of reflection
     * @param modifierSentence the sentence with the modifier scores
     * @return a <code>Map</code> of the scores for each category
     */
    public static Map<String, Float> parseSentenceScore(
            Map<String, Sentence<ReflectionScoredWord>> reflectionSentenceMap, Sentence<ScoredWord> modifierSentence) {

        // Get the modifier scores and build the modification vector
        List<Float> modifierScores = modifierSentence.getScores();
        List<Float> modificationVector = createModificationVector(modifierSentence, modifierScores);

        Map<String, Float> scoreMap = new HashMap<>();
        for(ReflectionCategory category : ReflectionCategory.values()) {
            List<Float> currentScores = reflectionSentenceMap.get(category.toString()).getScores();
            List<Float> modifiedScores = stripZeroScores(ListMath.hadamardProduct(modificationVector, currentScores));
            float currentScore = Math.max(0, Math.min(ListMath.mean(modifiedScores), 1));
            scoreMap.put(category.toString(), currentScore);
        }

        return scoreMap;
    }

}
