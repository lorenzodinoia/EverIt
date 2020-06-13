package it.uniba.di.sms1920.everit.utils.adapter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.threeten.bp.LocalDateTime;

import java.lang.reflect.Type;

import it.uniba.di.sms1920.everit.utils.Address;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;

public class OrderAdapter implements JsonDeserializer<Order> {
    private static final class Keys {
        private static final String ADDRESS_KEY = "delivery_address";
        private static final String LONGITUDE_KEY = "longitude";
        private static final String LATITUDE_KEY = "latitude";
    }

    private static final Gson orderJsonConverter = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat(Adapter.JSON_DATETIME_FORMAT)
            .registerTypeAdapter(boolean.class, new BooleanAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Restaurateur.class, new RestaurateurAdapter())
            .registerTypeAdapter(Adapter.PRODUCT_MAP_TYPE, new ProductMapAdapter())
            .create();

    @Override
    public Order deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String address = jsonObject.get(Keys.ADDRESS_KEY).getAsString();
        jsonObject.remove(Keys.ADDRESS_KEY);
        double latitude = jsonObject.get(Keys.LATITUDE_KEY).getAsDouble();
        jsonObject.remove(Keys.LATITUDE_KEY);
        double longitude = jsonObject.get(Keys.LONGITUDE_KEY).getAsDouble();
        jsonObject.remove(Keys.LONGITUDE_KEY);

        Address deliveryAddress = new Address(latitude, longitude, address);

        Order order = orderJsonConverter.fromJson(jsonObject.toString(), Order.class);
        order.setDeliveryAddress(deliveryAddress);

        return order;
    }
}
