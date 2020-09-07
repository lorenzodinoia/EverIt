package it.uniba.di.sms1920.everit.customer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import it.uniba.di.sms1920.everit.customer.DeliveryAddress;
import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.results.ResultListActivity;
import it.uniba.di.sms1920.everit.utils.Address;

import static android.app.Activity.RESULT_OK;

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
        editTextSearch.requestFocus();
        buttonSearch = viewRoot.findViewById(R.id.btnSearch);

        buttonSearch.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), AddressChooserActivity.class);
            Editable editableQuery = this.editTextSearch.getText();
            if (editableQuery != null) {
                String query = editableQuery.toString().trim();
                if (!query.equals("")) {
                    intent.putExtra(AddressChooserActivity.PARAMETER_QUERY, this.editTextSearch.getText().toString());
                }
            }
            startActivityForResult(intent, AddressChooserActivity.REQUEST_ADDRESS);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if ((requestCode == AddressChooserActivity.REQUEST_ADDRESS) && (resultCode == RESULT_OK)
                && (data != null) && (data.hasExtra(AddressChooserActivity.RESULT_ADDRESS))) {
            Address chosenAddress = data.getParcelableExtra(AddressChooserActivity.RESULT_ADDRESS);
            if (chosenAddress != null) {
                this.handleSearchResult(chosenAddress);
            }
        }
    }

    private void handleSearchResult(Address address) {
        DeliveryAddress.set(address);
        Intent searchIntent = new Intent(getActivity().getApplicationContext(), ResultListActivity.class);
        searchIntent.putExtra(ResultListActivity.PARAMETER_ADDRESS, address);
        startActivity(searchIntent);
    }
}
