package com.dtj503.lexicalanalyzer.sentiment.types;

import com.dtj503.lexicalanalyzer.libs.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.libs.sql.SQLEntity;
import com.dtj503.lexicalanalyzer.parsers.PartOfSpeechReducer;
import com.dtj503.lexicalanalyzer.types.Word;

import java.util.HashMap;
import java.util.Map;

public class ScoredWord extends Word implements SQLEntity {

    private float score;

    public ScoredWord(String word, String partOfSpeech) {
        this(word, partOfSpeech, 0);
    }

    public ScoredWord(String word, String partOfSpeech, float score) {
        super(word, partOfSpeech);
        this.score = score;
    }

    @Override
    public Map<SQLColumn, Object> toSqlMap() {
        Map<SQLColumn, Object> map = new HashMap<>();
        map.put(SQLColumn.WORD, this.getWord());
        map.put(SQLColumn.POS, PartOfSpeechReducer.getPartOfSpeechIndex(this.getPartOfSpeech()));
        return map;
    }

    public float getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "ScoredWord{" +
                "word='" + this.getWord() + '\'' +
                ", pos='" + this.getPartOfSpeech() + '\'' +
                ", score=" + score +
                '}';
    }
}
