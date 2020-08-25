package it.uniba.di.sms1920.everit.rider.activities.accountDetail;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.models.Rider;
import it.uniba.di.sms1920.everit.utils.request.RiderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class AccountDetailFragment extends Fragment {

    private Rider rider;
    private AccountDetailActivity mParent;

    TextView textViewRiderNameAccountDetail;
    TextView textViewEmailRiderAccountDetail;
    LinearLayout linearLayoutAccountInfo;
    LinearLayout linearLayoutChangePassword;

    public AccountDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            rider = savedInstanceState.getParcelable(AccountDetailActivity.ARG_RIDER);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_detail, container, false);
        initUi(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(rider != null) {
            initData();
        }
        else{
            RiderRequest riderRequest = new RiderRequest();
            riderRequest.readCurrent(new RequestListener<Rider>() {
                @Override
                public void successResponse(Rider response) {
                    rider = response;
                    initData();
                }

                @Override
                public void errorResponse(RequestException error) {
                    promptErrorMessage(error.getMessage());
                }
            });
        }
    }

    private void initData(){
        if(rider != null) {
            textViewRiderNameAccountDetail.setText(rider.getFullName());
            textViewEmailRiderAccountDetail.setText(rider.getEmail());
            linearLayoutAccountInfo.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable(AccountDetailActivity.ARG_RIDER, rider);
                AccountInfoFragment fragment = new AccountInfoFragment();
                fragment.setArguments(bundle);
                mParent.getSupportFragmentManager().beginTransaction().replace(R.id.containerAccountDetailRider, fragment).addToBackStack(null).commit();
            });
            linearLayoutChangePassword.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable(AccountDetailActivity.ARG_RIDER, rider);
                PrivacyAndSecurityFragment fragment = new PrivacyAndSecurityFragment();
                fragment.setArguments(bundle);
                mParent.getSupportFragmentManager().beginTransaction().replace(R.id.containerAccountDetailRider, fragment).addToBackStack(null).commit();
            });
        }
    }

    private void initUi(View view){
        textViewRiderNameAccountDetail = view.findViewById(R.id.textViewRiderNameAccountDetail);
        textViewEmailRiderAccountDetail = view.findViewById(R.id.textViewEmailRiderAccountDetail);
        linearLayoutAccountInfo = view.findViewById(R.id.linearLayoutAccountInfo);
        linearLayoutChangePassword = view.findViewById(R.id.linearLayoutChangePassword);
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
            mParent.finish();
        });

        dialog.show();
    }
}