package it.uniba.di.sms1920.everit.customer.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.di.sms1920.everit.customer.R;

public class ReviewFragment extends Fragment {

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_review, parent, false);
        this.initComponent(viewRoot);
        return viewRoot;
    }

    private void initComponent(View viewRoot){



    }
}
