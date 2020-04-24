package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import it.uniba.di.sms1920.everit.restaurateur.R;


public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Fragment fragment_signup1 = new Fragment(R.layout.fragment_sign_up1);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.signUpMain, fragment_signup1);
        //fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }

}
