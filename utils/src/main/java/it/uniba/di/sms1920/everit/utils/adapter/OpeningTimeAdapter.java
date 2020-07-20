package it.uniba.di.sms1920.everit.utils.adapter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Type;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.OpeningTime;

public class OpeningTimeAdapter implements JsonSerializer<OpeningTime>, JsonDeserializer<OpeningTime> {

    private static class Keys{
        private static final String OPENING_TIME = "opening_time";
        private static final String CLOSING_TIME = "closing_time";
    }
    private static final Gson jsonConverter = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .setDateFormat(Adapter.JSON_DATETIME_FORMAT)
            .create();

    @Override
    public JsonElement serialize(OpeningTime src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObject = (JsonObject) jsonConverter.toJsonTree(src);

        jsonObject.remove(Keys.OPENING_TIME);
        jsonObject.remove(Keys.CLOSING_TIME);
        LocalTime openingTime = src.getOpen();
        LocalTime closingTime = src.getClose();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
        String otString = openingTime.format(formatter);
        String ctString = closingTime.format(formatter);
        jsonObject.addProperty(Keys.OPENING_TIME, otString);
        jsonObject.addProperty(Keys.CLOSING_TIME, ctString);

        return jsonObject;
    }

    @Override
    public OpeningTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        OpeningTime openingTime = jsonConverter.fromJson(jsonObject.toString(), OpeningTime.class);

        return openingTime;
    }
}
