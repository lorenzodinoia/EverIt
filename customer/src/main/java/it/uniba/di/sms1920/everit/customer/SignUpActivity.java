package it.uniba.di.sms1920.everit.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.request.CustomerRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.InvalidPropertiesFormatException;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private ScrollView scrollViewSignUp;
    private TextInputEditText editTextName;
    private TextInputEditText editTextSurname;
    private TextInputEditText editTextPhoneNumber;
    private TextInputEditText editTextMail;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextPasswordConfirm;
    private CardView cardViewHintRegex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(it.uniba.di.sms1920.everit.utils.R.layout.activity_sign_up);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        scrollViewSignUp = findViewById(it.uniba.di.sms1920.everit.utils.R.id.scrollViewSignUp);

        editTextMail =  findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextMail);
        TextInputLayout editTextMailContainer = findViewById(R.id.editTextMailContainer);
        editTextMail.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                focusOnView(editTextMail);
            }
        });

        cardViewHintRegex = findViewById(R.id.cardViewHintPassword);

        editTextPassword = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextPassword);
        TextInputLayout editTextPasswordContainer = findViewById(R.id.editTextPasswordContainer);
        editTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                cardViewHintRegex.setVisibility(View.VISIBLE);
                focusOnView(cardViewHintRegex);
            }
            else{
                cardViewHintRegex.setVisibility(View.GONE);
            }
        });

        editTextPhoneNumber = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextPhone);
        TextInputLayout editTextPhoneContainer = findViewById(R.id.editTextPhoneContainer);
        editTextPhoneNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                focusOnView(editTextPhoneNumber);
            }
        });

        editTextName = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextName);
        TextInputLayout editTextNameContainer = findViewById(R.id.editTextNameContainer);
        editTextName.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                focusOnView(editTextName);
            }
        });

        editTextSurname = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextSurname);
        TextInputLayout editTextSurnameContainer = findViewById(R.id.editTextSurnameContainer);
        editTextSurname.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                focusOnView(editTextSurname);
            }
        });

        TextInputLayout editTextPasswordConfirmContainer = findViewById(R.id.editTextPasswordConfirmContainer);
        editTextPasswordConfirm = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextPasswordConfirm);
        editTextPasswordConfirm.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                focusOnView(editTextPasswordConfirm);
            }
        });

        MaterialButton buttonLogin = this.findViewById(it.uniba.di.sms1920.everit.utils.R.id.buttonLogin);
        buttonLogin.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        MaterialButton buttonSignUp = this.findViewById(it.uniba.di.sms1920.everit.utils.R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(view -> {
            String email = editTextMail.getText().toString();
            String password = editTextPassword.getText().toString();
            String name = editTextName.getText().toString();
            String surname = editTextSurname.getText().toString();
            String phone = editTextPhoneNumber.getText().toString();
            //TODO Sistemare sto if innestato
            if(Utility.isNameValid(name, editTextNameContainer, this)){
                if(Utility.isSurnameValid(surname, editTextSurnameContainer, this)){
                    if(Utility.isPhoneValid(phone, editTextPhoneContainer, this)){
                        if(Utility.isEmailValid(email)) {
                            if(Utility.isPasswordValid(password)) {
                                if(password.equals(editTextPasswordConfirm.getText().toString())) {
                                    try {
                                        Customer newCustomer = new Customer.CustomerBuilder(name, surname, phone, email)
                                                .setPassword(password)
                                                .build();
                                        CustomerRequest customerRequest = new CustomerRequest();
                                        customerRequest.create(newCustomer, new RequestListener<Customer>() {
                                            @Override
                                            public void successResponse(Customer response) {
                                                Toast.makeText(getApplicationContext(), R.string.account_created, Toast.LENGTH_LONG).show();
                                                finish();
                                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void errorResponse(RequestException error) {
                                                promptErrorMessage(error.getMessage());
                                            }
                                        });
                                    } catch (InvalidPropertiesFormatException e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                                else {
                                    editTextPasswordConfirmContainer.setError(getString(R.string.password_confirm_wrong));
                                }
                            }
                            else{
                                editTextPasswordContainer.setError(getString(R.string.error_password));
                            }
                        }
                        else{
                            editTextMailContainer.setError(getString(R.string.error_email));
                        }
                    }
                }
            }


        });

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
