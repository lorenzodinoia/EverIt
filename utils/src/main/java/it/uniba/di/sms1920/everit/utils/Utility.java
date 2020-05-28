package it.uniba.di.sms1920.everit.utils;

import android.util.Log;
import android.util.Patterns;

import java.util.regex.Pattern;

public final class Utility {

    //TODO verificare controlli

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile(Constants.PASSWORD_REGEX);
        return pattern.matcher(password).matches();
    }

    public static boolean isNameValid(String name){
        return name.length() > 1 && name.length() < 50;
    }

    public static boolean isSurnameValid(String surname){
        if(surname.length() > 1 && surname.length() < 50){
            return true;
        }

        return false;
    }

    public static boolean isPhoneValid(String phone){
        if(phone.length() > 8 && phone.length() < 11){
            return true;
        }

        return false;
    }


}
