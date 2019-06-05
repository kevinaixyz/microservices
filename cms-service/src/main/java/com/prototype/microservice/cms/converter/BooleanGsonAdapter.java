package com.prototype.microservice.cms.converter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class BooleanGsonAdapter implements JsonSerializer<Boolean>, JsonDeserializer<Boolean> {

    @Override
    public JsonElement serialize(Boolean b, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(Boolean.TRUE.equals(b) ? "Y" : "N");
    }

    @Override
    public Boolean deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return json.getAsString().toUpperCase().equals('Y');
    }
}
