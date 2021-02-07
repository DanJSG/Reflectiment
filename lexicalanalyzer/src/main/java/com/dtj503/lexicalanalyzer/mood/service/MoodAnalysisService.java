package com.dtj503.lexicalanalyzer.mood.service;

import com.dtj503.lexicalanalyzer.common.parsers.StringParser;
import com.dtj503.lexicalanalyzer.common.services.AnalysisService;
import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.sql.SQLTable;
import com.dtj503.lexicalanalyzer.common.types.*;
import com.dtj503.lexicalanalyzer.mood.parsers.MoodScoreParser;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredSentence;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredWord;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredWordBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoodAnalysisService extends AnalysisService {

    public static List<MoodScoredSentence> analyseMood(String text) {
        // TODO remove debugging prints
        System.out.println("Text  arrived at the analyseMood(x) function.");
        System.out.println(text);

        Document<Token> doc = StringParser.parseText(text);

        List<MoodScoredSentence> moodScoredSentences = new ArrayList<>();

        for(Sentence<Token> sentence : doc.getSentences()) {

            List<Token> words = sentence.getWords();

            List<MoodScoredWord> moodScoredWords = fetchWordScores(words, SQLTable.MOOD, SQLColumn.WORD, new MoodScoredWordBuilder());
            Map<String, List<MoodScoredWord>> scoredWordMap = buildScoredWordMap(words, moodScoredWords);
            Map<String, Sentence<MoodScoredWord>> moodSentenceMap = buildMoodSentenceMap(sentence, scoredWordMap);

            // Modifiers
            List<ScoredWord> modifierScoredWords = fetchWordScores(words, SQLTable.SENTIMENT, SQLColumn.WORD, new ScoredWordBuilder());
            modifierScoredWords = pickScoredWord(words, modifierScoredWords);
            Sentence<ScoredWord> modifierSentence = new Sentence<>(sentence.getOriginalText(), modifierScoredWords);

            Map<String, Float> moodMap = MoodScoreParser.parseSentenceScore(moodSentenceMap, modifierSentence);

            System.out.println("Showing mood map scores: ");
            moodMap.forEach((mood, score) -> {
                System.out.println(mood + ": " + score);
            });

            Pair<String, Float> strongestEmotionProperties = pickStrongestEmotion(moodMap);

            System.out.println("Strongest emotion -> " +
                                strongestEmotionProperties.getLeft() + ": " + strongestEmotionProperties.getRight());

            MoodScoredSentence scoredSentence = new MoodScoredSentence(sentence.getOriginalText(),
                                                moodSentenceMap.get(strongestEmotionProperties.getLeft()).getWords(),
                                                strongestEmotionProperties.getRight(),
                                                strongestEmotionProperties.getLeft(), moodMap);

            moodScoredSentences.add(scoredSentence);

        }

        return moodScoredSentences;

    }

    private static Map<String, Sentence<MoodScoredWord>> buildMoodSentenceMap(Sentence<Token> originalSentence,
                                                                              Map<String, List<MoodScoredWord>> scoredMoodWordMap) {
        Map<String, Sentence<MoodScoredWord>> moodSentenceMap = new HashMap<>();

        List<Token> words = originalSentence.getWords();

        List<MoodScoredWord> fearWords = pickMoodScoredWords(words, "fear", scoredMoodWordMap);
        moodSentenceMap.put("fear", new Sentence<>(originalSentence.getOriginalText(), fearWords));

        List<MoodScoredWord> angerWords = pickMoodScoredWords(words, "anger", scoredMoodWordMap);
        moodSentenceMap.put("anger", new Sentence<>(originalSentence.getOriginalText(), angerWords));

        List<MoodScoredWord> sadnessWords = pickMoodScoredWords(words, "sadness", scoredMoodWordMap);
        moodSentenceMap.put("sadness", new Sentence<>(originalSentence.getOriginalText(), sadnessWords));

        List<MoodScoredWord> joyWords = pickMoodScoredWords(words, "joy", scoredMoodWordMap);
        moodSentenceMap.put("joy", new Sentence<>(originalSentence.getOriginalText(), joyWords));

        return moodSentenceMap;

    }

    private static List<MoodScoredWord> pickMoodScoredWords(List<Token> words, String emotion,
                                                            Map<String, List<MoodScoredWord>> scoredMoodWordMap) {
        List<MoodScoredWord> pickedWords = new ArrayList<>();
        for(Token word : words) {
            List<MoodScoredWord> scoredWords = scoredMoodWordMap.get(word.getWord());
            int index = -1;
            for(int i = 0; i < scoredWords.size(); i++) {
                if(scoredWords.get(i).getEmotion().contentEquals(emotion)) {
                    index = i;
                }
            }
            if(index == -1) {
                pickedWords.add(new MoodScoredWord(word.getWord(), word.getPartOfSpeech(), emotion, 0));
            } else {
                pickedWords.add(scoredWords.get(index));
            }
        }
        return pickedWords;
    }

    private static Pair<String, Float> pickStrongestEmotion(Map<String, Float> moodScoreMap) {
        float highestScore = -1f;
        String label = null;

        String[] moods = moodScoreMap.keySet().toArray(String[]::new);

        for(String mood : moods) {
            float score = moodScoreMap.get(mood);
            if(score > highestScore) {
                highestScore = score;
                label = mood;
            }
        }

        return new ImmutablePair<>(label, highestScore);

    }

}
