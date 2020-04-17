package it.uniba.di.sms1920.everit.customer.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


import it.uniba.di.sms1920.everit.customer.R;

public class ProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPhone;
    private EditText editTextMail;
    private EditText editTextPassword;
    private Button buttonEditConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.initComponent();
    }

    private void initComponent(){

        editTextName = findViewById(R.id.editTextName);
        editTextName.setEnabled(false);

        editTextSurname = findViewById(R.id.editTextSurname);
        editTextSurname.setEnabled(false);

        editTextMail =  findViewById(R.id.editTextMail);
        editTextMail.setEnabled(false);

        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword.setEnabled(false);

        editTextPhone= findViewById(R.id.editTextPhone);
        editTextPhone.setEnabled(false);

        buttonEditConfirm = findViewById(R.id.buttonEditConfirm);
        buttonEditConfirm.setTag("Edit");
        buttonEditConfirm.setOnClickListener(v -> {
            String status = (String) buttonEditConfirm.getTag();
            if(status.equals("Edit")) {
                buttonEditConfirm.setText("Confirm");
                editTextName.setEnabled(true);
                editTextSurname.setEnabled(true);
                editTextMail.setEnabled(true);
                editTextPhone.setEnabled(true);
                editTextPassword.setEnabled(true);
                v.setTag("Confirm");
            } else {
                buttonEditConfirm.setText("Edit");
                editTextName.setEnabled(false);
                editTextSurname.setEnabled(false);
                editTextMail.setEnabled(false);
                editTextPhone.setEnabled(false);
                editTextPassword.setEnabled(false);
                v.setTag("Edit");
            }
        });
    }



}
