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

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import it.uniba.di.sms1920.everit.utils.models.Product;

public class ProductMapAdapter implements JsonDeserializer<Map<Product, Integer>> {
    private static final class Keys {
        private static final String PRODUCT_QUANTITY_KEY = "quantity";
    }

    private static final Gson jsonConverter = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat(Adapter.JSON_DATETIME_FORMAT)
            .create();

    @Override
    public Map<Product, Integer> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray productsJsonArray = json.getAsJsonArray();
        Map<Product, Integer> products = new HashMap<>();

        for (JsonElement jsonElement : productsJsonArray) {
            JsonObject productJson = jsonElement.getAsJsonObject();
            int quantity = productJson.get(Keys.PRODUCT_QUANTITY_KEY).getAsInt();
            Product product = jsonConverter.fromJson(productJson.toString(), Product.class);

            products.put(product, quantity);
        }

        return products;
    }
}
