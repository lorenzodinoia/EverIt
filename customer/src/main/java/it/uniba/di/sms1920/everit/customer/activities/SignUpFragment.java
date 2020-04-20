package it.uniba.di.sms1920.everit.customer.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.InvalidPropertiesFormatException;
import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.request.CustomerRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class SignUpFragment extends Fragment {

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPhoneNumber;
    private EditText editTextMail;
    private EditText editTextPassword;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_sign_up, parent, false);
        this.initComponents(viewRoot);
        return viewRoot;
    }

    private void initComponents(View viewRoot) {
        editTextMail =  viewRoot.findViewById(R.id.editTextMail);
        editTextPassword = viewRoot.findViewById(R.id.editTextPassword);
        editTextPhoneNumber = viewRoot.findViewById(R.id.editTextPhone);
        editTextName = viewRoot.findViewById(R.id.editTextName);
        editTextSurname = viewRoot.findViewById(R.id.editTextSurname);
        /*
        Button buttonSignUp = viewRoot.findViewById(R.id.btnSign); //si rompe qui per un NPE perchè non vede il cazzo di bottone
        buttonSignUp.setOnClickListener(view -> {
            String email = editTextMail.getText().toString();
            String password = editTextPassword.getText().toString();
            String name = editTextName.getText().toString();
            String surname = editTextSurname.getText().toString();
            String phone = editTextPhoneNumber.getText().toString();
            try {
                Customer newCustomer = new Customer.CustomerBuilder(name, surname, phone, email)
                        .setPassword(password)
                        .build();
                CustomerRequest customerRequest = new CustomerRequest();
                customerRequest.create(newCustomer, new RequestListener<Customer>() {
                    @Override
                    public void successResponse(Customer response) {
                        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), R.string.account_created, Toast.LENGTH_LONG).show();
                        //launchLoginActivity();
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            catch (InvalidPropertiesFormatException e) {
                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

         */
    }
}
