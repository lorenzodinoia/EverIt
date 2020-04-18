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

import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Product;

public class OrderAdapter implements JsonDeserializer<Order> {
    private static final class Keys {
        private static final String ORDER_PRODUCTS_KEY = "products";
        private static final String PRODUCT_QUANTITY_KEY = "quantity";
    }

    private static final Gson orderJsonConverter = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(boolean.class, new BooleanAdapter())
            .setDateFormat(Adapter.JSON_DATETIME_FORMAT)
            .create();

    @Override
    public Order deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray productsArray = jsonObject.getAsJsonArray(Keys.ORDER_PRODUCTS_KEY);
        jsonObject.remove(Keys.ORDER_PRODUCTS_KEY);
        Order order = orderJsonConverter.fromJson(jsonObject.toString(), Order.class);

        Map<Product, Integer> products = new HashMap<>();
        for (int i = 0; i < productsArray.size(); i++) {
            JsonObject productJson = productsArray.get(i).getAsJsonObject();
            final int quantity = productJson.get(Keys.PRODUCT_QUANTITY_KEY).getAsInt();
            productJson.remove(Keys.PRODUCT_QUANTITY_KEY);
            final Product product = orderJsonConverter.fromJson(productJson.toString(), Product.class);
            products.put(product, quantity);
        }
        order.setProducts(products);

        return order;
    }
}
