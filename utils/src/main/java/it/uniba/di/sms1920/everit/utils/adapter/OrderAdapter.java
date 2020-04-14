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
    private static final String ORDER_PRODUCTS_KEY = "products";
    private static final String PRODUCT_QUANTITY_KEY = "quantity";

    private static final Gson customGson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(boolean.class, new BooleanAdapter())
            .setDateFormat("yyyy-MM-dd H:mm")
            .create();

    @Override
    public Order deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray productsArray = jsonObject.getAsJsonArray(ORDER_PRODUCTS_KEY);
        jsonObject.remove(ORDER_PRODUCTS_KEY);
        Order order = customGson.fromJson(jsonObject.toString(), Order.class);

        Map<Product, Integer> products = new HashMap<>();
        for (int i = 0; i < productsArray.size(); i++) {
            JsonObject productJson = productsArray.get(i).getAsJsonObject();
            final int quantity = productJson.get(PRODUCT_QUANTITY_KEY).getAsInt();
            productJson.remove(PRODUCT_QUANTITY_KEY);
            final Product product = customGson.fromJson(productJson.toString(), Product.class);
            products.put(product, quantity);
        }
        order.setProducts(products);

        return order;
    }
}
