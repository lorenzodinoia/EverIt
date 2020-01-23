package it.uniba.di.sms1920.everit.adapter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import it.uniba.di.sms1920.everit.models.Model;

public class Adapter<T extends Model> {
    private static final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    public JSONObject toJSON(T object) throws JSONException {
        return new JSONObject(gson.toJson(object));
    }

    public T fromJSON(JSONObject json, Type classType) {
        return gson.fromJson(json.toString(), classType);
    }
}
