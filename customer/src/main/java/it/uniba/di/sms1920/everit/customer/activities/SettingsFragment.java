package it.uniba.di.sms1920.everit.customer.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.uniba.di.sms1920.everit.customer.R;

public class SettingsFragment extends Fragment {

    TextView a;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_settings, parent, false);
        this.initComponent(viewRoot);
        return viewRoot;
    }


    private void initComponent(View viewRoot){
        a = viewRoot.findViewById(R.id.textView);
    }
}
