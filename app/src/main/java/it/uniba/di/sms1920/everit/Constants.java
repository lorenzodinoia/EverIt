package it.uniba.di.sms1920.everit;

public final class Constants {
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    public static final String SERVER_HOST = "https://everitsms.000webhostapp.com";
    public static final String PREFS_ENC_FILE_NAME = "EverIt_Credentials";
    public static final String PREFS_FILE_NAME = "EverIt_Preferences";

    public final static class Flavors {
        public static final String CUSTOMER = "customer";
        public static final String RESTAURATEUR = "restaurateur";
        public static final String RIDER = "rider";
    }

    public final static class SharedPreferencesKeys {
        public static final String APP_TOKEN = "app_token";
        public static final String REMEMBER_ME = "remember_me";
    }

    public final static class EncSharedPreferencesKeys {
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
    }
}
