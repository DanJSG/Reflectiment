package com.dtj503.lexicalanalyzer.libs.sql;

import java.util.List;
import java.util.Map;

/**
 * Interface for a SQL repository containing basic Create, Read, Update and Delete (CRUD) method contracts.
 *
 * @param <T> the entity type to be handled by the SQL repository
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public interface SQLRepository<T extends SQLEntity> {

	/**
	 * Save a single item within the connected SQL database.
	 *
	 * @param item the item to save in the database
	 * @return <code>true</code> if the item was persisted successfully, <code>false</code> otherwise
	 */
	public Boolean save(T item);
	
	public Boolean saveMany(List<T> objects);
	
	public <V> List<T> findWhereEqual(SQLColumn searchColumn, V value, SQLEntityBuilder<T> builder);
	
	public <V> List<T> findWhereEqual(SQLColumn searchColumn, V value, int limit, SQLEntityBuilder<T> builder);
	
	public <V> List<T> findWhereEqual(List<SQLColumn> searchColumns, List<V> values, int limit, SQLEntityBuilder<T> builder);
	
	public <V> List<T> findWhereEqual(List<SQLColumn> searchColumns, List<V> values, SQLEntityBuilder<T> builder);
	
	public <V> List<T> findWhereLike(SQLColumn searchColumn, V value, SQLEntityBuilder<T> builder);
	
	public <V> List<T> findWhereLike(SQLColumn searchColumn, V value, int limit, SQLEntityBuilder<T> builder);
	
	public <V, U> Boolean updateWhereEquals(SQLColumn clauseColumn, V clauseValue, Map<SQLColumn, U> row);
	
	public <V, U> Boolean updateWhereEquals(SQLColumn clauseColumn, V clauseValue, SQLColumn updateColumn, U updateValue);
	
	public <V, U> Boolean updateWhereEquals(SQLColumn clauseColumn, V clauseValue, List<SQLColumn> updateColumns, List<U> updateValues);
	
	public <V, U> Boolean deleteWhereEquals(SQLColumn clauseColumn, V clauseValue);
	
}
