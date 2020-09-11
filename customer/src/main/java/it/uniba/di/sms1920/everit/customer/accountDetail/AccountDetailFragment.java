package it.uniba.di.sms1920.everit.customer.accountDetail;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Customer;

public class AccountDetailFragment extends Fragment {
    public static final String ITEM = "item";
    private static final String SAVED_CUSTOMER = "saved.customer";

    private Customer customer;

    private AppCompatActivity parentActivity;
    private TextView textViewCustomerNameAccountDetail, textViewEmailCustomerAccountDetail;
    private LinearLayout linearLayoutAccountInfo, linearLayoutChangePassword;

    public AccountDetailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_account_detail, parent, false);
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
        textViewCustomerNameAccountDetail = view.findViewById(R.id.textViewCustomerNameAccountDetail);
        textViewEmailCustomerAccountDetail = view.findViewById(R.id.textViewEmailCustomerAccountDetail);
        linearLayoutAccountInfo = view.findViewById(R.id.linearLayoutAccountInfo);
        linearLayoutChangePassword = view.findViewById(R.id.linearLayoutChangePassword);
    }

    private void initData() {
        this.textViewCustomerNameAccountDetail.setText(this.customer.getFullName());
        this.textViewEmailCustomerAccountDetail.setText(this.customer.getEmail());
        this.linearLayoutAccountInfo.setOnClickListener(v -> {
            ProfileFragment profileFragment = new ProfileFragment();
            Bundle profileFragmentArguments = new Bundle();
            profileFragmentArguments.putParcelable(ProfileFragment.ITEM, this.customer);
            profileFragment.setArguments(profileFragmentArguments);

            FragmentTransaction transaction = this.parentActivity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.containerSettingsCustomer, profileFragment).addToBackStack(null).commit();
        });
        this.linearLayoutChangePassword.setOnClickListener(v -> {
            PrivacySecurityFragment privacySecurityFragment = new PrivacySecurityFragment();
            FragmentTransaction fragmentTransaction = this.parentActivity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerSettingsCustomer, privacySecurityFragment).addToBackStack(null).commit();
        });
    }
}
