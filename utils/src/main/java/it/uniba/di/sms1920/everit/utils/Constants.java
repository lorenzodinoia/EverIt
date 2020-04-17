package it.uniba.di.sms1920.everit.utils;

public final class Constants {
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    public static final String SERVER_HOST = "https://everitsms.000webhostapp.com";
    public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm";

    public enum Variants {
        CUSTOMER, RIDER, RESTAURATEUR
    }
}
