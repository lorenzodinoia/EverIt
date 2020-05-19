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
import it.uniba.di.sms1920.everit.customer.activities.results.ResultListActivity;
import it.uniba.di.sms1920.everit.utils.Address;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

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

        buttonSearch.setOnClickListener(view -> this.search());
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        RestaurateurRequest request = new RestaurateurRequest();
        request.read(1, new RequestListener<Restaurateur>() {
            @Override
            public void successResponse(Restaurateur response) {
                Restaurateur a = response;
            }

            @Override
            public void errorResponse(RequestException error) {

            }
        });
    }


    private void search() {
        Intent intent = new Intent(getActivity().getApplicationContext(), ResultListActivity.class);
        Address address = new Address("via Edoardo Orabona", "Bari", 41.107798, 16.880203);
        intent.putExtra(ResultListActivity.PARAMETER_ADDRESS, address);
        startActivity(intent);

    }
}
