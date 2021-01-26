package com.dtj503.lexicalanalyzer.libs.sql;

import java.util.Map;

/**
 * Interface for a SQL database entity, allowing it to be converted to a map for insertion into the database.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public interface SQLEntity {

	/**
	 * Method for mapping a SQL database entity to a Java Map, used for inserting items into the database.
	 * @return a Map representing the entity
	 */
	public Map<SQLColumn, Object> toSqlMap();
	
}
