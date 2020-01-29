package it.uniba.di.sms1920.everit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


//import it.uniba.di.sms1920.everit.customer.HomeActivity;
import it.uniba.di.sms1920.everit.request.AuthProvider;
import it.uniba.di.sms1920.everit.request.RequestListener;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.initComponents();
    }

    private void initComponents() {
        this.editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        this.editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        this.buttonLogin = (Button) findViewById(R.id.buttonLogin);
        this.buttonLogin.setOnClickListener(view -> {
            String email = this.editTextEmail.getText().toString();
            String password = this.editTextPassword.getText().toString();

            if(Utility.isEmailValid(email) && Utility.isPasswordValid(password)) {
                this.login(email, password);
            }
            else {
                Toast toast = Toast.makeText(this, R.string.login_incorrect, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private void login(String email, String password) {
        AuthProvider.getInstance().login(email, password, new RequestListener<Boolean>() {
            @Override
            public void successResponse(Boolean response) {
                //Intent intent = intent = new Intent(getApplicationContext(), HomeActivity.class);
                //startActivity(intent);
            }

            @Override
            public void errorResponse(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
