package com.dtj503.lexicalanalyzer.common.types;

import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.sql.SQLEntity;

import java.util.HashMap;
import java.util.Map;

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
