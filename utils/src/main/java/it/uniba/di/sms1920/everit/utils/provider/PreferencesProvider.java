package it.uniba.di.sms1920.everit.utils.provider;

import android.content.Context;
import android.content.SharedPreferences;

public final class PreferencesProvider {
    private static final String PREFS_FILE_NAME = "EverIt_Preferences";
    private final static class Keys {
        private static final String FIREBASE_TOKEN = "app_token";
    }

    private SharedPreferences sharedPreferences;

    PreferencesProvider(Context applicationContext) {
        this.sharedPreferences = applicationContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    void saveFirebaseToken(String firebaseToken) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(Keys.FIREBASE_TOKEN, firebaseToken);
        editor.apply();
    }

    String loadFirebaseToken() {
        return this.sharedPreferences.getString(Keys.FIREBASE_TOKEN, null);
    }

    void removeFirebaseToken() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.remove(Keys.FIREBASE_TOKEN);
        editor.apply();
    }
}
