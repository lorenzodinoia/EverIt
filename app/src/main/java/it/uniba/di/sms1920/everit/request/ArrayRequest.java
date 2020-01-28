package it.uniba.di.sms1920.everit.request;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public final class ArrayRequest extends JsonArrayRequest {
    private String authToken;

    public ArrayRequest(int method, String url, @Nullable JSONArray jsonRequest, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public ArrayRequest(int method, String url, @Nullable JSONArray jsonRequest, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener, String authToken) {
        super(method, url, jsonRequest, listener, errorListener);
        this.authToken = authToken;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap<>();
        map.put("Expect", "application/json");
        map.put("Accept", "application/json");
        map.put("Content-Type", "application/json");
        if(authToken != null){
            map.put("Authorization", String.format("Bearer %s", authToken));
        }
        return map;
    }
}
