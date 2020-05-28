package it.uniba.di.sms1920.everit.customer.activities;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_profile, parent, false);
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.read(Providers.getAuthProvider().getUser().getId(), new RequestListener<Customer>() {
            @Override
            public void successResponse(Customer response) {
                customer = response;
                initComponent(viewRoot);
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO implementare errore risposta
                Log.d("test", error.toString());
            }
        });
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
        buttonEditConfirm.setTag(R.string.edit);
        buttonEditConfirm.setOnClickListener(v -> {
            String status = (String) buttonEditConfirm.getTag();
            if(status.equals(R.string.edit)) {
                buttonEditConfirm.setText("Confirm");
                editTextName.setEnabled(true);
                editTextSurname.setEnabled(true);
                editTextMail.setEnabled(true);
                editTextPhone.setEnabled(true);
                v.setTag(R.string.confirm);
            } else {
                boolean flag = true;
                //TODO aggiungere messaggi errore ai campi errati
                if(!Utility.isNameValid(editTextName.getText().toString())){
                    flag = false;
                    editTextNameContainer.setError("Mettere messaggio di errore");
                    editTextNameContainer.setBoxStrokeColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorWarning));
                } else{
                    editTextNameContainer.setError(null);
                    editTextNameContainer.clearFocus();
                }

                if(!Utility.isSurnameValid(editTextSurname.getText().toString())){
                    flag = false;
                    editTextSurnameContainer.setError("Mettere messaggio di errore");
                    editTextSurnameContainer.setBoxStrokeColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorWarning));
                } else{
                    editTextNameContainer.setError(null);
                    editTextNameContainer.clearFocus();
                }

                if(!Utility.isPhoneValid(editTextPhone.getText().toString())){
                    flag = false;
                    editTextPhoneContainer.setError("Mettere messaggio di errore");
                    editTextPhoneContainer.setBoxStrokeColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorWarning));
                } else{
                    editTextNameContainer.setError(null);
                    editTextNameContainer.clearFocus();
                }

                if(!Utility.isEmailValid(editTextMail.getText().toString())){
                    flag = false;
                    editTextMailContainer.setError("Mettere messaggio di errore");
                    editTextMailContainer.setBoxStrokeColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorWarning));
                } else{
                    editTextNameContainer.setError(null);
                    editTextNameContainer.clearFocus();
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
                            Toast.makeText(getActivity().getApplicationContext(), R.string.account_updated, Toast.LENGTH_LONG).show();
                            buttonEditConfirm.setText(R.string.edit);
                            editTextName.setText(customer.getName());
                            editTextName.setEnabled(false);
                            editTextSurname.setText(customer.getSurname());
                            editTextSurname.setEnabled(false);
                            editTextMail.setText(customer.getEmail());
                            editTextMail.setEnabled(false);
                            editTextMail.setText(customer.getEmail());
                            editTextPhone.setEnabled(false);
                            v.setTag(R.string.edit);
                        }

                        @Override
                        public void errorResponse(RequestException error) {
                            Toast.makeText(getActivity().getApplicationContext(), R.string.account_not_updated, Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });
    }


}

