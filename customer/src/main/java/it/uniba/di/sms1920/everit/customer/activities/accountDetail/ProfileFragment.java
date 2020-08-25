package it.uniba.di.sms1920.everit.customer.activities.accountDetail;

import android.app.Dialog;
import android.content.Context;
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

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.request.CustomerRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class ProfileFragment extends Fragment {

    private Customer customer;
    private TextInputEditText editTextName;
    private TextInputLayout editTextNameContainer;
    private TextInputEditText editTextSurname;
    private TextInputLayout editTextSurnameContainer;
    private TextInputEditText editTextPhone;
    private TextInputLayout editTextPhoneContainer;
    private TextInputEditText editTextMail;
    private TextInputLayout editTextMailContainer;
    private MaterialButton buttonEditConfirm;

    private AccountDetailActivity mParent;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_profile, parent, false);
        customer = mParent.getCustomer();
        mParent.toolbar.setTitle(R.string.account_info);
        initComponent(viewRoot);

        return viewRoot;
    }

    private void initComponent(View viewRoot){

        editTextNameContainer = viewRoot.findViewById(R.id.editTextNameContainer);
        editTextName = viewRoot.findViewById(R.id.editTextName);
        editTextName.setText(customer.getName());
        editTextName.setEnabled(false);

        editTextSurnameContainer = viewRoot.findViewById(R.id.editTextSurnameContainer);
        editTextSurname = viewRoot.findViewById(R.id.editTextSurname);
        editTextSurname.setText(customer.getSurname());
        editTextSurname.setEnabled(false);

        editTextMailContainer = viewRoot.findViewById(R.id.editTextMailContainer);
        editTextMail =  viewRoot.findViewById(R.id.editTextMail);
        editTextMail.setText(customer.getEmail());
        editTextMail.setEnabled(false);

        editTextPhoneContainer = viewRoot.findViewById(R.id.editTextPhoneContainer);
        editTextPhone= viewRoot.findViewById(R.id.editTextPhone);
        editTextPhone.setText(customer.getPhoneNumber());
        editTextPhone.setEnabled(false);

        buttonEditConfirm = viewRoot.findViewById(R.id.buttonEditConfirm);
        buttonEditConfirm.setTag("Edit");
        buttonEditConfirm.setOnClickListener(v -> {
            String status = (String) buttonEditConfirm.getTag();
            if(status.equals("Edit")) {
                buttonEditConfirm.setText(R.string.confirm);
                editTextName.setEnabled(true);
                editTextSurname.setEnabled(true);
                editTextMail.setEnabled(true);
                editTextPhone.setEnabled(true);
                v.setTag("Confirm");
            } else {
                boolean flag = true;
                if(!Utility.isNameValid(editTextName.getText().toString(), editTextNameContainer, mParent)){
                    flag = false;
                } else{
                    editTextNameContainer.setError(null);
                    editTextNameContainer.clearFocus();
                }

                if(!Utility.isSurnameValid(editTextSurname.getText().toString(), editTextSurnameContainer, mParent)){
                    flag = false;
                } else{
                    editTextSurnameContainer.setError(null);
                    editTextSurnameContainer.clearFocus();
                }

                if(!Utility.isPhoneValid(editTextPhone.getText().toString(), editTextPhoneContainer, mParent)){
                    flag = false;
                } else{
                    editTextPhoneContainer.setError(null);
                    editTextPhoneContainer.clearFocus();
                }

                if(!Utility.isEmailValid(editTextMail.getText().toString())){
                    flag = false;
                    editTextMailContainer.setError(getString(R.string.error_email));
                } else{
                    editTextMailContainer.setError(null);
                    editTextMailContainer.clearFocus();
                }

                if(flag){
                    CustomerRequest customerRequest = new CustomerRequest();
                    customer.setName(editTextName.getText().toString());
                    customer.setSurname(editTextSurname.getText().toString());
                    customer.setEmail(editTextMail.getText().toString());
                    customer.setPhoneNumber(editTextPhone.getText().toString());
                    customerRequest.update(customer, new RequestListener<Customer>() {
                        @Override
                        public void successResponse(Customer response) {
                            customer = response;
                            Toast.makeText(mParent, R.string.account_updated, Toast.LENGTH_LONG).show();
                            buttonEditConfirm.setText(R.string.edit);
                            editTextName.setText(customer.getName());
                            editTextName.setEnabled(false);
                            editTextSurname.setText(customer.getSurname());
                            editTextSurname.setEnabled(false);
                            editTextMail.setText(customer.getEmail());
                            editTextMail.setEnabled(false);
                            editTextMail.setText(customer.getEmail());
                            editTextPhone.setEnabled(false);
                            v.setTag("Edit");
                        }

                        @Override
                        public void errorResponse(RequestException error) {
                            promptErrorMessage(error.getMessage());
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof  AccountDetailActivity){
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
}

