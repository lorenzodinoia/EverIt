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

import it.uniba.di.sms1920.everit.customer.ProductRecyclerViewAdapter;
import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.orders.tab.OrderTabManagerFragment;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;

public class OrderDetailFragment extends Fragment {

    static final String ARG_ITEM_ID = "item_id";
    private Order order;

    public OrderDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(OrderTabManagerFragment.ARG_ORDER_KEY)){
            order = bundle.getParcelable(OrderTabManagerFragment.ARG_ORDER_KEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_detail, container, false);

        if (order != null) {
            TextView textViewOrderNumber = rootView.findViewById(R.id.textViewOrderNumber);
            TextView textViewOrderType = rootView.findViewById(R.id.textViewOrderType);
            TextView labelDeliveryAddress = rootView.findViewById(R.id.labelDeliveryAddress);
            TextView textViewDeliveryAddress = rootView.findViewById(R.id.textViewDeliveryAddress);
            TextView textViewOrderTime = rootView.findViewById(R.id.textViewOrderDateTime);
            TextView labelDeliveryDate = rootView.findViewById(R.id.labelDeliveryDate);
            TextView textViewDeliveryTime = rootView.findViewById(R.id.textViewDeliveryDateTime);
            TextView textViewOrderStatus = rootView.findViewById(R.id.textViewOrderStatus);

            TextView textViewSubTotal = rootView.findViewById(R.id.textViewSubTotal);
            TextView textViewDeliveryCost = rootView.findViewById(R.id.textViewDeliveryCost);
            TextView textViewTotalPrice = rootView.findViewById(R.id.textViewTotalPrice);


            RecyclerView recyclerView = rootView.findViewById(R.id.recycleViewProducts);

            textViewOrderNumber.setText("#"+order.getId());
            if(order.getOrderType().equals(Order.OrderType.HOME_DELIVERY)){
                textViewOrderType.setText(R.string.home_delivery);
                labelDeliveryAddress.setText(R.string.delivery_address);
                textViewDeliveryAddress.setText(order.getDeliveryAddress().getFullAddress());
                labelDeliveryDate.setText(R.string.delivery_date_label);
            }
            else{
                textViewOrderType.setText(R.string.take_away);
                labelDeliveryAddress.setText(R.string.pickup_address);
                textViewDeliveryAddress.setText(order.getRestaurateur().getAddress().getFullAddress());
                labelDeliveryDate.setText(R.string.pickup_date);
            }

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
                deliveryTimeAsString = estimatedDeliveryTime.format(formatter);
            }

            textViewOrderTime.setText(orderTimeAsString);
            textViewDeliveryTime.setText(deliveryTimeAsString);
            Order.Status orderStatus = order.getStatus();
            if (orderStatus.equals(Order.Status.ORDERED)) {
                textViewOrderStatus.setText(R.string.ordered);
            }
            else if (orderStatus.equals(Order.Status.IN_PROGRESS)) {
                textViewOrderStatus.setText(R.string.in_preparation);
            }
            else if (orderStatus.equals(Order.Status.DELIVERING)) {
                textViewOrderStatus.setText(R.string.delivering);
            }
            else if (orderStatus.equals(Order.Status.DELIVERED)) {
                textViewOrderStatus.setText(R.string.delivered);
            }

            textViewSubTotal.setText(Float.toString(order.getTotalCost()));
            float deliveryCost = order.getRestaurateur().getDeliveryCost();
            textViewDeliveryCost.setText(Float.toString(deliveryCost));
            textViewTotalPrice.setText(Float.toString(order.getTotalCost()+deliveryCost));

            recyclerView.setAdapter(new ProductRecyclerViewAdapter(order.getProducts()));
        }

        return rootView;
    }

}
