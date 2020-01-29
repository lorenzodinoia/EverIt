package it.uniba.di.sms1920.everit.request;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class ObjectRequest extends JsonObjectRequest {

    private String authToken;

    ObjectRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    ObjectRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener, String authToken) {
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
