package it.uniba.di.sms1920.everit.utils.provider;

import android.content.Context;

import java.io.IOException;
import java.security.GeneralSecurityException;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.Rider;

public final class Providers {
    private static Context applicationContext;
    private static PreferencesProvider preferencesProvider;
    private static CredentialProvider credentialProvider;
    private static FirebaseTokenProvider firebaseTokenProvider;
    private static AuthProvider authProvider;
    private static RequestProvider requestProvider;

    public static void init(Context applicationContext, Constants.Variants variant) throws Exception {
        Providers.applicationContext = applicationContext;
        preferencesProvider = new PreferencesProvider(applicationContext);
        try {
            credentialProvider = new CredentialProvider(applicationContext);
        }
        catch (GeneralSecurityException | IOException e) {
            throw new Exception(e.getMessage());
        }
        firebaseTokenProvider = new FirebaseTokenProvider();
        switch (variant) {
            case CUSTOMER:
                authProvider = new AuthProvider<Customer>(Customer.class);
                break;
            case RIDER:
                authProvider = new AuthProvider<Rider>(Rider.class);
                break;
            case RESTAURATEUR:
                authProvider = new AuthProvider<Restaurateur>(Restaurateur.class);
                break;
            default:
                throw new Exception();
        }
        requestProvider = new RequestProvider(applicationContext);
    }

    public static PreferencesProvider getPreferencesProvider() {
        return preferencesProvider;
    }

    public static CredentialProvider getCredentialProvider() {
        return credentialProvider;
    }

    public static FirebaseTokenProvider getFirebaseTokenProvider() {
        return firebaseTokenProvider;
    }

    public static AuthProvider getAuthProvider() {
        return authProvider;
    }

    public static RequestProvider getRequestProvider() {
        return requestProvider;
    }

    public static String getStringFromApplicationContext(int stringId) {
        return applicationContext.getString(stringId);
    }
}