package com.dtj503.lexicalanalyzer.libs.sql;

import java.util.Map;

public interface SQLEntity {
	
	public Map<SQLColumn, Object> toSqlMap();
	
}
