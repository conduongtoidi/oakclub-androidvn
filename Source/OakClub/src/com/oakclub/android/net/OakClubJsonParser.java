package com.oakclub.android.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.type.TypeReference;

public class OakClubJsonParser {
	public static <T>T getJsonObjectByMapper(String result, Class<T> valueType){
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = new JsonFactory();
			JsonParser parser = factory.createJsonParser(result);
			mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
			JsonToken current = parser.nextToken();
			if (current != JsonToken.START_OBJECT) {
			      return null;
			}
			else {
				T object = mapper.readValue(result, valueType);
				return object;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static HashMap<String, Object> getHashmapByMapper(String result){
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = new JsonFactory();
			JsonParser parser = factory.createJsonParser(result);
			mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
			JsonToken current = parser.nextToken();
			TypeReference<HashMap<String,Object>> valueType 
	          = new TypeReference<HashMap<String,Object>>() {}; 
			if (current != JsonToken.START_OBJECT) {
			      return null;
			}
			else {
				HashMap<String, Object> object = mapper.readValue(result, valueType);
				return object;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
}
