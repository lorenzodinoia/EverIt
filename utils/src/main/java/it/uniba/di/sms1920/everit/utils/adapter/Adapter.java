package it.uniba.di.sms1920.everit.utils.adapter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import it.uniba.di.sms1920.everit.utils.models.Model;
import it.uniba.di.sms1920.everit.utils.models.OpeningDay;
import it.uniba.di.sms1920.everit.utils.models.Product;

public class Adapter<T extends Model> {
    static final String JSON_DATETIME_FORMAT =  "yyyy-MM-dd H:mm";

    private static final Type openingDayArrayType = new TypeToken<Collection<OpeningDay>>() {}.getType();
    private static final Type productMapType = new TypeToken<Map<Product, Integer>>() {}.getType();

    private static final Gson jsonConverter = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(boolean.class, new BooleanAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(productMapType, new ProductMapAdapter())
            .registerTypeAdapter(openingDayArrayType, new OpeningDayArrayAdapter())
            .setDateFormat(JSON_DATETIME_FORMAT)
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
