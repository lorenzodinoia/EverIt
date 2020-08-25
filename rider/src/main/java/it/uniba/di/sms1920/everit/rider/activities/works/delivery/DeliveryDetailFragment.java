package it.uniba.di.sms1920.everit.rider.activities.works.delivery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;

public class DeliveryDetailFragment extends Fragment {
    public static final String ARG_ITEM = "item";
    public static final String ARG_ITEM_ID = "item_id";
    private static final String SAVED_DELIVERY = "saved.delivery";

    private Order delivery;
    private TextView textViewCustomer, textViewOrderNumber, textViewCustomerAddress, textViewCustomerPhone, textViewRestaurateurName,
            textViewEstimatedDeliveryTime, textViewDeliveryNotes;
    private LinearLayout linearLayoutCustomerAddress, linearLayoutCustomerPhone;
    private CardView cardViewDeliveryNotes;
    private MaterialButton buttonOrderDelivery;

    public DeliveryDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            if (arguments != null && arguments.containsKey(ARG_ITEM)) {
                this.delivery = arguments.getParcelable(ARG_ITEM);
            }
        }
        else {
            this.delivery = savedInstanceState.getParcelable(SAVED_DELIVERY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_detail, container, false);
        this.initUi(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.delivery != null) {
            this.initData();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_DELIVERY, this.delivery);
    }

    private void initUi(View view) {
        this.textViewCustomer = view.findViewById(R.id.textViewCustomer);
        this.textViewOrderNumber = view.findViewById(R.id.textViewOrderNumberDeliver);

        this.linearLayoutCustomerAddress = view.findViewById(R.id.linearLayoutCustomerAddress);
        this.textViewCustomerAddress = view.findViewById(R.id.textViewCustomerAddress);

        this.linearLayoutCustomerPhone = view.findViewById(R.id.linearLayoutCustomerPhone);
        this.textViewCustomerPhone = view.findViewById(R.id.textViewCustomerPhone);

        this.textViewRestaurateurName = view.findViewById(R.id.textViewRestaurateurName);
        this.textViewEstimatedDeliveryTime = view.findViewById(R.id.textViewEstimatedDeliveryTime);

        this.cardViewDeliveryNotes = view.findViewById(R.id.cardViewDeliveryNotes);
        this.textViewDeliveryNotes = view.findViewById(R.id.textViewDeliveryNotes);

        this.buttonOrderDelivery = view.findViewById(R.id.buttonOrderDelivery);
        this.buttonOrderDelivery.setOnClickListener(v -> {
            //TODO Click sul pulsante per la consegna

        });
    }

    private void initData() {
        this.textViewCustomer.setText(delivery.getCustomer().getFullName());
        this.textViewOrderNumber.setText(String.format(Locale.getDefault(), "#%d", this.delivery.getId()));

        this.linearLayoutCustomerAddress.setOnClickListener(v -> {
            startMap(delivery.getDeliveryAddress().getLatitude(),
                    delivery.getDeliveryAddress().getLongitude(),
                    "");
        });
        this.textViewCustomerAddress.setText(this.delivery.getDeliveryAddress().getFullAddress());

        this.linearLayoutCustomerPhone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + delivery.getCustomer().getPhoneNumber()));
            startActivity(intent);
        });
        this.textViewCustomerPhone.setText(delivery.getCustomer().getPhoneNumber());

        this.textViewRestaurateurName.setText(this.delivery.getRestaurateur().getShopName());

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
        this.textViewEstimatedDeliveryTime.setText(timeFormatter.format(this.delivery.getEstimatedDeliveryTime()));


        String deliveryNotes = this.delivery.getDeliveryNotes();
        if (deliveryNotes != null && deliveryNotes.length() > 0) {
            this.cardViewDeliveryNotes.setVisibility(View.VISIBLE);
            this.textViewDeliveryNotes.setText(deliveryNotes);
        }
    }

    private void startMap(double latitude, double longitude, String nameLocation) {
        Uri mapsUri = Uri.parse(String.format(Locale.getDefault(),"http://maps.google.com/maps?q=loc:%f,%f (%s)", latitude, longitude, nameLocation));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapsUri);
        startActivity(mapIntent);
    }
}