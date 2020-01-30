package it.uniba.di.sms1920.everit.credentials;

import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;

import it.uniba.di.sms1920.everit.Constants;
import it.uniba.di.sms1920.everit.PreferencesManager;

public final class AppToken {
    private static String appToken;

    public static String getAppToken() {
        String newAppToken = FirebaseInstanceId.getInstance().getToken();
        String savedAppToken = loadAppToken();

        if (savedAppToken == null || (!savedAppToken.equals(newAppToken))) {
            saveAppToken(newAppToken);
            appToken = newAppToken;
        }
        else {
            appToken = savedAppToken;
        }

        return appToken;
    }

    public static void removeAppToken() {
        appToken = null;
        SharedPreferences sharedPref = PreferencesManager.getInstance().getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.SharedPreferencesKeys.APP_TOKEN);
        editor.apply();
    }

    private static void saveAppToken(String appToken) {
        SharedPreferences sharedPref = PreferencesManager.getInstance().getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.SharedPreferencesKeys.APP_TOKEN, appToken);
        editor.apply();
    }

    private static String loadAppToken() {
        SharedPreferences sharedPref = PreferencesManager.getInstance().getSharedPreferences();
        return sharedPref.getString(Constants.SharedPreferencesKeys.APP_TOKEN, null);
    }
}
