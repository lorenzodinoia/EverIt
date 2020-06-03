package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.Utility;


public class SignUp3Fragment extends Fragment {

    private TextInputLayout editTextEmailContainer;
    private TextInputEditText editTextEmail;
    private TextInputLayout editTextPasswordContainer;
    private TextInputEditText editTextPassword;
    private TextInputLayout editTextConfirmPasswordContainer;
    private TextInputEditText editTextConfirmPassword;
    private MaterialButton btnSignUp;
    private MaterialButton btnBack;

    public SignUp3Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_sign_up3, container, false);
        this.initComponent(viewRoot);
        return viewRoot;
    }

    private void initComponent(View viewRoot){
        editTextEmailContainer = viewRoot.findViewById(R.id.editTextEmailContainerSignUp);
        editTextEmail = viewRoot.findViewById(R.id.editTextEmailSignUp);
        editTextPasswordContainer = viewRoot.findViewById(R.id.editTextPasswordContainerSignUp);
        editTextPassword = viewRoot.findViewById(R.id.editTextPasswordSignUp);
        editTextConfirmPasswordContainer = viewRoot.findViewById(R.id.editTextConfirmPasswordContainerSignUp);
        editTextConfirmPassword = viewRoot.findViewById(R.id.editTextConfirmPasswordSignUp);
        btnBack = viewRoot.findViewById(R.id.buttonBack2);
        btnBack.setOnClickListener(view -> {
            //TODO controllare ritorno
            getActivity().getSupportFragmentManager().popBackStack();
        });
        btnSignUp = viewRoot.findViewById(R.id.buttonSignUpAll);
        btnSignUp.setOnClickListener(view -> {

            boolean flag = true;
            if(Utility.isEmailValid(editTextEmail.toString())){
                editTextEmailContainer.setError(null);
            }else{
                flag = false;
                editTextEmailContainer.setError("Inserire messaggio di errore");
            }

            if(Utility.isPasswordValid(editTextPassword.toString())){
                editTextPasswordContainer.setError(null);
            }else{
                flag = false;
                editTextPasswordContainer.setError("Inserire messaggio di errore");
            }

            if(Utility.isPasswordValid(editTextConfirmPassword.toString())){
                editTextConfirmPasswordContainer.setError(null);
            }else{
                flag = false;
                editTextConfirmPasswordContainer.setError("Inserire messaggio di errore");
            }

            if(flag){
                //TODO fare richiesta
            }
        });

    }
}