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
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;

public class OrderAdapter implements JsonSerializer<Order>, JsonDeserializer<Order> {

    private static final class Keys {
        private static final String ADDRESS_KEY = "delivery_address";
        private static final String LONGITUDE_KEY = "longitude";
        private static final String LATITUDE_KEY = "latitude";
        private static final String STATUS = "status";
        private static final String LATE = "late";
    }

    private static final Gson orderJsonConverter = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat(Adapter.JSON_DATETIME_FORMAT)
            .registerTypeAdapter(boolean.class, new BooleanAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(Restaurateur.class, new RestaurateurAdapter())
            .registerTypeAdapter(Adapter.PRODUCT_MAP_TYPE, new ProductMapAdapter())
            .enableComplexMapKeySerialization()
            .create();

    @Override
    public JsonElement serialize(Order src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObject = (JsonObject) orderJsonConverter.toJsonTree(src);

        jsonObject.remove(Keys.ADDRESS_KEY);

        String address = src.getDeliveryAddress().getFullAddress();
        double latitude = src.getDeliveryAddress().getLatitude();
        double longitude = src.getDeliveryAddress().getLongitude();

        jsonObject.addProperty(Keys.ADDRESS_KEY, address);
        jsonObject.addProperty(Keys.LATITUDE_KEY, latitude);
        jsonObject.addProperty(Keys.LONGITUDE_KEY, longitude);

        return jsonObject;
    }

    @Override
    public Order deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String address = jsonObject.get(Keys.ADDRESS_KEY).getAsString();
        jsonObject.remove(Keys.ADDRESS_KEY);
        double latitude = jsonObject.get(Keys.LATITUDE_KEY).getAsDouble();
        jsonObject.remove(Keys.LATITUDE_KEY);
        double longitude = jsonObject.get(Keys.LONGITUDE_KEY).getAsDouble();
        jsonObject.remove(Keys.LONGITUDE_KEY);
        int status = jsonObject.get(Keys.STATUS).getAsInt();
        jsonObject.remove(Keys.STATUS);

        boolean late = jsonObject.get(Keys.LATE).getAsBoolean();
        jsonObject.remove(Keys.LATE);


        Address deliveryAddress = new Address(latitude, longitude, address);
        Order.Status newStatus = null;

        switch (status){
            case 0 :
                newStatus = Order.Status.ORDERED;
                break;
            case 1:
                newStatus = Order.Status.IN_PROGRESS;
                break;
            case 2:
                newStatus = Order.Status.DELIVERING;
                break;
            case 3:
                newStatus = Order.Status.DELIVERED;
                break;
        }

        Order order = orderJsonConverter.fromJson(jsonObject.toString(), Order.class);
        order.setDeliveryAddress(deliveryAddress);
        order.setStatus(newStatus);
        order.setLate(late);

        return order;
    }
}
