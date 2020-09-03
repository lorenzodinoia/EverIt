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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.models.Rider;
import it.uniba.di.sms1920.everit.utils.request.CustomerRequest;
import it.uniba.di.sms1920.everit.utils.request.RiderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class AccountInfoFragment extends Fragment {

    private AccountDetailActivity mParent;
    private Rider rider;
    private TextInputEditText editTextName;
    private TextInputLayout editTextNameContainer;
    private TextInputEditText editTextSurname;
    private TextInputLayout editTextSurnameContainer;
    private TextInputEditText editTextPhone;
    private TextInputLayout editTextPhoneContainer;
    private TextInputEditText editTextMail;
    private TextInputLayout editTextMailContainer;
    private MaterialButton buttonEditConfirm;

    public AccountInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                if (bundle.containsKey(AccountDetailActivity.ARG_RIDER)) {
                    rider = bundle.getParcelable(AccountDetailActivity.ARG_RIDER);
                }
            }
        }
        else{
            rider = savedInstanceState.getParcelable(AccountDetailActivity.ARG_RIDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_account_info, container, false);
        initUi(viewRoot);

        return viewRoot;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initData(){
        if(rider != null) {
            editTextName.setText(rider.getName());
            editTextName.setEnabled(false);

            editTextSurname.setText(rider.getSurname());
            editTextSurname.setEnabled(false);

            editTextMail.setText(rider.getEmail());
            editTextMail.setEnabled(false);

            editTextPhone.setText(rider.getPhoneNumber());
            editTextPhone.setEnabled(false);

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
                        RiderRequest riderRequest = new RiderRequest();
                        rider.setName(editTextName.getText().toString());
                        rider.setSurname(editTextSurname.getText().toString());
                        rider.setEmail(editTextMail.getText().toString());
                        rider.setPhoneNumber(editTextPhone.getText().toString());
                        riderRequest.update(rider, new RequestListener<Rider>() {
                            @Override
                            public void successResponse(Rider response) {
                                rider = response;

                                Snackbar.make(buttonEditConfirm, R.string.account_updated, Snackbar.LENGTH_SHORT).show();
                                buttonEditConfirm.setText(R.string.edit);
                                editTextName.setText(rider.getName());
                                editTextName.setEnabled(false);
                                editTextSurname.setText(rider.getSurname());
                                editTextSurname.setEnabled(false);
                                editTextMail.setText(rider.getEmail());
                                editTextMail.setEnabled(false);
                                editTextMail.setText(rider.getEmail());
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
    }

    private void initUi(View viewRoot){
        editTextNameContainer = viewRoot.findViewById(R.id.editTextNameContainer);
        editTextName = viewRoot.findViewById(R.id.editTextName);
        editTextSurnameContainer = viewRoot.findViewById(R.id.editTextSurnameContainer);
        editTextSurname = viewRoot.findViewById(R.id.editTextSurname);
        editTextMailContainer = viewRoot.findViewById(R.id.editTextMailContainer);
        editTextMail = viewRoot.findViewById(R.id.editTextMail);
        editTextPhoneContainer = viewRoot.findViewById(R.id.editTextPhoneContainer);
        editTextPhone = viewRoot.findViewById(R.id.editTextPhone);
        buttonEditConfirm = viewRoot.findViewById(R.id.buttonEditConfirm);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AccountDetailActivity){
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(AccountDetailActivity.ARG_RIDER, rider);
    }
}