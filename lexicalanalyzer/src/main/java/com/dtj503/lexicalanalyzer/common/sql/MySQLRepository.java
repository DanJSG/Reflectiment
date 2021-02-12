package com.dtj503.lexicalanalyzer.common.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Class for a MySQL or MariaDB database repository. Provides implementations for all basic Create, Read, Update and
 * Delete (CRUD) methods, and contains table and column name checks to avoid SQL injection.
 *
 * @param <T> the entity type to be handled by the SQL repository
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class MySQLRepository<T extends SQLEntity> implements SQLRepository<T>{

	// The name of the database table or view - cannot be changed once changed
	private final String tableName;

	/**
	 * Constructor for a SQL database repository connected to a specific table (or view) in the database.
	 *
	 * @param tableName the name of the table or view in the database
	 */
	public MySQLRepository(SQLTable tableName) {
		this.tableName = tableName.name();
	}
	
	@Override
	public Boolean save(T object) {
		// Uses the saveMany function to save a single object
		return saveMany(Arrays.asList(object));
	}
	
	@Override
	public Boolean saveMany(List<T> objects) {

		// Fetch database connection and return false if could not be fetched
		Connection connection = getConnection();
		if(connection == null || objects.size() == 0) {
			return false;
		}

		// Convert the Java objects to Maps and Arrays
		Map<SQLColumn, Object> valueMap = objects.get(0).toSqlMap();
		Object[] values = valueMap.values().toArray();

		// Generate SQL query
		String query = 
				"INSERT INTO `" + tableName + "` (" + stringifyKeys(valueMap) + 
				") VALUES (" + createParamMarkers(values) + ")";

		PreparedStatement statement;
		try {
			int count = 0;

			// Initialise database prepared statement
			statement = connection.prepareStatement(query);

			// Loop through each object to be saved in the database
			for(T object : objects) {

				// Fetch the next value for everything other than the first object
				if(count > 0) {
					valueMap = object.toSqlMap();
					values = valueMap.values().toArray();
				}

				// For each value in the current object, add the value to the prepared statement
				for(int i=0; i < values.length; i++) {
					statement.setObject(i + 1, values[i]);
				}

				// Add the current query to the prepared statement and increment the counter
				statement.addBatch();
				count++;

				// If the count is 500 or it has reached the total number of objects, then execute the statement and
				// return the connection to the connection pool
				if(count % 500 == 0 || count == objects.size()) {
					statement.executeBatch();
					connection.commit();
					connection.close();
				}
			}

			// Return true due to signal success
			return true;

		} catch(Exception e) {
			// Print error
			e.printStackTrace();
			// If exception thrown, then return false to signal failure
			return false;
		}
		
	}
	
	@Override
	public <V> List<T> findWhereEqual(SQLColumn searchColumn, V value, int limit, SQLEntityBuilder<T> builder) {
		return findWhereEqualAnd(Arrays.asList(searchColumn), Arrays.asList(value), limit, builder);
	}

	public <V, U> List<T> findWhereEqualAndOr(SQLColumn firstColumn, SQLColumn secondColumn, List<V> firstValues,
											  List<U> secondValues, SQLEntityBuilder<T> builder) {
		return findWhereEqualAndOr(firstColumn, secondColumn, firstValues, secondValues, 0, builder);
	}

	@Override
	public <V, U> List<T> findWhereEqualAndOr(SQLColumn firstColumn, SQLColumn secondColumn, List<V> firstValues,
											  List<U> secondValues, int limit, SQLEntityBuilder<T> builder) {
		// Fetch connection and return null if it cannot be fetched
		Connection connection = getConnection();
		if(connection == null || firstValues.size() != secondValues.size()) {
			return null;
		}
		// Initialise incomplete  SQL query
		String baseQuery = "SELECT * FROM `" + tableName + "` WHERE ";

		List<String> andConditions = new ArrayList<>();
		for(int i = 0; i < firstValues.size(); i++) {
			andConditions.add("(" + firstColumn.name() + "=?" + " AND " + secondColumn.name() + "=?" + ")");
		}
		StringBuilder queryCondition = new StringBuilder();
		for(int i = 0; i < andConditions.size(); i++)  {
			queryCondition.append(andConditions.get(i));
			if(i < andConditions.size() - 1) {
				queryCondition.append(" OR ");
			}
		}
		queryCondition.append(";");
		String query = baseQuery + queryCondition;

		System.out.println(query);

		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setFetchSize(limit);
			int n;
			for(int i = 0; i < firstValues.size(); i++) {
				n = ((i + 1) * 2) - 1;
				statement.setObject(n, firstValues.get(i));
				statement.setObject(n + 1, secondValues.get(i));
			}
			System.out.println(statement.toString());
			return executeStatementAndBuildObjects(builder, connection, statement);

		} catch (SQLException e) 	{
			e.printStackTrace();
			return null;
		}
	}

	private List<T> executeStatementAndBuildObjects(SQLEntityBuilder<T> builder, Connection connection, PreparedStatement statement) throws SQLException {
		ResultSet results = statement.executeQuery();
		connection.commit();
		ArrayList<T> objectList = new ArrayList<>();
		while(results.next()) {
			objectList.add(builder.fromResultSet(results));
		}
		if(objectList.size() == 0) {
			connection.close();
			return null;
		}
		connection.close();
		return objectList;
	}

	public <V> List<T> findWhereEqualOr(SQLColumn searchColumn, List<V> values, SQLEntityBuilder<T> builder) {
		return findWhereEqualOr(searchColumn, values, 0, builder);
	}

	public <V> List<T> findWhereEqualOr(SQLColumn searchColumn, List<V> values, int limit, SQLEntityBuilder<T> builder) {
		List<SQLColumn> searchColumns = new ArrayList<>(Collections.nCopies(values.size(), searchColumn));
		return findWhereEqualOr(searchColumns, values, limit, builder);
	}

	@Override
	public <V> List<T> findWhereEqualOr(List<SQLColumn> searchColumns, List<V> values, int limit,
										SQLEntityBuilder<T> builder) {
		// Fetch connection and return null if it cannot be fetched
		Connection connection = getConnection();
		if(connection == null || searchColumns.size() != values.size()) {
			return null;
		}

		// Initialise incomplete  SQL query
		String baseQuery = "SELECT * FROM `" + tableName + "` WHERE ";

		// Complete SQL query using string concatenation in a loop
		String queryCondition = "";
		for(int i=0; i < searchColumns.size(); i++) {
			queryCondition += searchColumns.get(i).name() + "=?";
			if(i < searchColumns.size() - 1) {
				queryCondition += " OR ";
			}
		}
		queryCondition += ";";

		// Concatenate sections of query
		String query = baseQuery + queryCondition;

		// Send the query to the database - return the resulting objects if it succeeds, null otherwise
		try {
			return runCustomSelectQuery(connection, query, values, limit, builder);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <V> List<T> findWhereEqualAnd(List<SQLColumn> searchColumns, List<V> values, int limit,
										 SQLEntityBuilder<T> builder) {
		// Fetch connection and return null if it cannot be fetched
		Connection connection = getConnection();
		if(connection == null || searchColumns.size() != values.size()) {
			return null;
		}

		// Initialise incomplete  SQL query
		String baseQuery = "SELECT * FROM `" + tableName + "` WHERE ";

		// Complete SQL query using string concatenation in a loop
		String queryCondition = "";
		for(int i=0; i < searchColumns.size(); i++) {
			queryCondition += searchColumns.get(i).name() + "=?";
			if(i < searchColumns.size() - 1) {
				queryCondition += " AND ";
			}
		}
		queryCondition += ";";

		// Concatenate sections of query
		String query = baseQuery + queryCondition;

		// Send the query to the database - return the resulting objects if it succeeds, null otherwise
		try {
			return runCustomSelectQuery(connection, query, values, limit, builder);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public <V> List<T> findWhereLike(SQLColumn searchColumn, V value, int limit, SQLEntityBuilder<T> builder) {
		// Fetch database connection, return null if connection cannot be made
		Connection connection = getConnection();
		if(connection == null) {
			return null;
		}

		// Create SQL query
		String query = "SELECT * FROM `" + tableName + "` WHERE " + searchColumn.name() + " LIKE ?;";

		// Send the query to the database - return the resulting objects if it succeeds, null otherwise
		try {
			return runCustomSelectQuery(connection, query, Arrays.asList(value), limit, builder);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public <V> List<T> findWhereLike(SQLColumn searchColumn, V value, SQLEntityBuilder<T> builder) {
		return findWhereLike(searchColumn, value, 0, builder);
	}
	
	@Override
	public <V> List<T> findWhereEqual(SQLColumn searchColumn, V value, SQLEntityBuilder<T> builder) {
		return findWhereEqual(searchColumn, value, 0, builder);
	}
	
	@Override
	public <V> List<T> findWhereEqualAnd(List<SQLColumn> searchColumns, List<V> values, SQLEntityBuilder<T> builder) {
		return findWhereEqualAnd(searchColumns, values, 0, builder);
	}
	
	@Override
	public <V, U> Boolean updateWhereEquals(SQLColumn clauseColumn, V clauseValue, Map<SQLColumn, U> row) {

		// Convert row map values to arrays
		ArrayList<SQLColumn> columns = new ArrayList<>(row.size());
		ArrayList<U> values = new ArrayList<>(row.size());
		row.forEach((key, value) -> {
			columns.add(key);
			values.add(value);
		});

		// Carry out function using converted objects
		return updateWhereEquals(clauseColumn, clauseValue, columns, values);
	}
	
	@Override
	public <V, U> Boolean updateWhereEquals(SQLColumn clauseColumn, V clauseValue, SQLColumn updateColumn,
											U updateValue) {
		return updateWhereEquals(clauseColumn, clauseValue, Arrays.asList(updateColumn), Arrays.asList(updateValue));
	}
	
	@Override
	public <V, U> Boolean updateWhereEquals(SQLColumn clauseColumn, V clauseValue, List<SQLColumn> updateColumns,
											List<U> updateValues) {
		// Get database connection, return false if one cannot be retrieved
		Connection connection = getConnection();
		if(connection == null) {
			return false;
		}

		// Initialise database query
		String query = "UPDATE `" + tableName + "` SET ";

		// If the number of columns and values are not equal then return false
		if(updateColumns.size() != updateValues.size()) {
			return false;
		}

		// Finish off the SQL query using string concatenation in a loop
		for(int i=0; i < updateColumns.size(); i++) {
			query += updateColumns.get(i).name() + "=?";
			if(i < updateColumns.size() - 1)
				query += ", ";
		}
		query += " WHERE " + clauseColumn.name() + "=?;";
		try {
			// Initialise the prepared SQL statement
			PreparedStatement statement = connection.prepareStatement(query);

			// Set all the values to be included in the prepared SQL statement
			int i = 0;
			for(; i < updateColumns.size(); i++) {
				statement.setObject(i + 1, updateValues.get(i));
			}
			statement.setObject(i + 1, clauseValue);

			// Send the update query and return the connection to the connection pool
			statement.executeUpdate();
			connection.commit();
			connection.close();
			return true;
		} catch(Exception e) {
			// If an exception is thrown, print the error and return false to signal failure
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public <V> Boolean deleteWhereEquals(SQLColumn clauseColumn, V clauseValue) {
		// Fetch a database connection, return false if one cannot be retrieved
		Connection connection = getConnection();
		if(connection == null) {
			return false;
		}
		// Generate the SQL query
		String query = "DELETE FROM `" + tableName + "` WHERE " + clauseColumn.name() + "=?;";

		// Set the object value in the query, send the query, and then return true if successful, false otherwise
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setObject(1, clauseValue);
			statement.executeUpdate();
			connection.commit();
			connection.close();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Private method for converting a SQL entity's column name keys into a string of valid SQL syntax.
	 *
	 * @param valueMap Java map representation of a SQL entity
	 * @return valid SQL syntax representing a list of entity's SQL column names
	 */
	private String stringifyKeys(Map<SQLColumn, Object> valueMap) {

		String keyString = new String();

		// Convert Map keys to an array of SQLColumns
		SQLColumn[] keys = (SQLColumn[]) valueMap.keySet().toArray();

		// Generate a string with a list of these column names
		for(int i=0; i< valueMap.keySet().size(); i++) {
			keyString += keys[i].name();
			if(i != valueMap.keySet().size() - 1) {
				keyString += ",";
			}
		}

		// Return the string of comma separated column names
		return keyString;
	}

	/**
	 * Private method for taking a list of values to insert into a database and generating a list of question marks to
	 * represent these values in valid SQL syntax for a prepared statement.
	 *
	 * @param values the values to convert to ? markers
	 * @return a string of valid SQL syntax param markers
	 */
	private String createParamMarkers(Object[] values) {

		String paramString = new String();

		// Iterate through loop for each object and generate a list of comma separated question marks
		for(int i=0; i < values.length; i++) {
			paramString += "?";
			if(i < values.length - 1) {
				paramString += ",";
			}
		}

		return paramString;
	}

	/**
	 * Fetch a database connection from the connection pool, and disable autocommit to improve SQL query performance
	 * for batched queries.
	 *
	 * @return a database connection, or <code>null</code> if a connection cannot be made
	 */
	private Connection getConnection() {
		try {
			Connection connection = SQLConnectionPool.getConnection();
			connection.setAutoCommit(false);
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Private method for sending a custom select query string to the SQL database. Returns a list of objects and or
	 * null if the request does not succeed. This is a private method and should never be made accessible outside of
	 * this class as passing custom end-user queries to the database can open up the application to SQL injection
	 * attacks.
	 *
	 * @param connection the database connection
	 * @param query the custom SQL query
	 * @param values the values to use in the query
	 * @param limit the maximum number of items to return
	 * @param builder the entity builder for the returned objects
	 * @param <V> the type of values used in the query
	 * @return a list of objects
	 * @throws Exception if a database connection cannot be made
	 */
	private <V> List<T> runCustomSelectQuery(Connection connection, String query, List<V> values, int limit,
											 SQLEntityBuilder<T> builder) throws Exception {
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setFetchSize(limit);
		for(int i=0; i < values.size(); i++) {
			statement.setObject(i + 1, values.get(i));
		}
		return executeStatementAndBuildObjects((SQLEntityBuilder<T>) builder, connection, statement);
	}
	
}
