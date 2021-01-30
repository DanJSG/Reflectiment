package com.dtj503.lexicalanalyzer.sentiment.types;

import com.dtj503.lexicalanalyzer.libs.sql.SQLEntityBuilder;
import com.dtj503.lexicalanalyzer.parsers.PartOfSpeechReducer;

import java.sql.ResultSet;

public class ScoreWordBuilder implements SQLEntityBuilder<ScoredWord> {

    @Override
    public ScoredWord fromResultSet(ResultSet sqlResults) {
        try {
            String word = sqlResults.getString("word");
            int posIndex = sqlResults.getInt("pos");
            float score = sqlResults.getFloat("sentiment");
            return new ScoredWord(word, PartOfSpeechReducer.getPartOfSpeechTag(posIndex), score);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
