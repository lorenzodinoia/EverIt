package it.uniba.di.sms1920.everit;

import android.util.Patterns;

import java.util.regex.Pattern;

public final class Utility {
    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile(Constants.PASSWORD_REGEX);
        return pattern.matcher(password).matches();
    }
}
