package it.uniba.di.sms1920.everit.customer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.request.CustomerRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.InvalidPropertiesFormatException;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPhoneNumber;
    private EditText editTextMail;
    private EditText editTextPassword;

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
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void initComponents() {
        editTextMail =  findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextMail);
        editTextPassword = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextPassword);
        editTextPhoneNumber = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextPhone);
        editTextName = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextName);
        editTextSurname = findViewById(it.uniba.di.sms1920.everit.utils.R.id.editTextSurname);

        Button buttonSignUp = findViewById(it.uniba.di.sms1920.everit.utils.R.id.buttonSignUp); //si rompe qui per un NPE perchè non vede il cazzo di bottone
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
        });

    }

}
