package com.prototype.microservice.cms.converter;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.prototype.microservice.cms.helper.cmsHelper;

public class LocalDateGsonAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

	@Override
	public JsonElement serialize(LocalDate date, Type type, JsonSerializationContext context) {
		String dateStr = cmsHelper.getInstance().formatLocalDate(date);
		return new JsonPrimitive(dateStr);
	}

	@Override
	public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		LocalDate localDate = cmsHelper.getInstance().parseIsoDate(json.getAsString());
		return localDate;
	}

}
