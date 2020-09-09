package it.uniba.di.sms1920.everit.rider.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.InvalidPropertiesFormatException;
import java.util.Objects;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Rider;
import it.uniba.di.sms1920.everit.utils.request.RiderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class SignUpActivity extends AppCompatActivity {

    private ScrollView scrollViewSignUp;
    private CardView cardViewHintPassword;

    private TextInputEditText editTextName;
    private TextInputEditText editTextSurname;
    private TextInputEditText editTextPhoneNumber;
    private TextInputEditText editTextMail;
    private TextInputEditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(it.uniba.di.sms1920.everit.utils.R.layout.activity_sign_up);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*
        if(savedInstanceState == null) {
            this.initComponents();
        }

         */
        this.initComponents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initComponents() {
        scrollViewSignUp = findViewById(R.id.scrollViewSignUp);
        cardViewHintPassword = findViewById(R.id.cardViewHintPassword);

        TextInputLayout editTextMailContainer = findViewById(R.id.editTextMailContainer);
        editTextMail = findViewById(R.id.editTextMail);
        editTextMail.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                focusOnView(editTextMail);
            }
        });

        TextInputLayout editTextPasswordContainer = findViewById(R.id.editTextPasswordContainer);
        editTextPassword = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextPassword);
        editTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                cardViewHintPassword.setVisibility(View.VISIBLE);
                focusOnView(editTextPassword);
            }
            else{
                cardViewHintPassword.setVisibility(View.GONE);
            }
        });

        TextInputLayout editTextPhoneContainer = findViewById(R.id.editTextPhoneContainer);
        editTextPhoneNumber = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextPhone);
        editTextPhoneNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                focusOnView(editTextPhoneNumber);
            }
        });

        TextInputLayout editTextNameContainer = findViewById(R.id.editTextNameContainer);
        editTextName = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextName);
        editTextName.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                focusOnView(editTextName);
            }
        });

        TextInputLayout editTextSurnameContainer = findViewById(R.id.editTextSurnameContainer);
        editTextSurname = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextSurname);
        editTextSurname.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                focusOnView(editTextSurname);
            }
        });

        MaterialButton buttonLogin = this.findViewById(it.uniba.di.sms1920.everit.utils.R.id.buttonLogin);
        buttonLogin.setOnClickListener(view -> launchLoginActivity());

        MaterialButton buttonSignUp = this.findViewById(it.uniba.di.sms1920.everit.utils.R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(view -> {
            String name = editTextName.getText().toString();
            String surname = editTextSurname.getText().toString();
            String phone = editTextPhoneNumber.getText().toString();
            String email = editTextMail.getText().toString();
            String password = editTextPassword.getText().toString();

            if(Utility.isNameValid(name, editTextNameContainer, this)){
                if(Utility.isSurnameValid(surname, editTextSurnameContainer, this)){
                    if(Utility.isPhoneValid(phone, editTextPhoneContainer, this)){
                        if(Utility.isEmailValid(email)) {
                            if(Utility.isPasswordValid(password)) {
                                try {
                                    Rider newRider = new Rider.RiderBuilder(name, surname, phone, email)
                                            .setPassword(password)
                                            .build();
                                    RiderRequest customerRequest = new RiderRequest();
                                    customerRequest.create(newRider, new RequestListener<Rider>() {
                                        @Override
                                        public void successResponse(Rider response) {
                                            Toast.makeText(getApplicationContext(), R.string.account_created, Toast.LENGTH_LONG).show();
                                            launchLoginActivity();
                                        }

                                        @Override
                                        public void errorResponse(RequestException error) {
                                            promptErrorMessage(error.getMessage());
                                        }
                                    });
                                } catch (InvalidPropertiesFormatException e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }else{
                                editTextPasswordContainer.setError(getString(R.string.error_password));
                            }
                        }else {
                            editTextMailContainer.setError(getString(R.string.error_email));
                        }
                    }
                }
            }
        });
    }

    private void launchLoginActivity() {
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
        });

        dialog.show();
    }

    private void focusOnView(View view){
        scrollViewSignUp.post(() -> scrollViewSignUp.scrollTo(0, view.getBottom()));
    }

}
