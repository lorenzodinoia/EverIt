package it.uniba.di.sms1920.everit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextSurname;
    private Button buttonRegister;
    private EditText editTextPhoneNumber;
    private EditText editTextMail;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.initComponents();
    }

    private void initComponents() {
        this.editTextMail =  findViewById(R.id.editTextMail);
        this.editTextPassword = findViewById(R.id.editTextPassword);
        this.buttonLogin = findViewById(R.id.buttonLogin);

//        this.editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
//        this.editTextName = findViewById(R.id.ediTextName);
//        this.editTextSurname = findViewById(R.id.ediTextSurname);
//        this.buttonRegister = findViewById(R.id.buttonRegister);


        this.buttonRegister.setOnClickListener(view -> {
            String email = this.editTextMail.getText().toString();
            String password = this.editTextPassword.getText().toString();
            String name = this.editTextName.getText().toString();
            String surname = this.editTextSurname.getText().toString();
            String phone = this.editTextPhoneNumber.getText().toString();
        });
    }

}
