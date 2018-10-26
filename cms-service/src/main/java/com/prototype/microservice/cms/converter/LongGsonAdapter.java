package com.prototype.microservice.cms.converter;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LongGsonAdapter implements JsonSerializer<Long>, JsonDeserializer<Long>{

	@Override
	public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		// TODO Auto-generated method stub
		return Long.parseLong(json.getAsString());
	}

	@Override
	public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
		// TODO Auto-generated method stub
		return new JsonPrimitive(src.toString());
	}

}
