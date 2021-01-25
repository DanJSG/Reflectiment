package com.dtj503.lexicalanalyzer.libs.sql;

import java.util.HashMap;
import java.util.Map;

public enum SQLColumn implements Whitelist {
	
	ID, USERNAME;
	
	private static final Map<String, SQLColumn> mapping = new HashMap<>(16);
	
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
