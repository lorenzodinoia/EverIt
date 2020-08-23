package it.uniba.di.sms1920.everit.rider.activities.works.delivery;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.threeten.bp.LocalTime;
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
    private TextView textViewOrderNumber;
    private TextView textViewCustomerAddress;
    private TextView textViewCustomerPhone;
    private TextView textViewRestaurateurName;
    private TextView textViewEstimatedDeliveryTime;
    private TextView textViewDeliveryNotes;
    private LinearLayout linearLayoutCustomerAddress, linearLayoutCustomerPhone;
    private CardView cardViewDeliveryNotes;
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
                        promptErrorMessage(error.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_detail, container, false);

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

        if (this.delivery != null) {
            this.initComponents();
        }

        return view;
    }

    private void initComponents() {
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

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
            getActivity().finish();
        });

        dialog.show();
    }

    private void startMap(double latitude, double longitude, String nameLocation){
        Uri mapsUri = Uri.parse(String.format(Locale.getDefault(),"http://maps.google.com/maps?q=loc:%f,%f (%s)", latitude, longitude, nameLocation));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapsUri);
        startActivity(mapIntent);
    }
}