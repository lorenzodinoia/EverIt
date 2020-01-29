package it.uniba.di.sms1920.everit;

import android.content.Context;
import android.content.SharedPreferences;

public final class PreferencesManager {
    private static PreferencesManager instance;
    private Context context;

    private PreferencesManager(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
    }

    public static PreferencesManager getInstance() {
        return instance;
    }

    public boolean saveAppToken(String appToken) {
        SharedPreferences sharedPref = this.context.getSharedPreferences(Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.PREFS_APP_TOKEN_KEY, appToken);
        return editor.commit();
    }

    public String loadAppToken() {
        SharedPreferences sharedPref = this.context.getSharedPreferences(Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.PREFS_APP_TOKEN_KEY, null);
    }
}
