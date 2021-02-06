package com.dtj503.lexicalanalyzer.common.sql;

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
	 * Save a single item in the connected SQL database.
	 *
	 * @param item the item to save in the database
	 * @return <code>true</code> if the item was persisted successfully, <code>false</code> otherwise
	 */
	public Boolean save(T item);

	/**
	 * Save a list of items in the connected SQL database.
	 *
	 * @param objects the list of items to save in the database
	 * @return <code>true</code> if the items were persisted successfully, <code>false</code> otherwise
	 */
	public Boolean saveMany(List<T> objects);

	/**
	 * Find all items in the database which have a specific value within a specific column.
	 *
	 * @param searchColumn the SQL table column name to use as the search target
	 * @param value the value to search the column for
	 * @param builder entity builder for the resulting objects
	 * @param <V> the type of value to search for
	 * @return a list of objects from the database, or null
	 */
	public <V> List<T> findWhereEqual(SQLColumn searchColumn, V value, SQLEntityBuilder<T> builder);

	/**
	 * Find a limited number of items from the database which have a specific value within a specific column.
	 *
	 * @param searchColumn the SQL table column name to use as the search target
	 * @param value the value to search the column for
	 * @param limit the number of items to fetch
	 * @param builder entity builder for the resulting objects
	 * @param <V> the type of value to search for
	 * @return a list of objects from the database, or null
	 */
	public <V> List<T> findWhereEqual(SQLColumn searchColumn, V value, int limit, SQLEntityBuilder<T> builder);

	/**
	 * Find a limited number of  items in a database where a collection of AND clauses are non-exclusively true.
	 * For example:
	 * 	... WHERE (A AND B) OR (C AND D) OR ...
	 *
	 * @param firstColumn the first column to search for values
	 * @param secondColumn the second column to search for values
	 * @param firstValues the first column's search values
	 * @param secondValues the second column's search values
	 * @param limit the total number of items to fetch
	 * @param builder the object builder
	 * @param <V> the first search value type
	 * @param <U> the second search value type
	 * @return a list of objects found and built from the database results
	 */
	public <V, U> List<T> findWhereEqualAndOr(SQLColumn firstColumn, SQLColumn secondColumn, List<V> firstValues, List<U> secondValues, int limit, SQLEntityBuilder<T> builder);

	/**
	 * Find a limited number of items in a database where a collection of OR clauses are true.
	 * For example:
	 * 	... WHERE A OR B OR C OR D OR ...
	 *
	 * @param searchColumns the columns to search for values
	 * @param values the values to search for
	 * @param limit the number of items to fetch
	 * @param builder the object builder
	 * @param <V> the search value type
	 * @return a list of objects found and built from the database results
	 */
	public <V> List<T> findWhereEqualOr(List<SQLColumn> searchColumns, List<V> values, int limit, SQLEntityBuilder<T> builder);

	/**
	 * Find a limited number of items from the database which have specific values within specific columns.
	 *
	 * @param searchColumns the SQL table column names to use as the search targets
	 * @param values the values to search the columns for
	 * @param limit the number of items to fetch
	 * @param builder entity builder for the resulting objects
	 * @param <V> the type of value to search for
	 * @return a list of objects from the database, or null
	 */
	public <V> List<T> findWhereEqualAnd(List<SQLColumn> searchColumns, List<V> values, int limit, SQLEntityBuilder<T> builder);

	/**
	 * Find all items from the database which have specific values within specific columns.
	 *
	 * @param searchColumns the SQL table column names to use as the search targets
	 * @param values the values to search the columns for
	 * @param builder entity builder for the resulting objects
	 * @param <V> the type of value to search for
	 * @return a list of objects from the database, or null
	 */
	public <V> List<T> findWhereEqualAnd(List<SQLColumn> searchColumns, List<V> values, SQLEntityBuilder<T> builder);

	/**
	 * Find all items from the database which meet a certain SQL like condition in a specific column. These criteria can
	 * be less specific than searching for something equalling something. For example, wildcards and templates can be
	 * used in these cases.
	 *
	 * @param searchColumn the SQL table column names to use as the conditional target
	 * @param value the value of the like condition
	 * @param builder entity builder for the resulting objects
	 * @param <V> the type of value to search for
	 * @return a list of objects from the database, or null
	 */
	public <V> List<T> findWhereLike(SQLColumn searchColumn, V value, SQLEntityBuilder<T> builder);

	/**
	 * Find a limited set of  items from the database which meet a certain SQL like condition in a specific column.
	 * These criteria can be less specific than searching for something equalling something. For example, wildcards and
	 * templates can be used in these cases.
	 *
	 * @param searchColumn the SQL table column names to use as the conditional target
	 * @param value the value of the like condition
	 * @param limit the number of items to fetch
	 * @param builder entity builder for the resulting objects
	 * @param <V> the type of value to search for
	 * @return
	 */
	public <V> List<T> findWhereLike(SQLColumn searchColumn, V value, int limit, SQLEntityBuilder<T> builder);

	/**
	 * Update an item in the database with a specific value.
	 *
	 * @param clauseColumn the column to search for the item to update
	 * @param clauseValue the value to search for
	 * @param row the updated row data
	 * @param <V> the type of value to be searched for
	 * @param <U> the type of the update data
	 * @return <code>true</code> if the item was updated successfully, <code>false</code> otherwise
	 */
	public <V, U> Boolean updateWhereEquals(SQLColumn clauseColumn, V clauseValue, Map<SQLColumn, U> row);

	/**
	 * Update an item in the database with a specific value.
	 *
	 * @param clauseColumn the column to search for the item to update
	 * @param clauseValue the value to search for
	 * @param updateColumn the column of data to be updated
	 * @param updateValue the updated value
	 * @param <V> the type of value to be searched for
	 * @param <U> the type of the update data
	 * @return <code>true</code> if the item was updated successfully, <code>false</code> otherwise
	 */
	public <V, U> Boolean updateWhereEquals(SQLColumn clauseColumn, V clauseValue, SQLColumn updateColumn, U updateValue);

	/**
	 * Update an item in the database with multiple new values.
	 *
	 * @param clauseColumn the column to search for the item to update
	 * @param clauseValue the value to search for
	 * @param updateColumns the columns of data to be updated
	 * @param updateValues the updated values
	 * @param <V> the type of value to be searched for
	 * @param <U> the type of the update data
	 * @return
	 */
	public <V, U> Boolean updateWhereEquals(SQLColumn clauseColumn, V clauseValue, List<SQLColumn> updateColumns, List<U> updateValues);

	/**
	 * Delete an item from the database which has a specific value.
	 *
	 * @param clauseColumn the column to search for the item
	 * @param clauseValue the value to search for
	 * @param <V> the type of value to be searched for
	 * @return <code>true</code> if the item was deleted, <code>false</code> otherwise
	 */
	public <V> Boolean deleteWhereEquals(SQLColumn clauseColumn, V clauseValue);
	
}
