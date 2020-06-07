package it.uniba.di.sms1920.everit.restaurateur.activities.signup;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
            getActivity().getSupportFragmentManager().popBackStack();
        });
        btnSignUp = viewRoot.findViewById(R.id.buttonSignUpAll);
        btnSignUp.setOnClickListener(view -> {

            boolean flag = true;
            if(Utility.isEmailValid(editTextEmail.toString())){
                editTextEmailContainer.setError(null);
            }else{
                flag = false;
                editTextEmailContainer.setError(getString(R.string.error_email));
            }

            if(Utility.isPasswordValid(editTextPassword.toString())){
                editTextPasswordContainer.setError(null);
            }else{
                flag = false;
                editTextPasswordContainer.setError(getString(R.string.error_password));
            }

            if(Utility.isPasswordValid(editTextConfirmPassword.toString())){
                if(editTextPassword.getText().equals(editTextConfirmPassword.getText())){
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
                SignUpActivity signUpActivity = new SignUpActivity();
                Restaurateur.Builder restaurateur = signUpActivity.getRestaurateur();

                restaurateur.setEmail(editTextEmail.getText().toString());
                restaurateur.setPassword(editTextPassword.getText().toString());
                Restaurateur newRestaurateur = restaurateur.build();
                RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
                restaurateurRequest.create(newRestaurateur, new RequestListener<Restaurateur>() {
                    @Override
                    public void successResponse(Restaurateur response) {
                        OpeningTimeRequest openingTimeRequest = new OpeningTimeRequest();
                        for(OpeningDay day : newRestaurateur.getOpeningDays()){
                            for(OpeningTime time : day.getOpeningTimes()){
                                //TODO fare richiesta sul server per aggiungere collection di openingtime
                                openingTimeRequest.create(time, new RequestListener<OpeningTime>() {
                                    @Override
                                    public void successResponse(OpeningTime response) {
                                        Toast.makeText(getContext(), R.string.account_created, Toast.LENGTH_LONG);
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }

                                    @Override
                                    public void errorResponse(RequestException error) {
                                        //TODO gestire errorResponse
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        //TODO gestire errorResponse
                    }
                });
            }
        });

    }
}