package com.dtj503.lexicalanalyzer.mood.types;

import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.types.ScoredWord;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a word scored with a value representing the intensity of a specific emotion.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class MoodScoredWord extends ScoredWord {

    // The label for the emotion
    private final String emotion;

    /**
     * Constructor which stores the word, part of speech tag, emotion and the score.
     *
     * @param word the word
     * @param partOfSpeech the PoS tag for the word
     * @param emotion the emotion the word represents (anger, fear, sadness or joy)
     * @param score the intensity score of the emotion
     */
    public MoodScoredWord(String word, String partOfSpeech, String emotion, float score) {
        super(word, partOfSpeech, score);
        this.emotion = emotion;
    }

    /**
     * Method for getting the emotion of the word.
     * @return the emotion of the word as a <code>String</code>
     */
    public String getEmotion() {
        return emotion;
    }

    @Override
    public Map<SQLColumn, Object> toSqlMap() {
        Map<SQLColumn, Object> map = new HashMap<>();
        map.put(SQLColumn.WORD, this.getWord());
        map.put(SQLColumn.EMOTION, emotion);
        map.put(SQLColumn.SCORE, getScore());
        return map;
    }

    @Override
    public String toString() {
        return "MoodScoredWord{" +
                "word='" + getWord() + '\'' +
                "score=" + getScore() +
                ", emotion='" + emotion + '\'' +
                '}';
    }
}
