package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import it.uniba.di.sms1920.everit.restaurateur.R;


public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Fragment fragmentSignUp1 = new Fragment(R.layout.fragment_sign_up1);
        getSupportFragmentManager().beginTransaction().add(R.id.containerMain, fragmentSignUp1).commit();
    }

}
