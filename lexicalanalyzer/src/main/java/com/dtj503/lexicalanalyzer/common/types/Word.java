package com.dtj503.lexicalanalyzer.common.types;

import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.parsers.PartOfSpeechReducer;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a word as well as the tag for the relevant part of speech.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class Word implements Token {

    private final String word;
    private final String partOfSpeech;
    private final int partOfSpeechIndex;

    public Word(String word, String partOfSpeech) {
        this.word = word;
        this.partOfSpeech = partOfSpeech;
        this.partOfSpeechIndex = PartOfSpeechReducer.getPartOfSpeechIndex(partOfSpeech);
    }

    public String getWord() {
        return word;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    @Override
    public float getScore() {
        return 0;
    }

    public int getPartOfSpeechIndex() {
        return partOfSpeechIndex;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", pos='" + partOfSpeech + '\'' +
                '}';
    }

    @Override
    public Map<SQLColumn, Object> toSqlMap() {
        Map<SQLColumn, Object> map = new HashMap<>();
        map.put(SQLColumn.WORD, this.getWord());
        map.put(SQLColumn.POS, PartOfSpeechReducer.getPartOfSpeechIndex(this.getPartOfSpeech()));
        return map;
    }

}
