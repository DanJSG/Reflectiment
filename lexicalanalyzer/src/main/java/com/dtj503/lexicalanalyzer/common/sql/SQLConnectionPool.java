package com.dtj503.lexicalanalyzer.common.sql;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * Final class which handles and manages the database connection pools automatically. Sets up the datasource based on
 * the database environment variables for the application, and sets the connection pool properties.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
@Component
public final class SQLConnectionPool {

	// Static datasource - there should only be one of these globally in the application
	private static final BasicDataSource DATA_SOURCE = new BasicDataSource();

	/**
	 * An autowired private constructor class which is automatically called when the application is started. This sets
	 * up all of the properties of the data source and connection pool. All parameters are automatically injected using
	 * Spring Boots dependency injection, and the values are taken from the system's environment variables.
	 */
	@Autowired
	private SQLConnectionPool(@Value("${SQL_USERNAME}") String sqlUsername,
							  @Value("${SQL_PASSWORD}") String sqlPassword,
							  @Value("${SQL_CONNECTION_STRING}") String sqlConnectionString) {
		// Set the connection string, username and password for connecting to the DB
		DATA_SOURCE.setUrl(sqlConnectionString);
		DATA_SOURCE.setUsername(sqlUsername);
		DATA_SOURCE.setPassword(sqlPassword);

		// Set the total number of active connections
		DATA_SOURCE.setMaxActive(200); // 200 active connections

		// Set the total number of idle (inactive) connections
		DATA_SOURCE.setMaxIdle(150); // 150 connections

		// Set the number of idle connections to always keep open
		DATA_SOURCE.setMinIdle(10); // 10 connections

		// Set the maximum SQL prepared statements that can be open simultaneously
		DATA_SOURCE.setMaxOpenPreparedStatements(200); // 200 statements

		// Set the max wait time before the connection request times out
		DATA_SOURCE.setMaxWait(2500); // 2.5 seconds

		// Set the time to wait before removing abandoned database connections
		DATA_SOURCE.setRemoveAbandonedTimeout(60); // 60 seconds

		// Sets the minimum amount of time a connection can be idle for
		DATA_SOURCE.setMinEvictableIdleTimeMillis(30000); // 30 seconds

		// Enable removing abandoned connections
		DATA_SOURCE.setRemoveAbandoned(true);

		// The time to wait between evicting idle connections
		DATA_SOURCE.setTimeBetweenEvictionRunsMillis(30000); // 30 seconds
	}

	/**
	 * Method to return a SQL database connection from the connection pool.
	 *
	 * @return database connection
	 * @throws SQLException if a database connection cannot be fetched from the pool
	 */
	public static Connection getConnection() throws SQLException {
		return DATA_SOURCE.getConnection();
	}
	
}
