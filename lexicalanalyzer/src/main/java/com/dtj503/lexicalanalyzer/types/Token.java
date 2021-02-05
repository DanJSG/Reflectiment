package com.dtj503.lexicalanalyzer.types;

import com.dtj503.lexicalanalyzer.libs.sql.SQLEntity;

public interface Token extends SQLEntity {

    public String getWord();

    public String getPartOfSpeech();

    public float getScore();

}
