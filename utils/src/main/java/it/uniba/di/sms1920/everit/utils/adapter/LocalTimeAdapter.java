package it.uniba.di.sms1920.everit.utils.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Type;


public class LocalTimeAdapter implements JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String timeAsString = json.getAsString();
        if ((timeAsString != null) && (timeAsString.length() > 0)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Adapter.JSON_DATETIME_FORMAT);
            return LocalDateTime.parse(timeAsString, formatter);
        }
        else {
            return null;
        }
    }
}
