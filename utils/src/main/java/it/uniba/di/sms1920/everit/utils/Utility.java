package it.uniba.di.sms1920.everit.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public final class Utility {

    //TODO verificare controlli

    /** Account controls */

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile(Constants.PASSWORD_REGEX);
        return pattern.matcher(password).matches();
    }


    /** Customer controls */

    public static boolean isNameValid(String name, TextInputLayout layout, Context context){
        if(name.length() >= 1){
            if(name.length()  <= 50){
                return  true;
            }else {
                layout.setError(context.getString(R.string.error_valueName_long));
                return false;
            }
        }else {
            layout.setError(context.getString(R.string.error_value_short));
            return false;
        }

    }

    public static boolean isSurnameValid(String surname, TextInputLayout layout, Context context){
        if(surname.length() >= 1){
            if(surname.length()  <= 50){
                return  true;
            }else {
                layout.setError(context.getString(R.string.error_valueSurname_long));
                return false;
            }
        }else {
            layout.setError(context.getString(R.string.error_value_short));
            return false;
        }
    }

    public static boolean isPhoneValid(String phone, TextInputLayout layout, Context context){
        if(phone.length() >= 8){
            if(phone.length()  <= 11){
                return  true;
            }else {
                layout.setError(context.getString(R.string.error_value_long));
                return false;
            }
        }else {
            layout.setError(context.getString(R.string.error_value_short));
            return false;
        }
    }



    /** Restaurateur controls */

    public static boolean isShopNameValid(String shopName, TextInputLayout layout, Context context){
        if(shopName.length() >= 1){
            if(shopName.length()  <= 50){
                return  true;
            }else {
                layout.setError(context.getString(R.string.error_valueShopName_long));
                return false;
            }
        }else {
            layout.setError(context.getString(R.string.error_value_short));
            return false;
        }
    }

    public static boolean isVATValid(String vat, TextInputLayout layout, Context context){

        if(vat.length() == 11){
            return true;
        }else {
            layout.setError(context.getString(R.string.error_value_VAT));
            return  false;
        }
    }

    public static boolean isAddressValid(Address address, TextInputLayout layout, Context context){

        if(address != null) {
            if (address.getFullAddress().length() >= 1) {
                return true;
            } else {
                layout.setError(context.getString(R.string.error_value_empty));
                return false;
            }
        }
        else{
            layout.setError(context.getString(R.string.error_value_empty));
            return false;
        }
    }


    public static boolean isDeliveryCostValid(float price, TextInputLayout layout, Context context){
        if(price >= 0){
            if(price  <= 1000){
                return  true;
            }else {
                layout.setError(context.getString(R.string.error_valueDeliveryCost_max));
                return false;
            }
        }else {
            layout.setError(context.getString(R.string.error_value_notValid));
            return false;
        }

    }

    public static boolean isMinPriceValid(float price, TextInputLayout layout, Context context){
        if(price >= 0){
            if(price  <= 1000){
                return  true;
            }else {
                layout.setError(context.getString(R.string.error_valueMinPrice_max));
                return false;
            }
        }else {
            layout.setError(context.getString(R.string.error_value_notValid));
            return false;
        }
    }

    public static boolean isDescriptionValid(String description){

        return description.length() <= 250;
    }

    public static boolean isMaxDeliveryTimeSlot(int maxDeliveryTimeSlot){

        return maxDeliveryTimeSlot > 0;
    }


    public static boolean isValidQuantity(int quantity){

        return quantity > 0 && quantity <= 999;
    }



    /** Products and Categories controls */

    public static boolean isValidCategoryName(String name, TextInputLayout layout, Context context){

        if(name.length() >= 4){
            if(name.length()  <= 20){
                return  true;
            }else {
                layout.setError(context.getString(R.string.error_value_long));
                return false;
            }
        }else {
            layout.setError(context.getString(R.string.error_value_short));
            return false;
        }

    }

    public static boolean isValidProductName(String name, TextInputLayout layout, Context context){

        if(name.length() >= 2){
            if(name.length()  <= 25){
                return  true;
            }else {
                layout.setError(context.getString(R.string.error_value_long));
                return false;
            }
        }else {
            layout.setError(context.getString(R.string.error_value_short));
            return false;
        }

    }

    public static boolean isValidProductDescription(String name, TextInputLayout layout, Context context){
        if(name.length() > 2){
            if(name.length()  <= 50){
                return  true;
            }else {
                layout.setError(context.getString(R.string.error_value_long));
                return false;
            }
        }else {
            layout.setError(context.getString(R.string.error_value_short));
            return false;
        }
    }

    public static boolean isValidProductPrice(float price, TextInputLayout layout, Context context){
        if(price > 0){
            if(price <= 1000){
                return  true;
            }else {
                layout.setError(context.getString(R.string.error_value_long));
                return false;
            }
        }else {
            layout.setError(context.getString(R.string.error_value_short));
            return false;
        }
    }


    /** Cart controls */

    public static boolean isValidOrderNote(String notes, EditText editText, Context context){
        if(notes.length() <=  250){
            return true;
        }else {
            editText.setError(context.getString(R.string.error_orderNotes_maxChar));
            return false;
        }

    }

    public static boolean isValidDeliveryNote(String notes, EditText editText, Context context){
        if(notes.length() <=  250){
            return true;
        }else {
            editText.setError(context.getString(R.string.error_deliveryNotes_maxChar));
            return false;
        }

    }


    /** Reviews controls */

    public static boolean isValidReview(String review, TextInputLayout layout, Context context){

        if(review.length() >= 1){
            if(review.length()  <= 250){
                return  true;
            }else {
                layout.setError(context.getString(R.string.error_valueReview_long));
                return false;
            }
        }else {
            layout.setError(context.getString(R.string.error_value_short));
            return false;
        }

    }


    public static void showGenericMessage(Context context, String title, String message) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView textViewTitle = dialog.findViewById(R.id.textViewTitle);
        textViewTitle.setText(title);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button buttonOk = dialog.findViewById(R.id.btnOk);
        buttonOk.setOnClickListener(v ->{
            dialog.dismiss();
        });

        dialog.show();
    }
}
