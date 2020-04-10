package it.uniba.di.sms1920.everit.utils.request;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.PreferencesManager;
import it.uniba.di.sms1920.everit.utils.adapter.Adapter;
import it.uniba.di.sms1920.everit.utils.adapter.AdapterProvider;
import it.uniba.di.sms1920.everit.utils.credentials.AppToken;
import it.uniba.di.sms1920.everit.utils.credentials.Credentials;
import it.uniba.di.sms1920.everit.utils.credentials.CredentialsManager;
import it.uniba.di.sms1920.everit.utils.models.Authenticable;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.models.Model;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.Rider;

public final class AuthProvider<T extends Model & Authenticable>  {

    private static AuthProvider instance;
    private String authToken;
    private T user;
    private Class<T> type;
    private String serverEndpoint;

    public static AuthProvider getInstance() {
        return instance;
    }

    public static void init(Constants.Variants variant) {
        if(variant == Constants.Variants.CUSTOMER) {
            instance = new AuthProvider<Customer>();
            instance.type = Customer.class;
            instance.serverEndpoint = "customer";
        }
        else if(variant == Constants.Variants.RESTAURATEUR) {
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
        AppToken.removeAppToken();
        CredentialsManager.getInstance().removeCredentials();
    }

    public boolean loginFromSavedCredentials(RequestListener<Boolean> RequestListener) {
        Credentials credentials = CredentialsManager.getInstance().loadCredentials();
        boolean success;

        if (credentials != null) {
            this.login(credentials.getEmail(), credentials.getPassword(), RequestListener, false);
            success = true;
        }
        else {
            //TODO Lanciare eccezione
            success = false;
        }

        return success;
    }

    public void login(String email, String password, RequestListener<Boolean> RequestListener, boolean rememberMe) {
        try {
            String appToken = AppToken.getAppToken();
            JSONObject json = new JSONObject();
            json = json.put("email", email);
            json =json.put("password", password);
            if (appToken != null) {
                json.put("device_id", appToken);
            }

            AuthRequest request = new AuthRequest(String.format("%s/api/%s/login", Constants.SERVER_HOST, instance.serverEndpoint), json,
                response -> {
                    Adapter<T> adapter = AdapterProvider.getAdapterFor(type);

                    try {
                        String token = response.getString("token");
                        this.setAuthToken(token);
                        if (rememberMe) {
                            if (!CredentialsManager.getInstance().saveCredentials(new Credentials(email, password))) {
                                //TODO Avvisare nel caso non si possano salvare le credenziali
                            }
                        }
                        PreferencesManager.getInstance().setRememberMe(rememberMe);
                        response.remove("Authorization");
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                    AuthProvider.instance.setUser(adapter.fromJSON(response, type));
                    RequestListener.successResponse(true);
                },
                error -> {
                    //TODO Sostituire con messaggi tradotti
                    if(error.networkResponse.statusCode != 401){
                        RequestListener.errorResponse("Server error");
                    }
                    else{
                        RequestListener.errorResponse("Wrong email or password");
                    }
                });

            RequestManager.getInstance().addToQueue(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void logout(RequestListener<Boolean> RequestListener) {
        AuthRequest request = new AuthRequest(String.format("%s/api/%s/logout", Constants.SERVER_HOST, instance.serverEndpoint), null,
            response -> {
                this.removeUserData();
                RequestListener.successResponse(true);
            },
            error -> {
                //TODO Sostituire con messaggi tradotti
                if(error.networkResponse.statusCode != 401) {
                    RequestListener.errorResponse("Server Error");
                }
                else {
                    RequestListener.errorResponse("Unauthorized");
                }
            }, this.authToken);
        RequestManager.getInstance().addToQueue(request);
    }
}
