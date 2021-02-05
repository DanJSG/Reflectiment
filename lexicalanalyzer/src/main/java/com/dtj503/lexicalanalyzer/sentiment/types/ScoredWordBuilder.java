package com.dtj503.lexicalanalyzer.sentiment.types;

import com.dtj503.lexicalanalyzer.libs.sql.SQLEntityBuilder;
import com.dtj503.lexicalanalyzer.parsers.PartOfSpeechReducer;
import com.dtj503.lexicalanalyzer.types.Token;

import java.sql.ResultSet;

/**
 * Builder class for a scored word. Designed to build a scored word from a set of SQL query results.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class ScoredWordBuilder implements SQLEntityBuilder<Token> {

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
