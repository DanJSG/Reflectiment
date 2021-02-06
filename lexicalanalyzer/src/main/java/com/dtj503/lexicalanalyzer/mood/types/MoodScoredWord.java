package com.dtj503.lexicalanalyzer.mood.types;

import com.dtj503.lexicalanalyzer.libs.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.types.Word;

import java.util.HashMap;
import java.util.Map;

public class MoodScoredWord extends Word {

    private float score;
    private String emotion;

    public MoodScoredWord(String word, String partOfSpeech) {
        this(word, partOfSpeech, null, 0);
    }

    public MoodScoredWord(String word, String partOfSpeech, String emotion, float score) {
        super(word, partOfSpeech);
        this.emotion = emotion;
        this.score = score;
    }

    @Override
    public float getScore() {
        return score;
    }

    public String getEmotion() {
        return emotion;
    }

    @Override
    public Map<SQLColumn, Object> toSqlMap() {
        Map<SQLColumn, Object> map = new HashMap<>();
        map.put(SQLColumn.WORD, this.getWord());
        map.put(SQLColumn.EMOTION, emotion);
        map.put(SQLColumn.SCORE, score);
        return map;
    }

    @Override
    public String toString() {
        return "MoodScoredWord{" +
                "word='" + getWord() + '\'' +
                "score=" + score +
                ", emotion='" + emotion + '\'' +
                '}';
    }
}
