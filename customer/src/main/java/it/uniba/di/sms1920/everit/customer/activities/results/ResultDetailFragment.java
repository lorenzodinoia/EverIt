package it.uniba.di.sms1920.everit.customer.activities.results;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;


public class ResultDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private Restaurateur restaurateur;

    public ResultDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getArguments() != null) && (getArguments().containsKey(ARG_ITEM_ID))) {
            long id = getArguments().getLong(ARG_ITEM_ID);
            this.restaurateur = ResultListActivity.getResultById(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.result_detail, container, false);

        //TODO Mapping dati ristorante

        return rootView;
    }
}
