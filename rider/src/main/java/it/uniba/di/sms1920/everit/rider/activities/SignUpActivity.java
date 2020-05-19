package it.uniba.di.sms1920.everit.rider.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.InvalidPropertiesFormatException;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.request.CustomerRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText editTextName;
    private TextInputEditText editTextSurname;
    private MaterialButton buttonSignUp;
    private TextInputEditText editTextPhoneNumber;
    private TextInputEditText editTextMail;
    private TextInputEditText editTextPassword;
    private MaterialButton buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(it.uniba.di.sms1920.everit.utils.R.layout.activity_sign_up);
        this.initComponents();
    }

    private void initComponents() {
        editTextMail =  findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextMail);
        editTextPassword = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextPassword);
        editTextPhoneNumber = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextPhone);
        editTextName = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextName);
        editTextSurname = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextSurname);
        buttonLogin = this.findViewById(it.uniba.di.sms1920.everit.utils.R.id.buttonLogin);
        buttonLogin.setOnClickListener(view ->{
            launchLoginActivity();
        });
        buttonSignUp = this.findViewById(it.uniba.di.sms1920.everit.utils.R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(view -> {
            String email = editTextMail.getText().toString();
            String password = editTextPassword.getText().toString();
            String name = editTextName.getText().toString();
            String surname = editTextSurname.getText().toString();
            String phone = editTextPhoneNumber.getText().toString();
            try {
                Customer newCustomer = new Customer.CustomerBuilder(name, surname, phone, email)
                        .setPassword(password)
                        .build();
                CustomerRequest customerRequest = new CustomerRequest();
                customerRequest.create(newCustomer, new RequestListener<Customer>() {
                    @Override
                    public void successResponse(Customer response) {
                        Toast.makeText(getApplicationContext(), R.string.account_created, Toast.LENGTH_LONG).show();
                        launchLoginActivity();
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
        });
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
