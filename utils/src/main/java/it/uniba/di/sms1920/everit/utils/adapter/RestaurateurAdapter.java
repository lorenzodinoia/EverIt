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

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.lang.reflect.Type;

import it.uniba.di.sms1920.everit.utils.Address;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;

public class RestaurateurAdapter implements JsonDeserializer<Restaurateur>, JsonSerializer<Restaurateur> {

    private static final class Keys {
        private static final String ADDRESS_KEY = "address";
        private static final String LONGITUDE_KEY = "longitude";
        private static final String LATITUDE_KEY = "latitude";
        private static final String SHOP_TYPE_KEY = "shop_type_id";
        private static final String OPENING_TIMES_KEY = "opening_times";
    }

    private static final Gson restaurateurJsonConverter = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat(Adapter.JSON_DATETIME_FORMAT)
            .registerTypeAdapter(boolean.class, new BooleanAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();

    @Override
    public JsonElement serialize(Restaurateur src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObject = (JsonObject) restaurateurJsonConverter.toJsonTree(src);

        jsonObject.remove(Keys.ADDRESS_KEY);

        jsonObject.addProperty(Keys.ADDRESS_KEY, src.getAddress().getFullAddress());
        jsonObject.addProperty(Keys.LATITUDE_KEY, src.getAddress().getLatitude());
        jsonObject.addProperty(Keys.LONGITUDE_KEY, src.getAddress().getLongitude());
        jsonObject.addProperty(Keys.SHOP_TYPE_KEY, src.getShopType().getId());


        return jsonObject;
    }

    @Override
    public Restaurateur deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String address = jsonObject.get(Keys.ADDRESS_KEY).getAsString();
        jsonObject.remove(Keys.ADDRESS_KEY);
        double latitude = jsonObject.get(Keys.LATITUDE_KEY).getAsDouble();
        jsonObject.remove(Keys.LATITUDE_KEY);
        double longitude = jsonObject.get(Keys.LONGITUDE_KEY).getAsDouble();
        jsonObject.remove(Keys.LONGITUDE_KEY);

        Address restaurateurAddress = new Address(latitude, longitude, address);

        Restaurateur restaurateur = restaurateurJsonConverter.fromJson(jsonObject.toString(), Restaurateur.class);
        restaurateur.setAddress(restaurateurAddress);

        return restaurateur;
    }
}
