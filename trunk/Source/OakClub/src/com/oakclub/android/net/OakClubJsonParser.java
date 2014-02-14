package com.oakclub.android.net;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;

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
}
