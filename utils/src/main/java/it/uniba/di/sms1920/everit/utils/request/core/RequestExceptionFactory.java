package it.uniba.di.sms1920.everit.utils.request.core;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import it.uniba.di.sms1920.everit.utils.R;
import it.uniba.di.sms1920.everit.utils.provider.Providers;

public final class RequestExceptionFactory {
    private static final String DEFAULT_MESSAGE_KEY = "message";

    public static RequestException createExceptionFromError(VolleyError error) {
        if ((error instanceof TimeoutError) || (error instanceof NoConnectionError)) {
            return new ServerUnreachableException(Providers.getStringFromApplicationContext(R.string.message_server_unreachable));
        }

        int errorCode = (error.networkResponse != null) ? error.networkResponse.statusCode : 0;
        String translatedMessage = "";

        switch (errorCode) {
            case 404:
                translatedMessage = Providers.getStringFromApplicationContext(R.string.message_404);
                return new NotFoundException(translatedMessage);
            case 401:
                translatedMessage = Providers.getStringFromApplicationContext(R.string.message_401);
                return new UnauthorizedException(translatedMessage);
            case 500:
                translatedMessage = Providers.getStringFromApplicationContext(R.string.message_500);
                return new ServerException(translatedMessage);
            default:
                String errorMessage = "";

                try {
                    String errorBody = (error.networkResponse != null) ? new String(error.networkResponse.data, StandardCharsets.UTF_8) : null;
                    if ((errorBody == null) || (errorBody.equals(""))) {
                        throw new JSONException("No JSON");
                    }
                    JSONObject jsonResponse = new JSONObject(errorBody);
                    if (jsonResponse.has(DEFAULT_MESSAGE_KEY)) {
                        errorMessage = jsonResponse.getString(DEFAULT_MESSAGE_KEY);
                    }
                }
                catch (Exception e) {
                    errorMessage = Providers.getStringFromApplicationContext(R.string.message_generic_error);
                }

                return new RequestException(errorMessage);
        }
    }
}

