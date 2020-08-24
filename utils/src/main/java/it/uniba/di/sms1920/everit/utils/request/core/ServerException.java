package it.uniba.di.sms1920.everit.utils.request.core;

import com.android.volley.VolleyError;

public class ServerException extends RequestException {
    public ServerException(VolleyError volleyError, String message) {
        super(volleyError, message);
    }
}
