package com.dtj503.lexicalanalyzer.libs.sql;

/**
 * Interface to be used for whitelisting specific strings used within a SQL database. This can help improve security by
 * reducing the risk of SQL injection attacks by limiting access to a subset of the tables and columns in a database.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public interface Whitelist {

	/**
	 * Method to validate whether or not a given string matches an item in the whitelist.
	 * @param value the string to validate
	 * @return <code>true</code> if the string matches item in whitelist, <code>false</code> otherwise
	 */
	public boolean validate(String value);
	
}
