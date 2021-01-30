package com.dtj503.lexicalanalyzer.sentiment.types;

import com.dtj503.lexicalanalyzer.libs.sql.SQLEntityBuilder;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ScoreWordBuilder implements SQLEntityBuilder<ScoredWord> {

    @Override
    public ScoredWord fromResultSet(ResultSet sqlResults) {
        try {
            String word = sqlResults.getString("word");
            int posIndex = sqlResults.getInt("pos");
            float score = sqlResults.getFloat("sentiment");
            Map<Integer, String> posMap = new HashMap<>();
            posMap.put(0, "v");
            posMap.put(1, "n");
            posMap.put(2, "r");
            posMap.put(3, "a");
            return new ScoredWord(word, posMap.get(posIndex), score);
        } catch (Exception e) {

        }
        return null;
    }

}
