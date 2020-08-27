package it.uniba.di.sms1920.everit.customer.activities.accountDetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.LoginActivity;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.CustomerRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

/**
 * Class used to handle Privacy & Security fragment. This fragment allows user to change his password or delete the account
 */
public class PrivacySecurityFragment extends Fragment {
    public static final String ITEM = "item";
    private static final String SAVED_CUSTOMER = "saved.customer";

    private Customer customer;
    private AppCompatActivity parentActivity;
    private TextInputEditText editTextOldPassword;
    private TextInputLayout editTextOldPasswordContainer;
    private TextInputEditText editTextNewPassword;
    private TextInputLayout editTextNewPasswordContainer;
    private TextInputEditText editTextPasswordConfirm;
    private TextInputLayout editTextPasswordConfirmContainer;
    private MaterialButton buttonDeleteAccount;
    private MaterialButton buttonChangePassword;

    public PrivacySecurityFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            if ((arguments != null) && (arguments.containsKey(ITEM))) {
                this.customer = arguments.getParcelable(ITEM);
            }
        }
        else if (savedInstanceState.containsKey(SAVED_CUSTOMER)) {
            this.customer = savedInstanceState.getParcelable(SAVED_CUSTOMER);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity){
            parentActivity = (AppCompatActivity) context;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_CUSTOMER, this.customer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacy_security, parent, false);
        this.initUi(view);
        return view;
    }

    private void initUi(View view) {
        if (this.parentActivity != null) {
            ActionBar parentActivityToolbar = this.parentActivity.getSupportActionBar();
            if (parentActivityToolbar != null) {
                parentActivityToolbar.setTitle(R.string.privacy_and_security);
            }
        }

        editTextOldPassword = view.findViewById(R.id.editTextOldPassword);
        editTextOldPasswordContainer = view.findViewById(R.id.editTextOldPasswordContainer);

        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);
        editTextNewPasswordContainer = view.findViewById(R.id.editTextNewPasswordContainer);

        editTextPasswordConfirm = view.findViewById(R.id.editTextPasswordConfirm);
        editTextPasswordConfirmContainer = view.findViewById(R.id.editTextPasswordConfirmContainer);

        buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        buttonDeleteAccount = view.findViewById(R.id.buttonDeleteAccount);

        buttonChangePassword.setOnClickListener(v -> this.changePassword());
        buttonDeleteAccount.setOnClickListener(v-> this.deleteAccount());
    }

    private void changePassword() {
        String oldPassword = this.editTextOldPassword.getText().toString();
        String newPassword = this.editTextNewPassword.getText().toString();
        String newPasswordConfirm = this.editTextPasswordConfirm.getText().toString();

        if (this.checkFieldsFilled(editTextOldPasswordContainer, editTextNewPasswordContainer, editTextPasswordConfirmContainer)) {
            if (newPassword.equals(newPasswordConfirm)) {
                if (Utility.isPasswordValid(newPassword)) {
                    if(!newPassword.equals(oldPassword)) {
                        CustomerRequest customerRequest = new CustomerRequest();
                        customerRequest.changePassword(oldPassword, newPassword, new RequestListener<Boolean>() {
                            @Override
                            public void successResponse(Boolean response) {
                                if (response) { //The password has been changed, now all user's local data is removed to force a new login. The fragment will be closed
                                    Providers.getAuthProvider().removeAllUserData();
                                    Toast.makeText(parentActivity, R.string.message_password_changed, Toast.LENGTH_LONG).show();
                                    parentActivity.finishAffinity();
                                    Intent intent = new Intent(parentActivity, LoginActivity.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void errorResponse(RequestException error) {
                                promptMessage(error.getMessage());
                            }
                        });
                    }
                    else {
                        editTextNewPasswordContainer.setError(getString(R.string.new_password_equals_old));
                    }
                }
                else {
                    this.editTextNewPasswordContainer.setError(this.parentActivity.getString(R.string.message_password_wrong));
                }
            }
            else {
                this.editTextPasswordConfirmContainer.setError(this.parentActivity.getString(R.string.message_password_confirm_mismatch));
            }
        }
    }

    private void deleteAccount() {
        Dialog dialog = new Dialog(parentActivity);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_y_n);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(R.string.delete_account);
        TextView message = dialog.findViewById(R.id.textViewMessage);
        message.setText(R.string.message_confirm_delete);
        MaterialButton btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> {
            CustomerRequest customerRequest = new CustomerRequest();
            customerRequest.delete(customer.getId(), new RequestListener<Boolean>() {
                @Override
                public void successResponse(Boolean response) {
                    parentActivity.finishAffinity();
                    Intent intent = new Intent(parentActivity, LoginActivity.class);
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
                field.setError(this.parentActivity.getString(R.string.message_field_required));
                allFieldsCompleted = false;
            }
            else {
                field.setErrorEnabled(false); //Remove error if it was marked previously
            }
        }

        return allFieldsCompleted;
    }

    private void promptMessage(String message) {
        Dialog dialog = new Dialog(parentActivity);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
            parentActivity.getSupportFragmentManager().popBackStack();
        });

        dialog.show();
    }
}
