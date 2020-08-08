package it.uniba.di.sms1920.everit.customer.activities.accountDetail;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.BaseActivity;

public class AccountDetailFragment extends Fragment {

    AccountDetailActivity mParent;
    private LinearLayout linearLayoutAccountInfo;
    private LinearLayout linearLayoutChangePassword;

    public AccountDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_account_detail, parent, false);
        this.initComponent(viewRoot);
        return viewRoot;
    }


    private void initComponent(View viewRoot){

        linearLayoutAccountInfo = viewRoot.findViewById(R.id.linearLayoutAccountInfo);
        linearLayoutAccountInfo.setOnClickListener(v -> {
            ProfileFragment profileFragment = new ProfileFragment();
            FragmentTransaction transaction = mParent.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.containerSettingsCustomer, profileFragment).addToBackStack(null).commit();
        });

        linearLayoutChangePassword = viewRoot.findViewById(R.id.linearLayoutChangePassword);
        linearLayoutChangePassword.setOnClickListener(v -> {
            PrivacySecurityFragment privacySecurityFragment = new PrivacySecurityFragment();
            FragmentTransaction fragmentTransaction = mParent.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerSettingsCustomer, privacySecurityFragment).addToBackStack(null).commit();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AccountDetailActivity){
            mParent = (AccountDetailActivity) context;
        }
    }
}
