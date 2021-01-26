package com.dtj503.lexicalanalyzer.libs.sql;

import java.util.HashMap;
import java.util.Map;

/**
 * Whitelist enum for SQL table names which need to be referenced directly within the application.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public enum SQLTable implements Whitelist {

	// Collection of SQL Table names that are needed to be referenced within the application
	// TODO add actual table names
	PLACEHOLDER;

	// Hashmap containing enums and their name
	private static final Map<String, SQLTable> mappings = new HashMap<>(1);

	// Static code block to add the enumerator members to the mappings hash map
	static {
		for(SQLTable table : SQLTable.values()) {
			mappings.put(table.name(), table);
		}
	}

	@Override
	public boolean validate(String value) {
		return mappings.containsKey(value);
	}
	
}
