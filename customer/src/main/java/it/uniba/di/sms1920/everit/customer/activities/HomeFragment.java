package it.uniba.di.sms1920.everit.customer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.search.AddressFilter;
import com.here.android.mpa.search.AutoSuggest;
import com.here.android.mpa.search.AutoSuggestPlace;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.Place;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;
import com.here.android.mpa.search.TextAutoSuggestionRequest;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.results.ResultListActivity;
import it.uniba.di.sms1920.everit.utils.Address;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

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



        /*
        String query = editTextSearch.getText().toString();

        GeoCoordinate center = new GeoCoordinate(41.107834, 16.880462);
        AddressFilter italyFilter = new AddressFilter();
        italyFilter.setCounty("IT");

        TextAutoSuggestionRequest textAutoSuggestionRequest = new TextAutoSuggestionRequest(query);
        textAutoSuggestionRequest.setFilters(EnumSet.of(TextAutoSuggestionRequest.AutoSuggestFilterType.ADDRESS));
        //textAutoSuggestionRequest.setAddressFilter(italyFilter);
        textAutoSuggestionRequest.setSearchCenter(center);
        textAutoSuggestionRequest.setCollectionSize(10);
        textAutoSuggestionRequest.setLocale(Locale.getDefault());

        textAutoSuggestionRequest.execute((autoSuggests, errorCode) -> {
            if (autoSuggests != null) {
                for (AutoSuggest suggest : autoSuggests) {
                    Log.d("RICHIESTA", suggest.getTitle());

                }
                //Solo per debug prende i dettagli del primo luogo suggerito e avvia la ricerca
                AutoSuggest suggest = autoSuggests.get(0);
                if (suggest instanceof AutoSuggestPlace) {
                    AutoSuggestPlace suggestPlace = (AutoSuggestPlace) suggest;
                    suggestPlace.getPlaceDetailsRequest().execute((place, errorCode1) -> {
                        if (place != null) {
                            //TODO Ricerca dei ristoranti
                            Intent intent = new Intent(getActivity().getApplicationContext(), ResultListActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
            else if (errorCode != null) {
                Log.e("RICHIESTA", errorCode.toString());
            }
        });

         */
    }
}
