package it.uniba.di.sms1920.everit.request;

import it.uniba.di.sms1920.everit.models.Authenticable;

public final class AuthProvider {
    private static AuthProvider instance = new AuthProvider();
    private String authToken;
    private Authenticable user;

    public static AuthProvider getInstance() {
        return instance;
    }

    String getAuthToken() {
        return authToken;
    }

    void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Authenticable getUser() {
        return user;
    }

    void setUser(Authenticable user) {
        this.user = user;
    }
}
