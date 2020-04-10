package it.uniba.di.sms1920.everit.utils.credentials;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

import it.uniba.di.sms1920.everit.utils.Constants;

//TODO Fræ controlla che non si sovvrascrivono i cazzi

public final class CredentialsManager {
    private static final KeyGenParameterSpec KEY_GEN_PARAMETER_SPEC = MasterKeys.AES256_GCM_SPEC;
    private static CredentialsManager instance;
    private Context context;
    private EncryptedSharedPreferences encryptedPreferences;

    private CredentialsManager(Context context) throws GeneralSecurityException, IOException {
        this.context = context;
        String masterKeyAlias = MasterKeys.getOrCreate(KEY_GEN_PARAMETER_SPEC);
        this.encryptedPreferences = (EncryptedSharedPreferences) EncryptedSharedPreferences
                .create(
                        Constants.PREFS_ENC_FILE_NAME,
                        masterKeyAlias,
                        this.context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );
    }

    public static void init(Context context) throws GeneralSecurityException, IOException {
        if (instance == null) {
            instance = new CredentialsManager(context);
        }
    }

    public static CredentialsManager getInstance() {
        return instance;
    }

    public boolean saveCredentials(Credentials credentials) {
        SharedPreferences.Editor editor = this.encryptedPreferences.edit();
        editor.putString(Constants.EncSharedPreferencesKeys.EMAIL, credentials.getEmail());
        editor.putString(Constants.EncSharedPreferencesKeys.PASSWORD, credentials.getPassword());
        return editor.commit();
    }

    public Credentials loadCredentials() {
        Credentials credentials = null;
        final String email = this.encryptedPreferences.getString(Constants.EncSharedPreferencesKeys.EMAIL, null);
        final String password = this.encryptedPreferences.getString(Constants.EncSharedPreferencesKeys.PASSWORD, null);

        if (email != null && password != null) {
            credentials = new Credentials(email, password);
        }

        return credentials;
    }

    public void removeCredentials() {
        SharedPreferences.Editor editor = this.encryptedPreferences.edit();
        editor.remove(Constants.EncSharedPreferencesKeys.EMAIL);
        editor.remove(Constants.EncSharedPreferencesKeys.PASSWORD);
        editor.apply();
    }
}
