package com.dtj503.lexicalanalyzer.common.parsers;

import com.dtj503.lexicalanalyzer.common.utils.ListMath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract class implementing shared methods in score parsers.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public abstract class ScoreParser {

    /**
     * Method for creating a vector of values used to modify the scores of words. These vectors are based on adverb
     * positions and their scores.
     *
     * @param adjectivePositions the indices of the adjectives
     * @param verbPositions the indices of the verbs
     * @param adverbPositions the indices of the adverbs
     * @param scores the scores of the words in the sentence
     * @return returns a <code>List</code> of <code>Float</code>s representing the modifying multipliers
     */
    protected static List<Float> createModificationVector(List<Integer> adjectivePositions, List<Integer> verbPositions,
                                                          List<Integer> adverbPositions, List<Float> scores) {
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

        return modificationVector;
    }

    /**
     * Method for removing all zero values from a list, unless the list only contains 0 in which case none are removed.
     *
     * @param scores the scores to remove the zeros from
     * @return array with zeros removed, or original array if it is exclusively zeros
     */
    protected static List<Float> stripZeroScores(List<Float> scores) {
        List<Float> newScores = new ArrayList<>();
        for(float score : scores) {
            if(score != 0f) {
                newScores.add(score);
            }
        }
        return newScores.size() > 0 ? newScores : scores;
    }

    /**
     * Method for removing all adverb positions from a set of scores.
     *
     * @param list the list of scores to remove the adverb positions from
     * @param adverbPositions the list of adverbs
     * @return an updated list of scores with the adverb positions removed
     */
    protected static List<Float> removeAdverbs(List<Float> list, List<Integer> adverbPositions) {
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
     * Method for finding modifiers and negators of specific words and updating a modification vector with these values.
     *
     * @param modificationVector the modification vector to update
     * @param modifiedPositions the indices of the modified words
     * @param adverbPositions the indices of the adverbs
     * @param scores the scores of the words in the sentence
     */
    private static void setModifiersAndNegators(List<Float> modificationVector, List<Integer> modifiedPositions,
                                                List<Integer> adverbPositions, List<Float> scores) {
        List<Float> negators = getNegators(modifiedPositions, adverbPositions, scores);
        List<Float> modifiers = getModifiers(modifiedPositions, adverbPositions, scores);
        updateModificationVector(modificationVector, modifiedPositions, modifiers);
        updateModificationVector(modificationVector, modifiedPositions, negators);
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
