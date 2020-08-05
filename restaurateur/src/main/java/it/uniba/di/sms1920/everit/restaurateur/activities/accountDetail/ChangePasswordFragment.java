package it.uniba.di.sms1920.everit.restaurateur.activities.accountDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.LoginActivity;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class ChangePasswordFragment extends Fragment {

    private AccountDetailActivity mParent;

    private TextInputLayout editTextOldPasswordContainer;
    private TextInputEditText editTextOldPassword;
    private TextInputLayout editTextNewPasswordContainer;
    private TextInputEditText editTextNewPassword;
    private TextInputLayout editTextConfirmNewPasswordContainer;
    private TextInputEditText editTextConfirmNewPassword;
    private MaterialButton buttonChangePassword;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        editTextOldPasswordContainer = view.findViewById(R.id.editTextOldPasswordContainer);
        editTextOldPassword = view.findViewById(R.id.editTextOldPassword);
        editTextNewPasswordContainer = view.findViewById(R.id.editTextNewPasswordContainer);
        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);
        editTextConfirmNewPasswordContainer = view.findViewById(R.id.editTextConfirmNewPasswordContainer);
        editTextConfirmNewPassword = view.findViewById(R.id.editTextConfirmNewPassword);
        buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = editTextOldPassword.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();
                String confirmNewPassword = editTextConfirmNewPassword.getText().toString();

                if(Utility.isPasswordValid(oldPassword)) {
                    if (Utility.isPasswordValid(newPassword)) {
                        if (newPassword.equals(confirmNewPassword)) {
                            RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
                            restaurateurRequest.changePassword(oldPassword, newPassword, new RequestListener<Boolean>() {
                                @Override
                                public void successResponse(Boolean response) {
                                    mParent.finishAffinity();
                                    Intent intent = new Intent(mParent, LoginActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void errorResponse(RequestException error) {
                                    //TODO gestire error response
                                }
                            });
                        } else {
                            editTextNewPasswordContainer.setError(getString(R.string.error_match_password));
                            editTextConfirmNewPasswordContainer.setError(getString(R.string.error_match_password));
                        }
                    } else {
                        editTextNewPasswordContainer.setError(getString(R.string.error_password));
                    }
                }
                else{
                    editTextOldPasswordContainer.setError(getString(R.string.error_password));
                }

            }
        });


        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AccountDetailActivity){
            mParent = (AccountDetailActivity) context;
        }
    }
}