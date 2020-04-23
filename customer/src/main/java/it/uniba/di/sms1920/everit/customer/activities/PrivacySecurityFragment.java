package it.uniba.di.sms1920.everit.customer.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import it.uniba.di.sms1920.everit.customer.R;


public class PrivacySecurityFragment extends Fragment {

    private EditText oldPassword;
    private EditText newPassword;
    private EditText newPasswordConf;
    private Button btnDeleteAccount;
    private Button btnChangePassword;


    public PrivacySecurityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_privacy_security, parent, false);
        this.initComponent(viewRoot);
        return viewRoot;
    }

    private void initComponent(View viewRoot) {
        oldPassword = viewRoot.findViewById(R.id.EditTextOldPassword);
        newPassword = viewRoot.findViewById(R.id.EditTextNewPassword);
        newPasswordConf = viewRoot.findViewById(R.id.EditTextNewPasswordConf);

        btnChangePassword = viewRoot.findViewById(R.id.btnChangePass);
        btnChangePassword.setOnClickListener(view -> {
            String oldPass = oldPassword.getText().toString();
            String newPass = newPassword.getText().toString();
            String newPassConf = newPasswordConf.getText().toString();

            if(newPass.equals(newPassConf)){
                //prosegui
            }else{
                //errore
            }
        });

        btnDeleteAccount = viewRoot.findViewById(R.id.btnDeleteAccount);
        btnDeleteAccount.setOnClickListener(view -> {
            View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window_confirm, null);
            final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setElevation(20);
            popupWindow.setOverlapAnchor(true);

            Button btnYes = popupView.findViewById(R.id.btnYes);
            Button btnNo = popupView.findViewById(R.id.btnNo);

            btnNo.setOnClickListener(PopupWindow -> popupWindow.dismiss());


            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        });
    }


}
