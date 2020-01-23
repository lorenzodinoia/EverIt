package it.uniba.di.sms1920.everit.request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public final class RequestManager {
    private static RequestManager instance;
    private RequestQueue requestQueue;
    private String authToken;

    private RequestManager(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new RequestManager(context);
        }
    }

    static RequestManager getInstance() {
        return instance;
    }

    void addToQueue(Request request) {
        requestQueue.add(request);
    }

    String getAuthToken() {
        return authToken;
    }

    void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
