package it.uniba.di.sms1920.everit.customer.activities;

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
import com.here.android.mpa.search.AutoSuggest;
import com.here.android.mpa.search.AutoSuggestPlace;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.TextAutoSuggestionRequest;

import java.util.List;
import java.util.Locale;

import it.uniba.di.sms1920.everit.customer.R;

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
    }


    private void search() {
        String query = editTextSearch.getText().toString();

        TextAutoSuggestionRequest textAutoSuggestionRequest = new TextAutoSuggestionRequest(query);
        GeoCoordinate center = new GeoCoordinate(41.107834, 16.880462);
        textAutoSuggestionRequest.setSearchCenter(center);
        textAutoSuggestionRequest.setCollectionSize(10);
        textAutoSuggestionRequest.setLocale(Locale.getDefault());
        textAutoSuggestionRequest.execute((autoSuggests, errorCode) -> {
            if (autoSuggests != null) {
                for (AutoSuggest suggest : autoSuggests) {
                    Log.d("RICHIESTA", suggest.getTitle());
                }
            }
            else if (errorCode != null) {
                Log.e("RICHIESTA", errorCode.toString());
            }
        });
    }
}
