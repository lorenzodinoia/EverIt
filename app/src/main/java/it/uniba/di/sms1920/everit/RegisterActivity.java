package it.uniba.di.sms1920.everit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.initComponents();
    }

    private void initComponents() {
        this.editTextEmail =  findViewById(R.id.editTextEmail);
        this.editTextPassword = findViewById(R.id.editTextPassword);
        this.buttonLogin = findViewById(R.id.buttonLogin);
        this.editTextName = findViewById(R.id.editTextName);
        this.editTextSurname = findViewById(R.id.editTextSurname);
        this.editTextPhone = findViewById(R.id.editTextPhone);
        this.buttonRegister = findViewById(R.id.buttonRegister);

        this.buttonRegister.setOnClickListener(view -> {
            String email = this.editTextEmail.getText().toString();
            String password = this.editTextPassword.getText().toString();
            String name = this.editTextName.getText().toString();
            String surname = this.editTextSurname.getText().toString();
            String phone = this.editTextPhone.getText().toString();
        });
    }

}
