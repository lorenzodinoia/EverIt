package it.uniba.di.sms1920.everit.utils.adapter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.threeten.bp.LocalTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.uniba.di.sms1920.everit.utils.models.OpeningDay;
import it.uniba.di.sms1920.everit.utils.models.OpeningTime;

public class OpeningDayArrayAdapter implements JsonDeserializer<List<OpeningDay>> {
    private static final class Keys {
        private static final String OPENING_DAY_KEY = "opening_day";
    }

    private static final Gson jsonConverter = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .setDateFormat(Adapter.JSON_DATETIME_FORMAT)
            .create();

    @Override
    public List<OpeningDay> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray openingTimesJsonArray = json.getAsJsonArray();

        List<OpeningDay> openingDays = new ArrayList<>();

        for (JsonElement jsonElement : openingTimesJsonArray) {
            JsonObject openingTimeJson = jsonElement.getAsJsonObject();
            OpeningTime openingTime = jsonConverter.fromJson(openingTimeJson.toString(), OpeningTime.class);

            if (openingTimeJson.has(Keys.OPENING_DAY_KEY)) {
                JsonObject jsonDay = openingTimeJson.getAsJsonObject(Keys.OPENING_DAY_KEY);
                OpeningDay openingDay = jsonConverter.fromJson(jsonDay.toString(), OpeningDay.class);

                boolean toAdd = true;
                if (openingDays.contains(openingDay)) {
                    int existingDayIndex = openingDays.indexOf(openingDay);
                    openingDay = openingDays.get(existingDayIndex);
                    toAdd = false;
                }
                if (openingDay.getOpeningTimes() == null) {
                    openingDay.setOpeningTimes(new ArrayList<>());
                }
                openingDay.getOpeningTimes().add(openingTime);
                if (toAdd) {
                    openingDays.add(openingDay);
                }

                openingDay.sortOpeningTimes(); //Ordina orari di apertura
            }
        }

        Collections.sort(openingDays); //Ordina giorni dal Lunedì alla Domenica

        return openingDays;
    }
}
