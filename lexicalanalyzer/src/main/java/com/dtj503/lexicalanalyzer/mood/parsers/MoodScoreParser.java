package com.dtj503.lexicalanalyzer.mood.parsers;

import com.dtj503.lexicalanalyzer.common.parsers.ScoreParser;
import com.dtj503.lexicalanalyzer.common.types.ScoredWord;
import com.dtj503.lexicalanalyzer.common.types.Sentence;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredWord;

import java.util.Map;

public class MoodScoreParser extends ScoreParser {

    public static void parseSentenceScore(Map<String, Sentence<MoodScoredWord>> moodSentenceMap,
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
    }

}
