package com.dtj503.lexicalanalyzer.types;

// TODO review whether this is actually needed
public interface JsonObjectBuilder<T extends JsonObject> {
	
	public T fromJson(String json);
	
}
