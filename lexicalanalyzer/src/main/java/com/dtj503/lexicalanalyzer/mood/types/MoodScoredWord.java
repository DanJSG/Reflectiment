package com.dtj503.lexicalanalyzer.mood.types;

import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.types.ScoredWord;

import java.util.HashMap;
import java.util.Map;

public class MoodScoredWord extends ScoredWord {

    private String emotion;

    public MoodScoredWord(String word, String partOfSpeech, String emotion, float score) {
        super(word, partOfSpeech, score);
        this.emotion = emotion;
    }

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
