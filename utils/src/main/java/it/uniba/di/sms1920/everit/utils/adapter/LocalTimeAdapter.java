package it.uniba.di.sms1920.everit.utils.adapter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Type;

import it.uniba.di.sms1920.everit.utils.Constants;


public class LocalTimeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

    private static final Gson jsonConverter = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat(Adapter.JSON_DATETIME_FORMAT)
            .create();

    @Override
    public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
        String time = src.format(formatter);

        return jsonConverter.toJsonTree(time, String.class);
    }

    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String timeAsString = json.getAsString();
        if ((timeAsString != null) && (timeAsString.length() > 0)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Adapter.JSON_TIME_FORMAT);
            return LocalTime.parse(timeAsString, formatter);
        } else {
            return null;
        }
    }

}
