package com.dtj503.lexicalanalyzer.mood.parsers;

import com.dtj503.lexicalanalyzer.common.parsers.ScoreParser;
import com.dtj503.lexicalanalyzer.common.types.ScoredWord;
import com.dtj503.lexicalanalyzer.common.types.Sentence;
import com.dtj503.lexicalanalyzer.common.utils.ListMath;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredWord;

import java.util.*;

public class MoodScoreParser extends ScoreParser {

    public static Map<String, Float> parseSentenceScore(Map<String, Sentence<MoodScoredWord>> moodSentenceMap,
                                          Sentence<ScoredWord> modifierSentence) {
        System.out.println("Fear: ");
        System.out.println(moodSentenceMap.get("fear"));
        System.out.println("Anger: ");
        System.out.println(moodSentenceMap.get("anger"));
        System.out.println("Sadness: ");
        System.out.println(moodSentenceMap.get("sadness"));
        System.out.println("Joy: ");
        System.out.println(moodSentenceMap.get("joy"));
        System.out.println("Modifiers: ");
        System.out.println(modifierSentence.getOriginalText());

        List<Float> fearScores = moodSentenceMap.get("fear").getScores();
        List<Float> angerScores = moodSentenceMap.get("anger").getScores();
        List<Float> sadnessScores = moodSentenceMap.get("sadness").getScores();
        List<Float> joyScores = moodSentenceMap.get("joy").getScores();

        // TODO refactor this stuff into a shared method in the score parser abstract class?
        List<Integer> adjectivePositions = modifierSentence.getAdjectivePositions();
        List<Integer> verbPositions = modifierSentence.getVerbPositions();
        List<Integer> adverbPositions = modifierSentence.getAdverbPositions();

        List<Float> modifierScores = modifierSentence.getScores();

        List<Float> modificationVector = new ArrayList<>(Collections.nCopies(modifierScores.size(), 1f));

        // If there are adjectives and adverbs in the sentence, then find the adverbs which modify and/or negate the
        // adjectives and update the modification vector with the modification and negation values
        if(adjectivePositions != null && adjectivePositions.size() > 0 && adverbPositions != null) {
            setModifiersAndNegators(modificationVector, adjectivePositions, adverbPositions, modifierScores);
        }

        // If there are verbs and adverbs in the sentence, then find the adverbs which modify and/or negate the
        // verbs and update the modification vector with the modification and negation values
        if(verbPositions != null && verbPositions.size() > 0 && adverbPositions != null) {
            setModifiersAndNegators(modificationVector, verbPositions, adverbPositions, modifierScores);
        }

        List<Float> modifiedFearScores = ListMath.hadamardProduct(modificationVector, fearScores);
        List<Float> modifiedAngerScores = ListMath.hadamardProduct(modificationVector, angerScores);
        List<Float> modifiedSadnessScores = ListMath.hadamardProduct(modificationVector, sadnessScores);
        List<Float> modifiedJoyScores = ListMath.hadamardProduct(modificationVector, joyScores);

        modifiedFearScores = stripZeroScores(modifiedFearScores);
        modifiedAngerScores = stripZeroScores(modifiedAngerScores);
        modifiedSadnessScores = stripZeroScores(modifiedSadnessScores);
        modifiedJoyScores = stripZeroScores(modifiedJoyScores);

        float sentenceFearScore = Math.max(-1, Math.min(ListMath.mean(modifiedFearScores), 1));
        float sentenceAngerScore = Math.max(-1, Math.min(ListMath.mean(modifiedAngerScores), 1));
        float sentenceSadnessScore = Math.max(-1, Math.min(ListMath.mean(modifiedSadnessScores), 1));
        float sentenceJoyScore = Math.max(-1, Math.min(ListMath.mean(modifiedJoyScores), 1));

        System.out.println("Modifier scores: ");
        System.out.println(modifierScores);
        System.out.println("Modification vector: ");
        System.out.println(modificationVector);

        System.out.println("Fear score: " + sentenceFearScore);
        System.out.println("Anger score: " + sentenceAngerScore);
        System.out.println("Sadness score: " + sentenceSadnessScore);
        System.out.println("Joy score: " + sentenceJoyScore);

        Map<String, Float> moodScoreMap = new HashMap<>();
        moodScoreMap.put("fear", sentenceFearScore);
        moodScoreMap.put("anger", sentenceAngerScore);
        moodScoreMap.put("sadness", sentenceSadnessScore);
        moodScoreMap.put("joy", sentenceJoyScore);

        return moodScoreMap;

    }

}
