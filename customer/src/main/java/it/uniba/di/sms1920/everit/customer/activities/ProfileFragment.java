package it.uniba.di.sms1920.everit.customer.activities;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import it.uniba.di.sms1920.everit.customer.R;

public class ProfileFragment extends Fragment {

    private TextInputEditText editTextName;
    private TextInputEditText editTextSurname;
    private TextInputEditText editTextPhone;
    private TextInputEditText editTextMail;
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
        this.initComponent(viewRoot);
        return viewRoot;
    }

    private void initComponent(View viewRoot){

        editTextName = viewRoot.findViewById(R.id.editTextName);
        editTextName.setEnabled(false);

        editTextSurname = viewRoot.findViewById(R.id.editTextSurname);
        editTextSurname.setEnabled(false);

        editTextMail =  viewRoot.findViewById(R.id.editTextMail);
        editTextMail.setEnabled(false);

        editTextPhone= viewRoot.findViewById(R.id.editTextPhone);
        editTextPhone.setEnabled(false);

        buttonEditConfirm = viewRoot.findViewById(R.id.buttonEditConfirm);
        buttonEditConfirm.setTag("Edit");
        buttonEditConfirm.setOnClickListener(v -> {
            String status = (String) buttonEditConfirm.getTag();
            if(status.equals("Edit")) {
                buttonEditConfirm.setText("Confirm");
                editTextName.setEnabled(true);
                editTextSurname.setEnabled(true);
                editTextMail.setEnabled(true);
                editTextPhone.setEnabled(true);
                v.setTag("Confirm");
            } else {
                buttonEditConfirm.setText("Edit");
                editTextName.setEnabled(false);
                editTextSurname.setEnabled(false);
                editTextMail.setEnabled(false);
                editTextPhone.setEnabled(false);
                v.setTag("Edit");
            }
        });
    }


}

