package it.uniba.di.sms1920.everit.customer.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.uniba.di.sms1920.everit.customer.R;
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
    private Customer customer;
    private Activity parentActivity;
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

        this.parentActivity = getActivity();

        this.customer = (Customer) Providers.getAuthProvider().getUser();
        if (this.customer == null) {
            showErrorMessage(this.parentActivity.getString(R.string.message_generic_error), true);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_privacy_security, parent, false);
        this.initComponent(viewRoot);
        return viewRoot;
    }

    /**
     * Initialize all the UI components
     *
     * @param viewRoot The view inflated which contains the components
     */
    private void initComponent(View viewRoot) {
        editTextOldPassword = viewRoot.findViewById(R.id.editTextOldPassword);
        editTextOldPasswordContainer = viewRoot.findViewById(R.id.editTextOldPasswordContainer);

        editTextNewPassword = viewRoot.findViewById(R.id.editTextNewPassword);
        editTextNewPasswordContainer = viewRoot.findViewById(R.id.editTextNewPasswordContainer);

        editTextPasswordConfirm = viewRoot.findViewById(R.id.editTextPasswordConfirm);
        editTextPasswordConfirmContainer = viewRoot.findViewById(R.id.editTextPasswordConfirmContainer);

        buttonChangePassword = viewRoot.findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(view -> {
            String oldPassword = this.editTextOldPassword.getText().toString();
            String newPassword = this.editTextNewPassword.getText().toString();
            String newPasswordConfirm = this.editTextPasswordConfirm.getText().toString();

            if (this.checkFieldsFilled(editTextOldPasswordContainer, editTextNewPasswordContainer, editTextPasswordConfirmContainer)) {
                if (newPassword.equals(newPasswordConfirm)) {
                    if (Utility.isPasswordValid(newPassword)) {
                        CustomerRequest customerRequest = new CustomerRequest();
                        customerRequest.changePassword(oldPassword, newPassword, new RequestListener<Boolean>() {
                            @Override
                            public void successResponse(Boolean response) {
                                if (response) {
                                    //The password has been changed, now all user's local data is removed to force a new login. The fragment will be closed
                                    Providers.getAuthProvider().removeAllUserData();
                                    showFeedbackMessage(parentActivity.getString(R.string.message_password_changed), true);
                                }
                            }

                            @Override
                            public void errorResponse(RequestException error) {
                                showErrorMessage(error.getMessage(), false);
                            }
                        });
                    }
                    else {
                        this.editTextNewPasswordContainer.setError(this.parentActivity.getString(R.string.message_password_wrong));
                    }
                }
                else {
                    this.editTextPasswordConfirmContainer.setError(this.parentActivity.getString(R.string.message_password_confirm_mismatch));
                }
            }
        });

        buttonDeleteAccount = viewRoot.findViewById(R.id.buttonDeleteAccount);
        buttonDeleteAccount.setOnClickListener(view -> {
            //Popup used to confirm the operation
            View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window_confirm, null);
            PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setElevation(20);
            popupWindow.setOverlapAnchor(true);

            MaterialButton buttonYes = popupView.findViewById(R.id.btnYes);
            MaterialButton buttonNo = popupView.findViewById(R.id.btnNo);

            buttonNo.setOnClickListener(v -> popupWindow.dismiss()); //If the user abort the popup will be closed
            buttonYes.setOnClickListener(v -> {
                CustomerRequest customerRequest = new CustomerRequest();
                customerRequest.delete(this.customer.getId(), new RequestListener<Boolean>() {
                    @Override
                    public void successResponse(Boolean response) {
                        if (response) {
                            //The account has been canceled, now all user's local data is removed and the fragment will be closed
                            Providers.getAuthProvider().removeAllUserData();
                            showFeedbackMessage(parentActivity.getString(R.string.message_account_deleted), true);
                        }
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        showErrorMessage(error.getMessage(), false);
                    }
                });
                popupWindow.dismiss(); //Anyway the popup is closed
            });

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0); //Shows the popup
        });
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

    /**
     * Show a popup message as feedback to the user when an operation is successfully completed
     *
     * @param text Text which will be displayed
     * @param closeFragment Tell if the fragment will be closed when the user approves
     */
    private void showFeedbackMessage(String text, boolean closeFragment) {
        new AlertDialog.Builder(this.parentActivity)
                .setTitle(this.parentActivity.getString(R.string.message_done))
                .setMessage(text)
                .setPositiveButton(this.parentActivity.getString(R.string.ok_default), (dialog, which) -> {
                    dialog.dismiss();
                    if (closeFragment) {
                        this.parentActivity.onBackPressed();
                    }
                })
                .show();
    }

    /**
     * Show an error message
     *
     * @param text Text which will be displayed
     * @param closeFragment Tell if the fragment will be closed when the user approves
     */
    private void showErrorMessage(String text, boolean closeFragment) {
        new AlertDialog.Builder(this.parentActivity)
                .setTitle(this.parentActivity.getString(R.string.message_generic_error))
                .setMessage(text)
                .setPositiveButton(this.parentActivity.getString(R.string.ok_default), (dialog, which) -> {
                    dialog.dismiss();
                    if (closeFragment) {
                        this.parentActivity.onBackPressed();
                    }
                })
                .show();
    }
}
