package com.dtj503.offlinedevelopment.parsers;

import com.dtj503.offlinedevelopment.types.ScoredWord;
import com.dtj503.offlinedevelopment.types.Sentence;
import com.dtj503.offlinedevelopment.types.Token;
import com.dtj503.offlinedevelopment.utils.Averages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoredWordParser {

    public static void parseScoredWords(Sentence sentence) {
        for(Token word : sentence.getWords()) {
            System.out.println(word);
        }

        List<Integer> adjectivePositions = sentence.getAdjectivePositions();
        List<Integer> verbPositions = sentence.getVerbPositions();
        List<Integer> adverbPositions = sentence.getAdverbPositions();
        List<Float> scores = sentence.getScores();

        System.out.println(scores);

        List<List<Float>> adjectiveModifierList = getModifiers(adjectivePositions, adverbPositions, scores);
        List<List<Float>> verbModifierList = getModifiers(verbPositions, adverbPositions, scores);

        List<Float> adjectiveAverageModifiers = new ArrayList<>(adjectiveModifierList.size());
        List<Float> verbAverageModifiers = new ArrayList<>(verbModifierList.size());

        System.out.println("Adjective modifiers: ");
        for(List<Float> adjectiveModifers : adjectiveModifierList) {
            System.out.println(adjectiveModifers);
        }

        System.out.println("Verb modifiers: ");
        for(List<Float> verbModifiers : verbModifierList) {
            System.out.println(verbModifiers);
        }

    }

    private static List<List<Float>> getModifiers(List<Integer> positions, List<Integer> adverbPositions, List<Float> scores) {
        List<List<Float>> modifiers = new ArrayList<>();
        for(int pos : positions) {
            List<Float> currentModifiers = new ArrayList<>();
            int prevPos = pos - 1;
            while(prevPos >= 0 && adverbPositions.contains(prevPos)) {
                currentModifiers.add(1 + scores.get(prevPos));
                prevPos = prevPos - 1;
            }
            if(currentModifiers.size() > 0) {
                modifiers.add(currentModifiers);
            } else {
                modifiers.add(null);
            }
        }
        return modifiers;
    }


}
