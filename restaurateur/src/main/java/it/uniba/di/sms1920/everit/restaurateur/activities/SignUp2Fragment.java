package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import it.uniba.di.sms1920.everit.restaurateur.R;


public class SignUp2Fragment extends Fragment {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextdescription;
    private ImageButton imgButtonProfileImg;
    private Button btnContinue;

    public SignUp2Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_sign_up2, parent, false);
        this.initComponent(viewRoot);
        return viewRoot;
    }

    private void initComponent(View viewRoot){
        editTextEmail = viewRoot.findViewById(R.id.editTextMail);
        editTextPassword = viewRoot.findViewById(R.id.editTextPassword);
        editTextdescription = viewRoot.findViewById(R.id.editTextDescription);
        imgButtonProfileImg = viewRoot.findViewById(R.id.imageButton);
        btnContinue = viewRoot.findViewById(R.id.buttonContinue);
        btnContinue.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            String descr = editTextdescription.getText().toString();
        });
    }


}
