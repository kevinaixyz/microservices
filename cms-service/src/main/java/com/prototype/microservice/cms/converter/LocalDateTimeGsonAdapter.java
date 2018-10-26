package com.prototype.microservice.cms.converter;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.prototype.microservice.cms.helper.cmsHelper;

public class LocalDateTimeGsonAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

	@Override
	public JsonElement serialize(LocalDateTime dateTime, Type type, JsonSerializationContext context) {
		String dateStr = cmsHelper.getInstance().formatLocalDateTime(dateTime);
		return new JsonPrimitive(dateStr);
	}

	@Override
	public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		LocalDateTime dateTime = cmsHelper.getInstance().parseIsoDate(json.getAsString()).atStartOfDay();
		return dateTime;
	}

}
