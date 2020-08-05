package it.uniba.di.sms1920.everit.customer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.request.CustomerRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.InvalidPropertiesFormatException;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

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
        editTextMail =  findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextMail);
        editTextPassword = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextPassword);

        editTextPhoneNumber = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextPhone);
        TextInputLayout editTextPhoneContainer = findViewById(R.id.editTextPhoneContainer);

        editTextName = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextName);
        TextInputLayout editTextNameContainer = findViewById(R.id.editTextNameContainer);

        editTextSurname = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextSurname);
        TextInputLayout editTextSurnameContainer = findViewById(R.id.editTextSurnameContainer);

        //TODO inizializzare button di login

        MaterialButton buttonSignUp = this.findViewById(it.uniba.di.sms1920.everit.utils.R.id.buttonSignUp); //si rompe qui per un NPE perchè non vede il cazzo di bottone
        buttonSignUp.setOnClickListener(view -> {
            String email = editTextMail.getText().toString();
            String password = editTextPassword.getText().toString();
            String name = editTextName.getText().toString();
            String surname = editTextSurname.getText().toString();
            String phone = editTextPhoneNumber.getText().toString();

            //TODO controlli su eamil e password diversi da quelli di match

            if(Utility.isNameValid(name, editTextNameContainer, this)){
                if(Utility.isSurnameValid(surname, editTextSurnameContainer, this)){
                    if(Utility.isPhoneValid(phone, editTextPhoneContainer, this)){

                        try {
                            Customer newCustomer = new Customer.CustomerBuilder(name, surname, phone, email)
                                    .setPassword(password)
                                    .build();
                            CustomerRequest customerRequest = new CustomerRequest();
                            customerRequest.create(newCustomer, new RequestListener<Customer>() {
                                @Override
                                public void successResponse(Customer response) {
                                    Toast.makeText(getApplicationContext(), R.string.account_created, Toast.LENGTH_LONG).show();
                                    //launchLoginActivity();
                                }

                                @Override
                                public void errorResponse(RequestException error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        catch (InvalidPropertiesFormatException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }


        });

    }

}
