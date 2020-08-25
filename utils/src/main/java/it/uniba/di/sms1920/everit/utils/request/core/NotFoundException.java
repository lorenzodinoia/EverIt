package it.uniba.di.sms1920.everit.utils.request.core;

import com.android.volley.VolleyError;

public class NotFoundException extends RequestException {
    public NotFoundException(VolleyError volleyError, String message) {
        super(volleyError, message);
    }
}
