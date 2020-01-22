package it.uniba.di.sms1920.everit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                if(this.login(email, password)) {
                    //Go to home activity
                }
                else {
                    Toast toast = Toast.makeText(this, R.string.login_failed, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            else {
                Toast toast = Toast.makeText(this, R.string.login_incorrect, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private boolean login(String email, String password) {
        //Login
        return true;
    }
}
