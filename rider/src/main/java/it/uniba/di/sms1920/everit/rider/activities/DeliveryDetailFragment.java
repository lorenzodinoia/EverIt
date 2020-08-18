package it.uniba.di.sms1920.everit.rider.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.RiderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class DeliveryDetailFragment extends Fragment {
    public static final String ARG_ITEM = "item";
    public static final String ARG_ITEM_ID = "item_id";

    private Order delivery;
    private TextView textViewCustomer;
    private TextView textViewCustomerAddress;
    private TextView textViewCustomerPhone;
    private TextView textViewRestaurateurName;
    private TextView textViewOrderNumber;
    private TextView textViewEstimatedDeliveryTime;
    private TextView textViewDeliveryNotes;
    private MaterialCardView cardDeliveryNotes;
    private MaterialButton buttonOrderDelivery;

    public DeliveryDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(ARG_ITEM)) {
                this.delivery = arguments.getParcelable(ARG_ITEM);
            }
            else if (arguments.containsKey(ARG_ITEM_ID)) {
                long orderId = arguments.getLong(ARG_ITEM_ID);
                RiderRequest riderRequest = new RiderRequest();
                riderRequest.readDelivery(orderId, new RequestListener<Order>() {
                    @Override
                    public void successResponse(Order response) {
                        delivery = response;
                        initComponents();
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        Utility.showGenericMessage(getContext(), getString(R.string.message_generic_error), error.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_detail, container, false);

        this.textViewCustomer = view.findViewById(R.id.textViewCustomer);
        this.textViewCustomerAddress = view.findViewById(R.id.textViewCustomerAddress);
        this.textViewCustomerPhone = view.findViewById(R.id.textViewCustomerPhone);
        this.textViewRestaurateurName = view.findViewById(R.id.textViewRestaurateurName);
        this.textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
        this.textViewEstimatedDeliveryTime = view.findViewById(R.id.textViewEstimatedDeliveryTime);
        this.cardDeliveryNotes = view.findViewById(R.id.cardDeliveryNotes);
        this.textViewDeliveryNotes = view.findViewById(R.id.textViewDeliveryNotes);
        this.buttonOrderDelivery = view.findViewById(R.id.buttonOrderDelivery);
        this.buttonOrderDelivery.setOnClickListener(v -> {
            //TODO Click sul pulsante per la consegna
        });

        if (this.delivery != null) {
            this.initComponents();
        }

        return view;
    }

    private void initComponents() {
        //TODO Aggiungere cliente all'ordine
        this.textViewCustomerAddress.setText(this.delivery.getDeliveryAddress().getFullAddress());
        this.textViewRestaurateurName.setText(this.delivery.getRestaurateur().getShopName());
        this.textViewOrderNumber.setText(String.format(Locale.getDefault(), "%s %d", getContext().getString(R.string.label_order_number), this.delivery.getId()));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
        this.textViewEstimatedDeliveryTime.setText(timeFormatter.format(this.delivery.getEstimatedDeliveryTime()));
        String deliveryNotes = this.delivery.getDeliveryNotes();
        if (deliveryNotes != null && deliveryNotes.length() > 0) {
            this.cardDeliveryNotes.setVisibility(View.VISIBLE);
            this.textViewDeliveryNotes.setText(deliveryNotes);
        }
    }
}