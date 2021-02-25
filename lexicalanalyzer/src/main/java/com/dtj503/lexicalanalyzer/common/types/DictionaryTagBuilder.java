package com.dtj503.lexicalanalyzer.common.types;

import com.dtj503.lexicalanalyzer.common.sql.SQLEntityBuilder;

import java.sql.ResultSet;

/**
 * SQL builder class for a dictionary tag.
 */
public class DictionaryTagBuilder implements SQLEntityBuilder<DictionaryTag> {

    @Override
    public DictionaryTag fromResultSet(ResultSet sqlResults) {
        try {
            String tag = sqlResults.getString("tag");
            int index = sqlResults.getInt("tbl_idx");
            return new DictionaryTag(tag, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
