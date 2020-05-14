package it.uniba.di.sms1920.everit.utils.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public final class BooleanAdapter implements JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            int value = json.getAsInt();
            return value != 0;
        }
        catch (NumberFormatException e) {
            String value = json.getAsString();
            return value.equals("true");
        }
    }
}
