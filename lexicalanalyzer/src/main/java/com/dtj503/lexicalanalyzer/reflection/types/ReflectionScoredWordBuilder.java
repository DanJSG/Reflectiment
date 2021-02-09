package com.dtj503.lexicalanalyzer.reflection.types;

import com.dtj503.lexicalanalyzer.common.parsers.PartOfSpeechReducer;
import com.dtj503.lexicalanalyzer.common.sql.SQLEntityBuilder;

import java.sql.ResultSet;

public class ReflectionScoredWordBuilder implements SQLEntityBuilder<ReflectionScoredWord> {

    @Override
    public ReflectionScoredWord fromResultSet(ResultSet sqlResults) {
        try {
            String word = sqlResults.getString("word");
            String category = sqlResults.getString("category");
            int posIndex = sqlResults.getInt("pos");
            float score = sqlResults.getFloat("score");
            return new ReflectionScoredWord(word, PartOfSpeechReducer.getPartOfSpeechTag(posIndex), score, category);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}