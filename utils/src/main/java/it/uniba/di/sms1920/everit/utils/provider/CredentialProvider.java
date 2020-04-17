package it.uniba.di.sms1920.everit.utils.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public final class CredentialProvider {
    public static class Credential {
        private String email;
        private String password;

        public Credential(String email, String password) {
            this.email = email;
            this.password = password;
        }

        String getEmail() {
            return email;
        }

        String getPassword() {
            return password;
        }
    }

    private static final KeyGenParameterSpec KEY_GEN_PARAMETER_SPEC = MasterKeys.AES256_GCM_SPEC;
    private static final String PREFS_ENC_FILE_NAME = "EverIt_Credentials";
    private final static class Keys {
        private static final String EMAIL = "email";
        private static final String PASSWORD = "password";
    }

    private EncryptedSharedPreferences encryptedPreferences;

    CredentialProvider(Context applicationContext) throws GeneralSecurityException, IOException {
        String masterKeyAlias = MasterKeys.getOrCreate(KEY_GEN_PARAMETER_SPEC);
        this.encryptedPreferences = (EncryptedSharedPreferences) EncryptedSharedPreferences
                .create(
                        PREFS_ENC_FILE_NAME,
                        masterKeyAlias,
                        applicationContext,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );
    }

    boolean saveCredential(Credential credentials) {
        SharedPreferences.Editor editor = this.encryptedPreferences.edit();
        editor.putString(Keys.EMAIL, credentials.getEmail());
        editor.putString(Keys.PASSWORD, credentials.getPassword());
        return editor.commit();
    }

    Credential loadCredential() {
        Credential credentials = null;
        final String email = this.encryptedPreferences.getString(Keys.EMAIL, null);
        final String password = this.encryptedPreferences.getString(Keys.PASSWORD, null);

        if (email != null && password != null) {
            credentials = new Credential(email, password);
        }

        return credentials;
    }

    void removeCredential() {
        SharedPreferences.Editor editor = this.encryptedPreferences.edit();
        editor.remove(Keys.EMAIL);
        editor.remove(Keys.PASSWORD);
        editor.apply();
    }
}
