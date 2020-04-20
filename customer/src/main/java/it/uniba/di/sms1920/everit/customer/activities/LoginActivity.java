package it.uniba.di.sms1920.everit.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.provider.AuthProvider;
import it.uniba.di.sms1920.everit.utils.provider.CredentialProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonGoToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.initComponents();
    }

    private void initComponents() {
        this.editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        this.editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        this.buttonGoToSignUp = (Button) findViewById(R.id.buttonGoToSignUp);
        //this.buttonGoToSignUp.setOnClickListener(view -> /**launchSignUpActivity()*/);
        this.buttonLogin = (Button) findViewById(R.id.buttonLogin);
        this.buttonLogin.setOnClickListener(view -> {
            String email = this.editTextEmail.getText().toString();
            String password = this.editTextPassword.getText().toString();

            if(Utility.isEmailValid(email) && Utility.isPasswordValid(password)) {
                this.login(email, password);
            }
            else {
                Toast toast = Toast.makeText(this, R.string.wrong_login, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    //Authprovider.getuser  if null cazzi

    private void login(String email, String password) {
        AuthProvider<Customer> auth = Providers.getAuthProvider();
        auth.login(new CredentialProvider.Credential(email, password), new RequestListener<Customer>() {
            @Override
            public void successResponse(Customer response) {
                Toast.makeText(getApplicationContext(), "Login completato", Toast.LENGTH_LONG ).show();
            }

            @Override
            public void errorResponse(RequestException error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG ).show();
            }
        });
    }

    /**
    private void launchSignUpActivity() {
        Intent goIntent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(goIntent);
    }
     */
}
