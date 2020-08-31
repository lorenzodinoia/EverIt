package it.uniba.di.sms1920.everit.customer.activities.accountDetail;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.request.CustomerRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class ProfileFragment extends Fragment {
    public static final String ITEM = "item";
    private static final String SAVED_CUSTOMER = "saved.customer";
    private static final String BUTTON_TAG_EDIT = "edit";
    private static final String BUTTON_TAG_CONFIRM = "confirm";

    private Customer customer;

    private AppCompatActivity parentActivity;
    private TextInputEditText editTextName;
    private TextInputLayout editTextNameContainer;
    private TextInputEditText editTextSurname;
    private TextInputLayout editTextSurnameContainer;
    private TextInputEditText editTextPhone;
    private TextInputLayout editTextPhoneContainer;
    private TextInputEditText editTextMail;
    private TextInputLayout editTextMailContainer;
    private MaterialButton buttonEditConfirm;

    public ProfileFragment() {
        // Required empty public constructor
    }

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
        if (context instanceof AppCompatActivity) {
            this.parentActivity = (AppCompatActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, parent, false);
        this.initUi(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.customer != null) {
            this.initData();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_CUSTOMER, this.customer);
    }

    private void initUi(View view) {
        if (this.parentActivity != null) {
            ActionBar parentActivityToolbar = this.parentActivity.getSupportActionBar();
            if (parentActivityToolbar != null) {
                parentActivityToolbar.setTitle(R.string.account_info);
            }
        }

        editTextNameContainer = view.findViewById(R.id.editTextNameContainer);
        editTextName = view.findViewById(R.id.editTextName);

        editTextSurnameContainer = view.findViewById(R.id.editTextSurnameContainer);
        editTextSurname = view.findViewById(R.id.editTextSurname);

        editTextMailContainer = view.findViewById(R.id.editTextMailContainer);
        editTextMail =  view.findViewById(R.id.editTextMail);

        editTextPhoneContainer = view.findViewById(R.id.editTextPhoneContainer);
        editTextPhone= view.findViewById(R.id.editTextPhone);

        buttonEditConfirm = view.findViewById(R.id.buttonEditConfirm);
    }

    private void initData() {
        editTextName.setText(customer.getName());
        editTextName.setEnabled(false);

        editTextSurname.setText(customer.getSurname());
        editTextSurname.setEnabled(false);

        editTextMail.setText(customer.getEmail());
        editTextMail.setEnabled(false);

        editTextPhone.setText(customer.getPhoneNumber());
        editTextPhone.setEnabled(false);

        buttonEditConfirm.setTag(BUTTON_TAG_EDIT);
        buttonEditConfirm.setOnClickListener(this::handleButtonClick);
    }

    private void handleButtonClick(View button) {
        String status = (String) buttonEditConfirm.getTag();

        if (status.equals(BUTTON_TAG_EDIT)) {
            buttonEditConfirm.setText(R.string.confirm);
            editTextName.setEnabled(true);
            editTextSurname.setEnabled(true);
            editTextMail.setEnabled(true);
            editTextPhone.setEnabled(true);
            button.setTag(BUTTON_TAG_CONFIRM);
        }
        else {
            boolean dataIsCorrect = true;

            if (!Utility.isNameValid(editTextName.getText().toString(), editTextNameContainer, parentActivity)) {
                dataIsCorrect = false;
            }
            else {
                editTextNameContainer.setError(null);
                editTextNameContainer.clearFocus();
            }

            if (!Utility.isSurnameValid(editTextSurname.getText().toString(), editTextSurnameContainer, parentActivity)){
                dataIsCorrect = false;
            }
            else {
                editTextSurnameContainer.setError(null);
                editTextSurnameContainer.clearFocus();
            }

            if (!Utility.isPhoneValid(editTextPhone.getText().toString(), editTextPhoneContainer, parentActivity)){
                dataIsCorrect = false;
            }
            else {
                editTextPhoneContainer.setError(null);
                editTextPhoneContainer.clearFocus();
            }

            if (!Utility.isEmailValid(editTextMail.getText().toString())){
                dataIsCorrect = false;
                editTextMailContainer.setError(getString(R.string.error_email));
            }
            else {
                editTextMailContainer.setError(null);
                editTextMailContainer.clearFocus();
            }

            if (dataIsCorrect) {
                customer.setName(editTextName.getText().toString());
                customer.setSurname(editTextSurname.getText().toString());
                customer.setEmail(editTextMail.getText().toString());
                customer.setPhoneNumber(editTextPhone.getText().toString());

                CustomerRequest customerRequest = new CustomerRequest();
                customerRequest.update(customer, new RequestListener<Customer>() {
                    @Override
                    public void successResponse(Customer response) {
                        customer = response;

                        Snackbar.make(button, R.string.account_updated, Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(parentActivity, R.string.account_updated, Toast.LENGTH_LONG).show();
                        buttonEditConfirm.setText(R.string.edit);

                        editTextName.setText(customer.getName());
                        editTextName.setEnabled(false);

                        editTextSurname.setText(customer.getSurname());
                        editTextSurname.setEnabled(false);

                        editTextMail.setText(customer.getEmail());
                        editTextMail.setEnabled(false);

                        editTextPhone.setText(customer.getPhoneNumber());
                        editTextPhone.setEnabled(false);

                        button.setTag(BUTTON_TAG_EDIT);
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        promptErrorMessage(error.getMessage());
                    }
                });
            }
        }
    }

    private void promptErrorMessage(String message) {
        Dialog dialog = new Dialog(this.parentActivity);
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
}

