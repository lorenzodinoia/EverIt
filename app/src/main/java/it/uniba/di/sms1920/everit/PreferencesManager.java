package it.uniba.di.sms1920.everit;

import android.content.Context;
import android.content.SharedPreferences;

public final class PreferencesManager {
    private static PreferencesManager instance;
    private SharedPreferences  sharedPreferences;
    private Context context;

    private PreferencesManager(Context context) {
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences(Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
    }

    public static PreferencesManager getInstance() {
        return instance;
    }

    public final SharedPreferences getSharedPreferences() {
        return this.sharedPreferences;
    }

    public final void setRememberMe(boolean rememberMe) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(Constants.SharedPreferencesKeys.REMEMBER_ME, rememberMe);
        editor.apply();
    }

    public final boolean getRemeberMe() {
        return this.sharedPreferences.getBoolean(Constants.SharedPreferencesKeys.REMEMBER_ME, false);
    }
}
