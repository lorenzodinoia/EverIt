package it.uniba.di.sms1920.everit.customer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.results.ResultListActivity;
import it.uniba.di.sms1920.everit.utils.Address;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private EditText editTextSearch;
    private Button buttonSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_home, parent, false);
        this.initComponent(viewRoot);
        return viewRoot;
    }

    private void initComponent(View viewRoot){
        editTextSearch = (EditText) viewRoot.findViewById(R.id.editTextSearch);
        buttonSearch = (Button) viewRoot.findViewById(R.id.btnSearch);

        buttonSearch.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), AddressChooserActivity.class);
            intent.putExtra(AddressChooserActivity.PARAMETER_QUERY, this.editTextSearch.getText().toString());
            startActivityForResult(intent, AddressChooserActivity.REQUEST_ADDRESS);
        });
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
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
        Intent searchIntent = new Intent(getActivity().getApplicationContext(), ResultListActivity.class);
        searchIntent.putExtra(ResultListActivity.PARAMETER_ADDRESS, address);
        startActivity(searchIntent);
    }
}
