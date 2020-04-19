package it.uniba.di.sms1920.everit.utils.request.core;

import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public final class AuthRequest extends ObjectRequest {
    public AuthRequest(String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.POST, url, jsonRequest, listener, errorListener);
    }

    public AuthRequest(String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener, String authToken) {
        super(Method.POST, url, jsonRequest, listener, errorListener, authToken);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            JSONObject jsonResponse = new JSONObject(jsonString);
            Log.d("HEAD", response.headers.toString());
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
