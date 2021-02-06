package com.dtj503.lexicalanalyzer.common.sql;

import java.sql.ResultSet;

/**
 * Interface for a builder class that can be used to automatically construct a Java object from a ResultSet returned
 * from a database query.
 *
 * @param <T> the object type that is built by this builder. Must extend SQLEntity.
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public interface SQLEntityBuilder<T extends SQLEntity> {

	public T fromResultSet(ResultSet sqlResults);
	
}
