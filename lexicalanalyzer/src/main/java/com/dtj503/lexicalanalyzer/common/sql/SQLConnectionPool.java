package com.dtj503.lexicalanalyzer.common.sql;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
/**
 * Final class which handles and manages the database connection pools automatically. Sets up the datasource based on
 * the database environment variables for the application, and sets the connection pool properties.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public final class SQLConnectionPool {

	// Static datasource - there should only be one of these globally in the application
	private static BasicDataSource dataSource = new BasicDataSource();
	
	@Autowired
	/**
	 * An autowired private constructor class which is automatically called when the application is started. This sets
	 * up all of the properties of the data source and connection pool. All parameters are automatically injected using
	 * Spring Boots dependency injection, and the values are taken from the system's environment variables.
	 */
	private SQLConnectionPool(@Value("${SQL_USERNAME}") String sqlUsername,
							  @Value("${SQL_PASSWORD}") String sqlPassword,
							  @Value("${SQL_CONNECTION_STRING}") String sqlConnectionString) {
		// Set the connection string, username and password for connecting to the DB
		dataSource.setUrl(sqlConnectionString);
		dataSource.setUsername(sqlUsername);
		dataSource.setPassword(sqlPassword);

		// Set the total number of active connections
		dataSource.setMaxActive(200); // 200 active connections

		// Set the total number of idle (inactive) connections
		dataSource.setMaxIdle(150); // 150 connections

		// Set the number of idle connections to always keep open
		dataSource.setMinIdle(10); // 10 connections

		// Set the maximum SQL prepared statements that can be open simultaneously
		dataSource.setMaxOpenPreparedStatements(200); // 200 statements

		// Set the max wait time before the connection request times out
		dataSource.setMaxWait(2500); // 2.5 seconds

		// Set the time to wait before removing abandoned database connections
		dataSource.setRemoveAbandonedTimeout(60); // 60 seconds

		// Sets the minimum amount of time a connection can be idle for
		dataSource.setMinEvictableIdleTimeMillis(30000); // 30 seconds

		// Enable removing abandoned connections
		dataSource.setRemoveAbandoned(true);

		// The time to wait between evicting idle connections
		dataSource.setTimeBetweenEvictionRunsMillis(30000); // 30 seconds
	}

	/**
	 * Method to return a SQL database connection from the connection pool.
	 *
	 * @return database connection
	 * @throws SQLException if a database connection cannot be fetched from the pool
	 */
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
}
