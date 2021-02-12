package com.dtj503.lexicalanalyzer.api.types;

/**
 * Interface for objects which are JSON serializable/deserializable.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public interface JsonObject {

	/**
	 * Method to serialize the object into a JSON string suitable for transmission over the web.
	 * @return the JSON in String format
	 */
	String writeValueAsString();
	
}
