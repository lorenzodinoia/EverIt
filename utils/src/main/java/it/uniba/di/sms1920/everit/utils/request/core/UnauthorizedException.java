package it.uniba.di.sms1920.everit.utils.request.core;

import com.android.volley.VolleyError;

public class UnauthorizedException extends RequestException {
    public UnauthorizedException(VolleyError volleyError, String message) {
        super(volleyError, message);
    }
}
