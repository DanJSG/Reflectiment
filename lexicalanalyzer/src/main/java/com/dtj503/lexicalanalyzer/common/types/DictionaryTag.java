package com.dtj503.lexicalanalyzer.common.types;

import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.sql.SQLEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Dictionary tag used within the database for determining what dictionary a collection of words belong to. The index
 * represents the collection type they belong to, with 0 being sentiment, 1 being mood and 2 being reflection.
 */
public class DictionaryTag implements SQLEntity {

    private final String tag;
    private final int index;

    public DictionaryTag(String tag, int index) {
        this.tag = tag;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object obj) {
        // Check that the object is of the same type and then
        if(obj instanceof DictionaryTag) {
            DictionaryTag castObj = (DictionaryTag) obj;
            if(this.index == castObj.getIndex() && this.tag.contentEquals(castObj.getTag())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<SQLColumn, Object> toSqlMap() {
        Map<SQLColumn, Object> map = new HashMap<>();
        map.put(SQLColumn.TAG, tag);
        map.put(SQLColumn.TBL_IDX, index);
        return map;
    }

}
