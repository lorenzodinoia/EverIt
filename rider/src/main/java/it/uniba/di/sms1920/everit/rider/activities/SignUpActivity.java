package it.uniba.di.sms1920.everit.rider.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.InvalidPropertiesFormatException;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.request.CustomerRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class SignUpActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextSurname;
    private Button buttonSignUp;
    private EditText editTextPhoneNumber;
    private EditText editTextMail;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.initComponents();
    }

    private void initComponents() {
        editTextMail =  findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPhoneNumber = findViewById(R.id.editTextPhone);
        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(view ->{
            launchLoginActivity();
        });
        buttonSignUp = findViewById(R.id.buttonSignUp);
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
