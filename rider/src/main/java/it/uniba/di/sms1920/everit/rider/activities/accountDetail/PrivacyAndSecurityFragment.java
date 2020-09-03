package it.uniba.di.sms1920.everit.rider.activities.accountDetail;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.rider.activities.LoginActivity;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Rider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.CustomerRequest;
import it.uniba.di.sms1920.everit.utils.request.RiderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;


public class PrivacyAndSecurityFragment extends Fragment {

    private AccountDetailActivity mParent;
    private Rider rider;

    private TextInputEditText editTextOldPassword;
    private TextInputLayout editTextOldPasswordContainer;
    private TextInputEditText editTextNewPassword;
    private TextInputLayout editTextNewPasswordContainer;
    private TextInputEditText editTextPasswordConfirm;
    private TextInputLayout editTextPasswordConfirmContainer;
    private MaterialButton buttonDeleteAccount;
    private MaterialButton buttonChangePassword;


    public PrivacyAndSecurityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            Bundle bundle = getArguments();
            if(bundle != null) {
                if (bundle.containsKey(AccountDetailActivity.ARG_RIDER)) {
                    rider = bundle.getParcelable(AccountDetailActivity.ARG_RIDER);
                }
            }
        }
        else{
            rider = savedInstanceState.getParcelable(AccountDetailActivity.ARG_RIDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_privacy_and_security, container, false);
        initUi(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initUi(View view){
        editTextOldPassword = view.findViewById(R.id.editTextOldPassword);
        editTextOldPasswordContainer = view.findViewById(R.id.editTextOldPasswordContainer);

        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);
        editTextNewPasswordContainer = view.findViewById(R.id.editTextNewPasswordContainer);

        editTextPasswordConfirm = view.findViewById(R.id.editTextPasswordConfirm);
        editTextPasswordConfirmContainer = view.findViewById(R.id.editTextPasswordConfirmContainer);

        buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        buttonDeleteAccount = view.findViewById(R.id.buttonDeleteAccount);
    }

    private void initData(){
        buttonChangePassword.setOnClickListener(view -> {
            String oldPassword = this.editTextOldPassword.getText().toString();
            String newPassword = this.editTextNewPassword.getText().toString();
            String newPasswordConfirm = this.editTextPasswordConfirm.getText().toString();

            if (this.checkFieldsFilled(editTextOldPasswordContainer, editTextNewPasswordContainer, editTextPasswordConfirmContainer)) {
                if (newPassword.equals(newPasswordConfirm)) {
                    if (Utility.isPasswordValid(newPassword)) {
                        if(!newPassword.equals(oldPassword)) {
                            RiderRequest riderRequest = new RiderRequest();
                            riderRequest.changePassword(oldPassword, newPassword, new RequestListener<Boolean>() {
                                @Override
                                public void successResponse(Boolean response) {
                                    if (response) {//The password has been changed, now all user's local data is removed to force a new login. The fragment will be closed
                                        Providers.getAuthProvider().removeAllUserData();
                                        Snackbar.make(buttonChangePassword, R.string.message_password_changed, Snackbar.LENGTH_SHORT).show();
                                        mParent.finishAffinity();
                                        Intent intent = new Intent(mParent, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void errorResponse(RequestException error) {
                                    promptMessage(error.getMessage());
                                }
                            });
                        }
                        else{
                            editTextNewPasswordContainer.setError(getString(R.string.new_password_equals_old));
                        }
                    }
                    else {
                        this.editTextNewPasswordContainer.setError(mParent.getString(R.string.message_password_wrong));
                    }
                }
                else {
                    this.editTextPasswordConfirmContainer.setError(mParent.getString(R.string.message_password_confirm_mismatch));
                }
            }
        });

        buttonDeleteAccount.setOnClickListener(view -> {

            Dialog dialog = new Dialog(mParent);
            dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_y_n);

            TextView title = dialog.findViewById(R.id.textViewTitle);
            title.setText(R.string.delete_account);
            TextView message = dialog.findViewById(R.id.textViewMessage);
            message.setText(R.string.message_confirm_delete);
            MaterialButton btnOk = dialog.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(v -> {

                RiderRequest riderRequest = new RiderRequest();
                riderRequest.delete(rider.getId(), new RequestListener<Boolean>() {
                    @Override
                    public void successResponse(Boolean response) {
                        mParent.finishAffinity();
                        Intent intent = new Intent(mParent, LoginActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        dialog.dismiss();
                        promptMessage(error.getMessage());
                    }
                });
            });
            MaterialButton btnCancel = dialog.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        });
    }

    private void promptMessage(String message){
        Dialog dialog = new Dialog(mParent);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
            mParent.getSupportFragmentManager().popBackStack();
        });

        dialog.show();
    }

    /**
     * Check if the fields are filled.
     * If a field is unfilled this one will be marked as uncorrected with a message
     *
     * @param fields Fields which will be checked
     * @return true if all provided fields is filled, false otherwise
     */
    private boolean checkFieldsFilled(TextInputLayout... fields) {
        boolean allFieldsCompleted = true;

        for (TextInputLayout field : fields) {
            String text = field.getEditText().getText().toString().trim(); //Get text without leading and trailing spaces
            if (text.length() == 0) {
                field.setError(mParent.getString(R.string.message_field_required));
                allFieldsCompleted = false;
            }
            else {
                field.setErrorEnabled(false); //Remove error if it was marked previously
            }
        }

        return allFieldsCompleted;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AccountDetailActivity){
            mParent = (AccountDetailActivity) context;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(AccountDetailActivity.ARG_RIDER, rider);
    }
}