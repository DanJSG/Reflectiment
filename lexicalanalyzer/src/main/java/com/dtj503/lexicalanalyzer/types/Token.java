package com.dtj503.lexicalanalyzer.types;

import com.dtj503.lexicalanalyzer.libs.sql.SQLEntity;

/**
 * Interface for word tokens.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public interface Token extends SQLEntity {

    /**
     * Method to get the word from the token.
     *
     * @return the word in <code>string</code> format
     */
    public String getWord();

    /**
     * Method to get the Part of Speech (PoS) tag of the word.
     *
     * @return the PoS tag in <code>string</code> format
     */
    public String getPartOfSpeech();

    /**
     * Method for getting the score of a word.
     *
     * @return the words score as a <code>float</code>
     */
    public float getScore();

}
