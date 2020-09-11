package it.uniba.di.sms1920.everit.rider.deliverHistory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.format.DateTimeFormatter;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;

public class DeliveryHistoryDetailFragment extends Fragment {

    private static final String SAVED_DELIVERED = "delivered";
    public static final String ARG_ITEM_ID = "item_id";

    private Order order;

    private TextView textViewOrderNumber;
    private TextView textViewShopName;
    private TextView textViewShopAddress;
    private TextView textViewCustomerName;
    private TextView textViewCustomerAddress;
    private TextView textViewPickupTime;
    private TextView textViewDeliveryDateTime;
    private TextView textViewDeliveryNotes;

    public DeliveryHistoryDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            Bundle arguments = getArguments();
            if (arguments != null && arguments.containsKey(ARG_ITEM_ID)) {
                this.order = arguments.getParcelable(ARG_ITEM_ID);
            }
        }
        else {
            this.order = savedInstanceState.getParcelable(SAVED_DELIVERED);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.deliveryhistory_detail, container, false);
        initUi(rootView);
        return rootView;
    }

    private void initUi(View view){
        textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
        textViewShopName = view.findViewById(R.id.textViewShopName);
        textViewShopAddress = view.findViewById(R.id.textViewShopAddress);
        textViewCustomerName = view.findViewById(R.id.textViewCustomerName);
        textViewCustomerAddress = view.findViewById(R.id.textViewCustomerAddress);
        textViewPickupTime = view.findViewById(R.id.textViewPickupTime);
        textViewDeliveryDateTime = view.findViewById(R.id.textViewDeliveryDateTime);
        textViewDeliveryNotes = view.findViewById(R.id.textViewDeliveryNotes);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initData(){
        if(order != null) {
            textViewOrderNumber.setText("#" + order.getId());
            textViewShopName.setText(order.getRestaurateur().getShopName());
            textViewShopAddress.setText(order.getRestaurateur().getAddress().getFullAddress());
            textViewCustomerName.setText(order.getCustomer().getFullName());
            textViewCustomerAddress.setText(order.getDeliveryAddress().getFullAddress());
            textViewPickupTime.setText(order.getPickupTimeAsString());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
            textViewDeliveryDateTime.setText(order.getActualDeliveryTime().format(formatter));

            //textViewDeliveryDateTime.setText(order.getActualDeliveryTime());

            textViewDeliveryNotes.setText(order.getDeliveryNotes());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ARG_ITEM_ID, order);
    }
}