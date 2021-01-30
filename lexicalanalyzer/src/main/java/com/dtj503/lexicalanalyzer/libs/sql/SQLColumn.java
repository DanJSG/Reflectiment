package com.dtj503.lexicalanalyzer.libs.sql;

import java.util.HashMap;
import java.util.Map;

/**
 * Whitelist enum for SQL column names which need to be referenced directly within the application.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public enum SQLColumn implements Whitelist {

	// Collection of SQL column names that are referenced within the application
	ID, TAG, POS, WORD, SENTIMENT;

	// Hashmap containing enums and their name
	private static final Map<String, SQLColumn> mapping = new HashMap<>(16);

	// Static code block to add the enumerator members to the mappings hash map
	static {
		for(SQLColumn column : SQLColumn.values()) {
			mapping.put(column.name(), column);
		}
	}

	@Override
	public boolean validate(String value) {
		return mapping.containsKey(value);
	}
	
}
