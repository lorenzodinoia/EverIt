package it.uniba.di.sms1920.everit.customer.results.Tabs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;

public class InfoFragment extends Fragment {
    public static final String ARG_ITEM = "item";
    private static final String SAVED_RESTAURATEUR = "saved.restaurateur";

    private Restaurateur restaurateur;

    private TextView textViewPhoneNumber, textViewAddress, textViewOpenClosed, textViewDeliveryCost, textViewMinPurchase;
    private ImageView imageViewOpenClosed;
    private LinearLayout layoutCall, layoutAddress;
    private MaterialButton buttonCall, buttonMap;

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
        imageViewOpenClosed = view.findViewById(R.id.imageViewOpenClosed);

        buttonCall = view.findViewById(R.id.buttonCall);
        buttonMap = view.findViewById(R.id.buttonMap);
    }

    private void initData() {
        textViewPhoneNumber.setText(restaurateur.getPhoneNumber());
        textViewAddress.setText(restaurateur.getAddress().getAddress());
        textViewDeliveryCost.setText(String.valueOf(restaurateur.getDeliveryCost()));
        textViewMinPurchase.setText(String.valueOf(restaurateur.getMinPrice()));

        layoutCall.setOnClickListener(v -> callShop());
        buttonCall.setOnClickListener(v -> callShop());

        layoutAddress.setOnClickListener(v -> Utility.showLocationOnMap(getContext(), restaurateur.getAddress().getLatitude(), restaurateur.getAddress().getLongitude(), restaurateur.getShopName()));
        buttonMap.setOnClickListener(v -> Utility.showLocationOnMap(getContext(), restaurateur.getAddress().getLatitude(), restaurateur.getAddress().getLongitude(), restaurateur.getShopName()));

        if (restaurateur.isOpen()) {
            imageViewOpenClosed.setImageResource(it.uniba.di.sms1920.everit.utils.R.drawable.ic_open_40px);
            textViewOpenClosed.setText(getString(R.string.open_shop));
        }
        else {
            imageViewOpenClosed.setImageResource(it.uniba.di.sms1920.everit.utils.R.drawable.ic_close_40px);
            textViewOpenClosed.setText(getString(R.string.closed_shop));
        }
    }

    private void callShop(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + restaurateur.getPhoneNumber()));
        startActivity(intent);
    }
}