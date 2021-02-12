package com.dtj503.lexicalanalyzer.common.types;

import com.dtj503.lexicalanalyzer.common.sql.SQLEntity;

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
    String getWord();

    /**
     * Method to get the Part of Speech (PoS) tag of the word.
     *
     * @return the PoS tag in <code>string</code> format
     */
    String getPartOfSpeech();

    /**
     * Method for getting the score of a word.
     *
     * @return the words score as a <code>float</code>
     */
    float getScore();

}
