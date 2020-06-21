package it.uniba.di.sms1920.everit.restaurateur.activities.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.LoginActivity;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.OpeningDay;
import it.uniba.di.sms1920.everit.utils.models.OpeningTime;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.OpeningTimeRequest;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;


public class SignUp3Fragment extends Fragment {

    private SignUpActivity signUpActivity;
    private  Restaurateur.Builder restaurateurBuilder;
    private TextInputLayout editTextEmailContainer;
    private TextInputEditText editTextEmail;
    private TextInputLayout editTextPasswordContainer;
    private TextInputEditText editTextPassword;
    private TextInputLayout editTextConfirmPasswordContainer;
    private TextInputEditText editTextConfirmPassword;
    private MaterialButton btnSignUp;
    private MaterialButton btnBack;
    private Context context;

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
            signUpActivity.getSupportFragmentManager().popBackStackImmediate();
        });
        btnSignUp = viewRoot.findViewById(R.id.buttonSignUpAll);
        btnSignUp.setOnClickListener(view -> {

            boolean flag = true;
            if(Utility.isEmailValid(editTextEmail. getText().toString())){
                editTextEmailContainer.setError(null);
            }else{
                flag = false;
                editTextEmailContainer.setError(getString(R.string.error_email));
            }

            if(Utility.isPasswordValid(editTextPassword.getText().toString())){
                editTextPasswordContainer.setError(null);
            }else{
                flag = false;
                editTextPasswordContainer.setError(getString(R.string.error_password));
            }

            if(Utility.isPasswordValid(editTextConfirmPassword.getText().toString())){
                if(editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())){
                    editTextConfirmPasswordContainer.setError(null);
                }else{
                    flag = false;
                    editTextConfirmPasswordContainer.setError(getString(R.string.error_match_password));
                }
            }else{
                flag = false;
                editTextConfirmPasswordContainer.setError(getString(R.string.error_password));
            }


            if(flag){
                restaurateurBuilder.setEmail(editTextEmail.getText().toString());
                Restaurateur newRestaurateur = restaurateurBuilder.build();
                newRestaurateur.setPassword(editTextPassword.getText().toString());
                RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
                restaurateurRequest.create(newRestaurateur, new RequestListener<Restaurateur>() {
                    @Override
                    public void successResponse(Restaurateur response) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        //TODO gestire errorResponse
                        Log.d("test", error.getMessage());
                    }
                });
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof  SignUpActivity){
            signUpActivity = (SignUpActivity) context;
            this.context = context;
            restaurateurBuilder = signUpActivity.getRestaurateurBuilder();
        }
    }
}