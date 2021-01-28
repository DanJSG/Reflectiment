package com.dtj503.lexicalanalyzer.types;

public interface JsonObject {

	/**
	 * Method to serialize the object into a JSON string suitable for transmission over the web.
	 * @return the JSON in String format
	 */
	public String writeValueAsString();
	
}
