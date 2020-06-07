package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.signup.SignUpActivity;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.AuthProvider;
import it.uniba.di.sms1920.everit.utils.provider.CredentialProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private MaterialButton buttonLogin;
    private MaterialButton buttonGoToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.initComponents();
    }

    private void initComponents() {
        this.editTextEmail = this.findViewById(R.id.editTextEmail);
        this.editTextPassword = this.findViewById(R.id.editTextPassword);
        this.buttonGoToSignUp = this.findViewById(R.id.buttonGoToSignUp);
        this.buttonGoToSignUp.setOnClickListener(view -> launchSignUpActivity());
        this.buttonLogin = this.findViewById(R.id.buttonLoginAL);
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

    private void login(String email, String password) {
        AuthProvider<Restaurateur> auth = Providers.getAuthProvider();
        auth.login(new CredentialProvider.Credential(email, password), new RequestListener<Restaurateur>() {
            @Override
            public void successResponse(Restaurateur response) {
                Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void errorResponse(RequestException error) {
                Log.d("test", error.toString());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void launchSignUpActivity() {
        Intent goIntent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(goIntent);
    }
}
