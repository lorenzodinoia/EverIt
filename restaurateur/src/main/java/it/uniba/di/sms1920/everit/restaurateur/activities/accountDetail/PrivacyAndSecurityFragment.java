package it.uniba.di.sms1920.everit.restaurateur.activities.accountDetail;

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
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.LoginActivity;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class PrivacyAndSecurityFragment extends Fragment {

    private final String ARG_RESTAURATEUR = "restaurateur_privacy_security_fragment";
    private AccountDetailActivity mParent;
    private Restaurateur restaurateur;

    private TextInputLayout editTextOldPasswordContainer;
    private TextInputEditText editTextOldPassword;
    private TextInputLayout editTextNewPasswordContainer;
    private TextInputEditText editTextNewPassword;
    private TextInputLayout editTextConfirmNewPasswordContainer;
    private TextInputEditText editTextConfirmNewPassword;
    private MaterialButton buttonChangePassword;
    private MaterialButton buttonDeleteAccount;

    public PrivacyAndSecurityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            restaurateur = mParent.getRestaurateur();
        }
        else{
            if(savedInstanceState.containsKey(ARG_RESTAURATEUR)) {
                restaurateur = savedInstanceState.getParcelable(ARG_RESTAURATEUR);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        initUi(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initUi(View view){
        editTextOldPasswordContainer = view.findViewById(R.id.editTextOldPasswordContainer);
        editTextOldPassword = view.findViewById(R.id.editTextOldPassword);
        editTextNewPasswordContainer = view.findViewById(R.id.editTextNewPasswordContainer);
        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);
        editTextConfirmNewPasswordContainer = view.findViewById(R.id.editTextConfirmNewPasswordContainer);
        editTextConfirmNewPassword = view.findViewById(R.id.editTextConfirmNewPassword);
        buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        buttonDeleteAccount = view.findViewById(R.id.buttonDeleteAccount);
    }

    private void initData(){
        mParent.toolbar.setTitle(R.string.privacy_and_security);
        buttonChangePassword.setOnClickListener(v -> {
            String oldPassword = editTextOldPassword.getText().toString();
            String newPassword = editTextNewPassword.getText().toString();
            String confirmNewPassword = editTextConfirmNewPassword.getText().toString();

            if(Utility.isPasswordValid(oldPassword)) {
                if (Utility.isPasswordValid(newPassword)) {
                    if (newPassword.equals(confirmNewPassword)) {
                        if(!newPassword.equals(oldPassword)) {
                            RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
                            restaurateurRequest.changePassword(oldPassword, newPassword, new RequestListener<Boolean>() {
                                @Override
                                public void successResponse(Boolean response) {
                                    Providers.getAuthProvider().removeAllUserData();
                                    Toast.makeText(mParent, R.string.message_password_changed, Toast.LENGTH_LONG).show();
                                    mParent.finishAffinity();
                                    Intent intent = new Intent(mParent, LoginActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void errorResponse(RequestException error) {
                                    promptErrorMessage(error.getMessage());
                                }
                            });
                        }
                        else{
                            editTextNewPasswordContainer.setError(getString(R.string.new_password_equals_old));
                        }
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

        });
        buttonDeleteAccount.setOnClickListener(v -> {
            Dialog dialog = new Dialog(mParent);
            dialog.setContentView(R.layout.dialog_message_y_n);

            TextView title = dialog.findViewById(R.id.textViewTitle);
            title.setText(R.string.delete_account);

            TextView message = dialog.findViewById(R.id.textViewMessage);
            message.setText(R.string.message_confirm_deleteAccount);

            Button btnOk = dialog.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(v12 -> {
                RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
                restaurateurRequest.delete(restaurateur.getId(), new RequestListener<Boolean>() {
                    @Override
                    public void successResponse(Boolean response) {
                        restaurateur = null;
                        mParent.finishAffinity();
                        Intent intent = new Intent(mParent, LoginActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        dialog.dismiss();
                        promptErrorMessage(error.getMessage());
                    }
                });
            });
            Button btnCancel = dialog.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(v1 -> dialog.dismiss());

            dialog.show();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AccountDetailActivity){
            mParent = (AccountDetailActivity) context;
        }
    }

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(mParent);
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

        if(restaurateur != null) {
            outState.putParcelable(ARG_RESTAURATEUR, restaurateur);
        }
    }
}