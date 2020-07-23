package it.uniba.di.sms1920.everit.customer.activities.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.uniba.di.sms1920.everit.customer.ProductRecyclerViewAdapter;
import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Product;

public class OrderDetailFragment extends Fragment {
    static final String ARG_ITEM_ID = "item_id";
    private Order order;
    public OrderDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getArguments() != null) && (getArguments().containsKey(ARG_ITEM_ID))) {
            long id = getArguments().getLong(ARG_ITEM_ID);
            this.order = OrderListActivity.getOrderById(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_detail, container, false);

        if (order != null) {
            TextView textViewDeliveryAddress = rootView.findViewById(R.id.textViewDeliveryAddress);
            TextView textViewTotalPrice = rootView.findViewById(R.id.textViewPrice);
            TextView textViewOrderTime = rootView.findViewById(R.id.textViewOrderDateTime);
            TextView textViewDeliveryTime = rootView.findViewById(R.id.textViewDeliveryDateTime);
            TextView textViewOrderNotes = rootView.findViewById(R.id.textViewOrderNotesBox);
            TextView textViewDeliveryNotes = rootView.findViewById(R.id.textViewDeliveryNotesBox);
            RecyclerView recyclerView = rootView.findViewById(R.id.recycleViewProducts);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
            LocalDateTime createdAt = order.getCreatedAt();
            LocalDateTime estimatedDeliveryTime = order.getEstimatedDeliveryTime();

            String orderTimeAsString = "";
            if(createdAt != null) {
                orderTimeAsString = createdAt.format(formatter);
            }

            String deliveryTimeAsString = "";
            if (order.isDelivered()) {
                if (order.getActualDeliveryTime() != null) {
                    deliveryTimeAsString = estimatedDeliveryTime.format(formatter);
                }
            }
            else {
                //TODO Aggiungere un simbolo per segnalare che l'ordine è ancora da consegnare
                deliveryTimeAsString = estimatedDeliveryTime.format(formatter);
            }

            textViewDeliveryAddress.setText(order.getDeliveryAddress().getAddress());
            textViewOrderTime.setText(orderTimeAsString);
            textViewDeliveryTime.setText(deliveryTimeAsString);
            textViewOrderNotes.setText(order.getOrderNotes());
            textViewDeliveryNotes.setText(order.getDeliveryNotes());
            textViewTotalPrice.setText(String.format(Locale.getDefault(), "€ %.2f", order.getTotalCost()));

            recyclerView.setAdapter(new ProductRecyclerViewAdapter(order.getProducts()));
        }

        return rootView;
    }

}
