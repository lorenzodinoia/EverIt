package it.uniba.di.sms1920.everit.request;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;


import it.uniba.di.sms1920.everit.BuildConfig;
import it.uniba.di.sms1920.everit.Constants;
import it.uniba.di.sms1920.everit.adapter.Adapter;
import it.uniba.di.sms1920.everit.adapter.AdapterProvider;
import it.uniba.di.sms1920.everit.models.Authenticable;
import it.uniba.di.sms1920.everit.models.Customer;
import it.uniba.di.sms1920.everit.models.Model;
import it.uniba.di.sms1920.everit.models.Restaurateur;
import it.uniba.di.sms1920.everit.models.Rider;

public final class AuthProvider<T extends Model & Authenticable>  {

    private static AuthProvider instance;
    private String authToken;
    private T user;
    private Class<T> type;
    private String serverEndpoint;

    public static AuthProvider getInstance() {
        if(instance == null) {
            AuthProvider.init();
        }
        return instance;
    }

    public static void init() {
        if(BuildConfig.FLAVOR.equals(Constants.FLAVOR_CUSTOMER)) {
            instance = new AuthProvider<Customer>();
            instance.type = Customer.class;
            instance.serverEndpoint = "customer";
        }
        else if(BuildConfig.FLAVOR.equals(Constants.FLAVOR_RESTAURATEUR)) {
            instance = new AuthProvider<Restaurateur>();
            instance.type = Restaurateur.class;
            instance.serverEndpoint = "restaurateur";
        }
        else {
            instance = new AuthProvider<Rider>();
            instance.type = Restaurateur.class;
            instance.serverEndpoint = "rider";
        }
    }

    String getAuthToken() {
        return authToken;
    }

    private void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public T getUser() {
        return user;
    }

    private void setUser(@Nullable T user) {
        this.user = user;
    }

    private void removeUserData() {
        this.user = null;
        this.authToken = null;
    }

    public void login(String email, String password, RequestListener<Boolean> requestListener) {
        try {
            JSONObject json = new JSONObject();
            json = json.put("email", email);
            json =json.put("password", password);

            AuthRequest request = new AuthRequest(String.format("%s/api/%s/login", Constants.SERVER_HOST, instance.serverEndpoint), json,
                response -> {
                    Adapter<T> adapter = AdapterProvider.getAdapterFor(type);
                    try {
                        String token = response.getString("Authorization");
                        this.setAuthToken(token);
                        response.remove("Authorization");
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AuthProvider.getInstance().setUser(adapter.fromJSON(response, type));
                    requestListener.successResponse(true);
                },
                error -> {
                    if(error.networkResponse.statusCode != 401){
                        requestListener.errorResponse("Server Error");
                    }
                    else{
                        requestListener.errorResponse("Wrong email or password");
                    }
                });

            RequestManager.getInstance().addToQueue(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void logout(RequestListener<Boolean> requestListener) {
        AuthRequest request = new AuthRequest(String.format("%s/api/%s/logout", Constants.SERVER_HOST, instance.serverEndpoint), null,
            response -> {
                this.removeUserData();
                requestListener.successResponse(true);
            },
            error -> {
                if(error.networkResponse.statusCode != 401) {
                    requestListener.errorResponse("Server Error");
                }
                else {
                    requestListener.errorResponse("Unauthorized");
                }
            }, this.authToken);
        RequestManager.getInstance().addToQueue(request);
    }
}
