package it.uniba.di.sms1920.everit.utils.request;

import org.json.JSONException;
import org.json.JSONObject;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.R;
import it.uniba.di.sms1920.everit.utils.adapter.Adapter;
import it.uniba.di.sms1920.everit.utils.provider.AdapterProvider;
import it.uniba.di.sms1920.everit.utils.models.Authenticable;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.models.Model;
import it.uniba.di.sms1920.everit.utils.models.Rider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.AuthRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestExceptionFactory;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import it.uniba.di.sms1920.everit.utils.request.core.UnauthorizedException;

public final class AccessRequest<T extends Model & Authenticable> {
    private String serverEndpoint;
    private Class<T> userType;

    public AccessRequest(Class<T> userType) {
        if (userType == Customer.class) {
            this.serverEndpoint = "customer";
        }
        else if (userType == Rider.class) {
            this.serverEndpoint = "rider";
        }
        else {
            this.serverEndpoint = "restaurateur";
        }
        this.userType = userType;
    }

    public void login(String email, String password, RequestListener<T> RequestListener) {
        try {
            String appToken = Providers.getFirebaseTokenProvider().getFirebaseToken();
            JSONObject json = new JSONObject();
            json = json.put("email", email);
            json =json.put("password", password);
            if (appToken != null) {
                json.put("device_id", appToken);
            }

            AuthRequest request = new AuthRequest(String.format("%s/api/%s/login", Constants.SERVER_HOST, this.serverEndpoint), json,
                response -> {
                    Adapter<T> adapter = AdapterProvider.getAdapterFor(userType);
                    String token = "";
                    try {
                        token = response.getString("token");
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    response.remove("Authorization");
                    T user = adapter.fromJSON(response, userType);
                    Providers.getAuthProvider().setAuthToken(token);
                    RequestListener.successResponse(user);
                },
                error -> {
                    if((error.networkResponse != null) && (error.networkResponse.statusCode == 401)){
                        String translatedMessage = Providers.getStringFromApplicationContext(R.string.message_wrong_login);
                        RequestListener.errorResponse(new UnauthorizedException(error, translatedMessage));
                    }
                    else {
                        RequestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                    }
                });

            Providers.getRequestProvider().addToQueue(request);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void logout(RequestListener<Boolean> RequestListener) {
        AuthRequest request = new AuthRequest(String.format("%s/api/%s/logout", Constants.SERVER_HOST, this.serverEndpoint), null,
            response -> {
                RequestListener.successResponse(true);
            },
            error -> {
                RequestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
            }, Providers.getAuthProvider().getAuthToken());

        Providers.getRequestProvider().addToQueue(request);
    }
}
