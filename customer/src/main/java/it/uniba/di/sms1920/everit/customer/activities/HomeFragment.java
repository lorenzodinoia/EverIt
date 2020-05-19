package it.uniba.di.sms1920.everit.customer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import it.uniba.di.sms1920.everit.customer.R;

public class HomeFragment extends Fragment {

    private TextInputEditText editTextSearch;
    private MaterialButton buttonSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_home, parent, false);
        this.initComponent(viewRoot);
        return viewRoot;
    }

    private void initComponent(View viewRoot){
        editTextSearch = viewRoot.findViewById(R.id.editTextSearch);
        buttonSearch = viewRoot.findViewById(R.id.btnSearch);

        buttonSearch.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), AddressChooserActivity.class);
            intent.putExtra(AddressChooserActivity.PARAMETER_QUERY, this.editTextSearch.getText().toString());
            startActivityForResult(intent, AddressChooserActivity.REQUEST_ADDRESS);
        });
    }
}
