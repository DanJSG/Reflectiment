package com.dtj503.lexicalanalyzer.sentiment.parsers;

import com.dtj503.lexicalanalyzer.types.Sentence;
import com.dtj503.lexicalanalyzer.types.Token;
import com.dtj503.lexicalanalyzer.utils.ListMath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for parsing the sentiment score of a sentence using a lexicon-based approach.
 * @author
 */
public class ScoreParser {

    /**
     * Method for parsing the scores of all the words in a sentence and calculating the overall sentiment polarity score
     * for the sentence.
     *
     * @param sentence the sentence to parse
     * @return a <code>float</code> between -1 and 1 (inclusive)
     */
    public static float parseSentenceScore(Sentence sentence) {
        //TODO remove debugging print statements
        for(Token word : sentence.getWords()) {
            System.out.println(word);
        }

        // Get the adjective, verb and adverb positions from the sentence
        List<Integer> adjectivePositions = sentence.getAdjectivePositions();
        List<Integer> verbPositions = sentence.getVerbPositions();
        List<Integer> adverbPositions = sentence.getAdverbPositions();

        // Get the word scores from the sentence
        List<Float> scores = sentence.getScores();

        //TODO remove debugging print statements
        System.out.println(scores);

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

        //TODO remove debugging print statements
        System.out.println("Trimmed scores: ");
        System.out.println(scores);
        System.out.println("Modification vector: ");
        System.out.println(modificationVector);
        System.out.println("Modified scores: ");
        System.out.println(modifiedScores);
        System.out.println("Final average: " + sentenceScore);

        return sentenceScore;

    }

    /**
     * Method for removing all zero values from a list, unless the list only contains 0 in which case none are removed.
     *
     * @param scores the scores to remove the zeros from
     * @return array with zeros removed, or original array if it is exclusively zeros
     */
    private static List<Float> stripZeroScores(List<Float> scores) {
        List<Float> newScores = new ArrayList<>();
        for(float score : scores) {
            if(score != 0f) {
                newScores.add(score);
            }
        }
        return newScores.size() > 0 ? newScores : scores;
    }

    /**
     * Method for finding modifiers and negators of specific words and updating a modification vector with these values.
     *
     * @param modificationVector the modification vector to update
     * @param modifiedPositions the indices of the modified words
     * @param adverbPositions the indices of the adverbs
     * @param scores the scores of the words in the sentence
     */
    private static void setModifiersAndNegators(List<Float> modificationVector, List<Integer> modifiedPositions,
                                                List<Integer> adverbPositions, List<Float> scores) {
        List<Float> verbNegators = getNegators(modifiedPositions, adverbPositions, scores);
        List<Float> verbModifiers = getModifiers(modifiedPositions, adverbPositions, scores);
        updateModificationVector(modificationVector, modifiedPositions, verbModifiers);
        updateModificationVector(modificationVector, modifiedPositions, verbNegators);
    }

    /**
     * Method for finding the indices of adverb negators from a sentence.
     *
     * @param positions the indices of the words being negated
     * @param adverbPositions the indices of the adverbs
     * @param scores the scores of the words in the sentence
     * @return list of 1s and -1s representing negation, in <code>float</code> format
     */
    private static List<Float> getNegators(List<Integer> positions, List<Integer> adverbPositions, List<Float> scores) {
        List<Float> negators = new ArrayList<>();
        for(int pos : positions) {
            float currentNegator = 1;
            int prevPos = pos - 1;
            // Loop backwards from the modified word to find all potential negators
            while(prevPos >= 0 && adverbPositions.contains(prevPos)) {
                // If the word has a negative polarity, flip the sign
                if(scores.get(prevPos) < 0) {
                    currentNegator *= -1;
                }
                prevPos = prevPos - 1;
            }
            negators.add(currentNegator);
        }
        return negators;
    }

    /**
     * Method for finding the indices of adverb modifiers from a sentence.
     *
     * @param positions the indices of the words being modified
     * @param adverbPositions the indices of the adverbs
     * @param scores the scores of the words in the sentence
     * @return list of modifiers, each with a value between 0 and 2, in <code>float</code> format
     */
    private static List<Float> getModifiers(List<Integer> positions, List<Integer> adverbPositions,
                                            List<Float> scores) {
        List<Float> modifiers = new ArrayList<>();
        for(int pos : positions) {
            List<Float> currentModifiers = new ArrayList<>();
            int prevPos = pos - 1;
            // Loop backwards from the modified word to find all potential modifiers
            while(prevPos >= 0 && adverbPositions.contains(prevPos)) {
                // Take the absolute value of the modifiers score
                // TODO tweak during analysis to see impact on effectiveness
                currentModifiers.add(Math.abs(scores.get(prevPos)));
                prevPos = prevPos - 1;
            }
            // If no modifiers are found in front of the word, search for them after the word
            if(currentModifiers.size() == 0) {
                prevPos = pos + 1;
                // Loop forward from the modified word to find all potential modifiers
                while(prevPos < scores.size() && adverbPositions.contains(prevPos)) {
                    // Take the absolute value of the modifiers score
                    // TODO tweak during analysis to see impact on effectiveness
                    currentModifiers.add(Math.abs(scores.get(prevPos)));
                    prevPos = prevPos + 1;
                }
            }
            // Add the modifiers to the list, or just add 1 if there are no modifiers
            if(currentModifiers.size() > 0) {
                // TODO tweak this during analysis to see impact on effectiveness
                modifiers.add(1 + ListMath.mean(currentModifiers));
            } else {
                modifiers.add(1f);
            }
        }
        return modifiers;
    }

    /**
     * Method for removing all adverb positions from a set of scores.
     *
     * @param list the list of scores to remove the adverb positions from
     * @param adverbPositions the list of adverbs
     * @return an updated list of scores with the adverb positions removed
     */
    private static List<Float> removeAdverbs(List<Float> list, List<Integer> adverbPositions) {
        List<Float> newList = new ArrayList<>(list.size() - adverbPositions.size());
        for(int i = 0; i < list.size(); i++) {
            if(adverbPositions.contains(i)) {
                continue;
            }
            newList.add(list.get(i));
        }
        return newList;
    }

    /**
     * Method to update the modification vector with a set of new modifiers.
     *
     * @param modificationVector the modification vector to change - this is changed directly rather than returning
     * @param positions the positions of the modifiers
     * @param modifiers the values of the modifiers
     */
    private static void updateModificationVector(List<Float> modificationVector, List<Integer> positions,
                                                 List<Float> modifiers) {
        // Loop over each modifier position
        for(int i = 0; i < positions.size(); i++) {
            int position = positions.get(i);
            float modifier = modifiers.get(i);
            // Update cumulatively for each position - don't just replace, multiply
            modificationVector.set(position, modificationVector.get(position) * modifier);
        }
    }

}
