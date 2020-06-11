package it.uniba.di.sms1920.everit.utils.adapter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import it.uniba.di.sms1920.everit.utils.models.Product;

public class ProductAdapter implements JsonSerializer<Product> {

    private static final class Keys{
        private static final String PRODUCT_CATEGORY_KEY = "product_category_id";
    }

    private final static Gson productConverter = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    @Override
    public JsonElement serialize(Product src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObject = (JsonObject) productConverter.toJsonTree(src);

        jsonObject.addProperty(Keys.PRODUCT_CATEGORY_KEY, src.getCategory().getId());

        return jsonObject;
    }
}
