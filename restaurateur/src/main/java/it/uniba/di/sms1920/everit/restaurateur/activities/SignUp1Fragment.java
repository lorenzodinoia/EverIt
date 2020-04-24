package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import it.uniba.di.sms1920.everit.restaurateur.R;

public class SignUp1Fragment extends Fragment {

    private EditText editTextShopName;
    private EditText editTextPhoneNumber;
    private EditText editTextVAT;
    private EditText editTextAddress;

    public SignUp1Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_sign_up1, parent, false);
        this.initComponent(viewRoot);
        return viewRoot;
    }

    private void initComponent(View viewRoot) {
        editTextAddress = viewRoot.findViewById(R.id.editTextAddress);
        editTextPhoneNumber = viewRoot.findViewById(R.id.editTextPhoneNumber);
        editTextShopName = viewRoot.findViewById(R.id.editTextShopName);
        editTextVAT = viewRoot.findViewById(R.id.editTextVAT);
        Button btnNext = viewRoot.findViewById(R.id.buttonContinue);
        btnNext.setOnClickListener(view -> {
            String shopName = editTextShopName.getText().toString();
            String address = editTextAddress.getText().toString();
            String VAT = editTextVAT.getText().toString();
            String phoneNumber = editTextPhoneNumber.getText().toString();

            //TODO Controlli

            Fragment fragment_signup2 = new Fragment(R.layout.fragment_sign_up2);
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.signUpMain, fragment_signup2);
            //fragmentTransaction.addToBackStack(fragment.toString());
            fragmentTransaction.commit();
        });

    }

}
