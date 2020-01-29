package it.uniba.di.sms1920.everit.request;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public final class AuthRequest extends JsonObjectRequest {

    private String authToken;

    public AuthRequest(String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.POST, url, jsonRequest, listener, errorListener);
    }

    public AuthRequest(String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener, String authToken) {
        super(Method.POST, url, jsonRequest, listener, errorListener);
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

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            JSONObject jsonResponse = new JSONObject(jsonString);
            if(response.headers.containsKey("Authorization")) {
                jsonResponse = jsonResponse.put("token", response.headers.get("Authorization"));
            }
            return Response.success(jsonResponse, HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
