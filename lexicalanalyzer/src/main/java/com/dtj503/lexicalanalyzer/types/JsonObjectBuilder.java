package com.dtj503.lexicalanalyzer.types;

public interface JsonObjectBuilder<T extends JsonObject> {
	
	public T fromJson(String json);
	
}
