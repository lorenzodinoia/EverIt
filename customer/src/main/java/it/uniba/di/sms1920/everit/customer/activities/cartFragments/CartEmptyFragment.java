package it.uniba.di.sms1920.everit.customer.activities.cartFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import it.uniba.di.sms1920.everit.customer.R;

public class CartEmptyFragment extends Fragment {

    public CartEmptyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_cart_empty, parent, false);

        return viewRoot;
    }

}
