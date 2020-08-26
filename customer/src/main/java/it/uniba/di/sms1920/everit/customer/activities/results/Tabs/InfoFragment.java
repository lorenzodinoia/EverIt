package it.uniba.di.sms1920.everit.customer.activities.results.Tabs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;

public class InfoFragment extends Fragment {
    public static final String ARG_ITEM = "item";
    private static final String SAVED_RESTAURATEUR = "saved.restaurateur";

    private Restaurateur restaurateur;

    private TextView textViewPhoneNumber, textViewAddress, textViewOpenClosed, textViewDeliveryCost, textViewMinPurchase;
    private LinearLayout layoutCall, layoutAddress;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            if ((arguments != null) && (arguments.containsKey(ARG_ITEM))) {
                this.restaurateur = arguments.getParcelable(ARG_ITEM);
            }
        }
        else if (savedInstanceState.containsKey(SAVED_RESTAURATEUR)) {
            this.restaurateur = savedInstanceState.getParcelable(SAVED_RESTAURATEUR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        this.initUi(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.restaurateur != null) {
            this.initData();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_RESTAURATEUR, this.restaurateur);
    }

    private void initUi(View view) {
        textViewAddress = view.findViewById(R.id.textViewAddress);
        layoutAddress = view.findViewById(R.id.layoutAddress);

        textViewPhoneNumber = view.findViewById(R.id.textViewCall);
        layoutCall = view.findViewById(R.id.layoutCall);

        textViewDeliveryCost = view.findViewById(R.id.textViewDeliveryCost);
        textViewMinPurchase = view.findViewById(R.id.textViewMinPurchase);
        textViewOpenClosed = view.findViewById(R.id.textViewOpenClosed);
    }

    private void initData() {
        textViewPhoneNumber.setText(restaurateur.getPhoneNumber());
        textViewAddress.setText(restaurateur.getAddress().getAddress());
        textViewDeliveryCost.setText(String.valueOf(restaurateur.getDeliveryCost()));
        textViewMinPurchase.setText(String.valueOf(restaurateur.getMinPrice()));

        layoutCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + restaurateur.getPhoneNumber()));
            startActivity(intent);
        });

        layoutAddress.setOnClickListener(v -> startMap(restaurateur.getAddress().getLongitude(), restaurateur.getAddress().getLatitude(), restaurateur.getShopName()));

        if (restaurateur.isOpen()) {
            textViewOpenClosed.setText(getString(R.string.open_shop));
        }
        else {
            textViewOpenClosed.setText(getString(R.string.closed_shop));
        }
    }

    private void startMap(double latitude, double longitude, String nameLocation){
        Uri mapsUri = Uri.parse(String.format(Locale.getDefault(),"http://maps.google.com/maps?q=loc:%f,%f (%s)", latitude, longitude, nameLocation));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapsUri);
        startActivity(mapIntent);
    }
}