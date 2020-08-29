package it.uniba.di.sms1920.everit.restaurateur.activities.signup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.LoginActivity;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;


public class SignUp3Fragment extends Fragment {

    private final String ARG_RESTAURATEUR = "restaurateur_builder_signup3";

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

    //TODO crasha dopo 2 rotazioni

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            restaurateurBuilder = signUpActivity.getRestaurateur();
        }
        else{
            restaurateurBuilder = savedInstanceState.getParcelable(ARG_RESTAURATEUR);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_sign_up3, container, false);

        editTextEmailContainer = viewRoot.findViewById(R.id.editTextEmailContainerSignUp);
        editTextEmail = viewRoot.findViewById(R.id.editTextEmailSignUp);
        editTextPasswordContainer = viewRoot.findViewById(R.id.editTextPasswordContainerSignUp);
        editTextPassword = viewRoot.findViewById(R.id.editTextPasswordSignUp);
        editTextConfirmPasswordContainer = viewRoot.findViewById(R.id.editTextConfirmPasswordContainerSignUp);
        editTextConfirmPassword = viewRoot.findViewById(R.id.editTextConfirmPasswordSignUp);
        btnBack = viewRoot.findViewById(R.id.buttonBack2);
        btnSignUp = viewRoot.findViewById(R.id.buttonSignUpAll);

        return viewRoot;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initData(){

        btnBack.setOnClickListener(view -> {
            signUpActivity.getSupportFragmentManager().popBackStack();
        });

        btnSignUp.setOnClickListener(view -> {

            boolean flag = true;
            if(!Utility.isEmailValid(editTextEmail. getText().toString())){
                flag = false;
                editTextEmailContainer.setError(getString(R.string.error_email));
            }else{
                editTextEmailContainer.setError(null);
            }

            if(!Utility.isPasswordValid(editTextPassword.getText().toString())){
                flag = false;
                editTextPasswordContainer.setError(getString(R.string.error_password));
            }else{
                editTextPasswordContainer.setError(null);
            }

            if(!Utility.isPasswordValid(editTextConfirmPassword.getText().toString())){
                flag = false;
                editTextConfirmPasswordContainer.setError(getString(R.string.error_password));
            }else{
                if(editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())){
                    editTextConfirmPasswordContainer.setError(null);
                }else{
                    flag = false;
                    editTextConfirmPasswordContainer.setError(getString(R.string.error_match_password));
                }
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
                        promptErrorMessage(error.getMessage());
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
        }
    }

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(signUpActivity);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ARG_RESTAURATEUR, restaurateurBuilder);
    }
}