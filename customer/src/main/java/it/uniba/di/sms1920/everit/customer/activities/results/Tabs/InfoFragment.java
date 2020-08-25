package it.uniba.di.sms1920.everit.customer.activities.results.Tabs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.results.ResultDetailActivity;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;

public class InfoFragment extends Fragment {

    private ResultDetailActivity resultDetailActivity;
    private Restaurateur restaurateur;

    private TextView textViewPhoneNumber, textViewAddress, textViewOpenClosed, textViewDeliveryCost, textViewMinPurchase;
    private LinearLayout layoutCall, layoutAddress;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        textViewAddress = rootView.findViewById(R.id.textViewAddress);
        layoutAddress = rootView.findViewById(R.id.layoutAddress);

        textViewPhoneNumber = rootView.findViewById(R.id.textViewCall);
        layoutCall = rootView.findViewById(R.id.layoutCall);

        textViewDeliveryCost = rootView.findViewById(R.id.textViewDeliveryCost);
        textViewMinPurchase = rootView.findViewById(R.id.textViewMinPurchase);
        textViewOpenClosed = rootView.findViewById(R.id.textViewOpenClosed);

        this.initComponent();

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof  ResultDetailActivity){
            resultDetailActivity = (ResultDetailActivity) context;
            restaurateur = resultDetailActivity.passRestaurateur();
        }
    }

    private void initComponent(){
        textViewPhoneNumber.setText(restaurateur.getPhoneNumber());
        textViewAddress.setText(restaurateur.getAddress().getAddress());
        textViewDeliveryCost.setText(String.valueOf(restaurateur.getDeliveryCost()));
        textViewMinPurchase.setText(String.valueOf(restaurateur.getMinPrice()));

        layoutCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + restaurateur.getPhoneNumber()));
            startActivity(intent);
        });

        layoutAddress.setOnClickListener(v -> {
            startMap(restaurateur.getAddress().getLongitude(),
                    restaurateur.getAddress().getLatitude(),
                    restaurateur.getShopName());
        });

        if(restaurateur.isOpen()){
            textViewOpenClosed.setText(getString(R.string.open_shop));
        }else{
            textViewOpenClosed.setText(getString(R.string.closed_shop));
        }

    }

    private void startMap(double latitude, double longitude, String nameLocation){
        Uri mapsUri = Uri.parse(String.format(Locale.getDefault(),"http://maps.google.com/maps?q=loc:%f,%f (%s)", latitude, longitude, nameLocation));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapsUri);
        startActivity(mapIntent);
    }


}