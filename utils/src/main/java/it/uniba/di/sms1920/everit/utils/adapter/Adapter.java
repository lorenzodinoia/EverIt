package it.uniba.di.sms1920.everit.utils.adapter;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDateTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import it.uniba.di.sms1920.everit.utils.models.Model;
import it.uniba.di.sms1920.everit.utils.models.OpeningDay;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;

public class Adapter<T extends Model> {
    static final String JSON_DATETIME_FORMAT =  "yyyy-MM-dd H:mm";
    static final Type OPENING_DAY_ARRAY_TYPE = new TypeToken<Collection<OpeningDay>>() {}.getType();
    static final Type PRODUCT_MAP_TYPE = new TypeToken<Map<Product, Integer>>() {}.getType();

    private static final Gson jsonConverter = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat(JSON_DATETIME_FORMAT)
            .registerTypeAdapter(boolean.class, new BooleanAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(Order.class, new OrderAdapter())
            .registerTypeAdapter(Restaurateur.class, new RestaurateurAdapter())
            .registerTypeAdapter(PRODUCT_MAP_TYPE, new ProductMapAdapter())
            .registerTypeAdapter(OPENING_DAY_ARRAY_TYPE, new OpeningDayArrayAdapter())
            .registerTypeAdapter(Product.class, new ProductAdapter())
            .create();

    public JSONObject toJSON(T object) throws JSONException {
            return new JSONObject(jsonConverter.toJson(object));
    }

    public T fromJSON(JSONObject json, Type classType) {
        return jsonConverter.fromJson(json.toString(), classType);
    }

    public Collection<T> fromJSONArray(JSONArray json, Type classType) throws JSONException{
        Collection<T> collection = new ArrayList<>();

        for(int i = 0; i < json.length(); i++) {
            JSONObject object = json.getJSONObject(i);
            collection.add(jsonConverter.fromJson(object.toString(), classType));
        }

        return collection;
    }
}
