package it.uniba.di.sms1920.everit.utils.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.threeten.bp.LocalTime;

import java.lang.reflect.Type;

public class LocalTimeAdapter implements JsonDeserializer<LocalTime> {
    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String timeAsString = json.getAsString();
        if ((timeAsString != null) && (timeAsString.length() > 0)) {
            return LocalTime.parse(timeAsString);
        }
        else {
            return null;
        }
    }
}
