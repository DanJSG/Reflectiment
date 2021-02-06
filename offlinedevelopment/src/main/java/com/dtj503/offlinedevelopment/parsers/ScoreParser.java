package com.dtj503.offlinedevelopment.parsers;

import com.dtj503.offlinedevelopment.types.Sentence;
import com.dtj503.offlinedevelopment.types.Token;
import com.dtj503.offlinedevelopment.utils.ListMath;

import java.util.*;

public class ScoreParser {

    public static void parseSentenceScore(Sentence sentence) {
        for(Token word : sentence.getWords()) {
            System.out.println(word);
        }

        List<Integer> adjectivePositions = sentence.getAdjectivePositions();
        List<Integer> verbPositions = sentence.getVerbPositions();
        List<Integer> adverbPositions = sentence.getAdverbPositions();
        List<Float> scores = sentence.getScores();

        System.out.println(scores);

        List<Float> modificationVector = new ArrayList<>(Collections.nCopies(scores.size(), 1f));

        // TODO refactor
        if(adjectivePositions != null && adjectivePositions.size() > 0) {
            setModifiersAndNegators(modificationVector, adjectivePositions, adverbPositions, scores);
        }

        // TODO refactor
        if(verbPositions != null && verbPositions.size() > 0) {
            setModifiersAndNegators(modificationVector, verbPositions, adverbPositions, scores);
        }

        scores = removeAdverbs(scores, adverbPositions);
        modificationVector = removeAdverbs(modificationVector, adverbPositions);

        List<Float> modifiedScores = ListMath.hadamardProduct(modificationVector, scores);

        float sentenceScore = Math.max(-1, Math.min(ListMath.mean(modifiedScores), 1));
        
        System.out.println("Trimmed scores: ");
        System.out.println(scores);
        System.out.println("Modification vector: ");
        System.out.println(modificationVector);
        System.out.println("Modified scores: ");
        System.out.println(modifiedScores);
        System.out.println("Final average: " + sentenceScore);

    }

    private static void setModifiersAndNegators(List<Float> modificationVector, List<Integer> modifiedPositions,
                                                List<Integer> adverbPositions, List<Float> scores) {
        List<Float> verbNegators = getNegators(modifiedPositions, adverbPositions, scores);
        List<Float> verbModifiers = getModifiers(modifiedPositions, adverbPositions, scores);
        updateModificationVector(modificationVector, modifiedPositions, verbModifiers);
        updateModificationVector(modificationVector, modifiedPositions, verbNegators);
    }

    private static List<Float> getNegators(List<Integer> positions, List<Integer> adverbPositions, List<Float> scores) {
        List<Float> negators = new ArrayList<>();
        for(int pos : positions) {
            float currentNegator = 1;
            int prevPos = pos - 1;
            while(prevPos >= 0 && adverbPositions.contains(prevPos)) {
                if(scores.get(prevPos) < 0) {
                    currentNegator *= -1;
                }
                prevPos = prevPos - 1;
            }
            negators.add(currentNegator);
        }
        return negators;
    }

    private static List<Float> getModifiers(List<Integer> positions, List<Integer> adverbPositions,
                                            List<Float> scores) {
        List<Float> modifiers = new ArrayList<>();
        for(int pos : positions) {
            List<Float> currentModifiers = new ArrayList<>();
            int prevPos = pos - 1;
            while(prevPos >= 0 && adverbPositions.contains(prevPos)) {
                currentModifiers.add(Math.abs(scores.get(prevPos)));
                prevPos = prevPos - 1;
            }
            if(currentModifiers.size() == 0) {
                prevPos = pos + 1;
                while(prevPos < scores.size() && adverbPositions.contains(prevPos)) {
                    currentModifiers.add(Math.abs(scores.get(prevPos)));
                    prevPos = prevPos + 1;
                }
            }
            if(currentModifiers.size() > 0) {
                // TODO also tweak this during analysis to see impact on effectiveness
                modifiers.add(1 + ListMath.mean(currentModifiers));
            } else {
                modifiers.add(1f);
            }
        }
        return modifiers;
    }

    private static List<Float> removeAdverbs(List<Float> list, List<Integer> positions) {
        List<Float> newList = new ArrayList<>(list.size() - positions.size());
        for(int i = 0; i < list.size(); i++) {
            if(positions.contains(i)) {
                continue;
            }
            newList.add(list.get(i));
        }
        return newList;
    }

    private static void updateModificationVector(List<Float> modificationVector, List<Integer> positions,
                                                 List<Float> modifiers) {
        for(int i = 0; i < positions.size(); i++) {
            int position = positions.get(i);
            float modifier = modifiers.get(i);
            modificationVector.set(position, modificationVector.get(position) * modifier);
        }
    }

}
