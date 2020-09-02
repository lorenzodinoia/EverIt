package it.uniba.di.sms1920.everit.customer.activities.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import it.uniba.di.sms1920.everit.customer.ProductRecyclerViewAdapter;
import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.nfc.OrderReceiverActivity;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;

public class OrderDetailFragment extends Fragment {
    public static final String ARG_ITEM = "item";
    private static final String SAVED_ORDER = "saved.order";
    /*
    public static final String ORDER = "order";
    static final String ARG_ITEM_ID = "item_id";
        */
    private Order order;

    private TextView textViewOrderNumber;
    private TextView textViewOrderType;
    private TextView labelDeliveryAddress;
    private TextView textViewDeliveryAddress;
    private TextView textViewOrderTime;
    private TextView labelDeliveryDate;
    private TextView textViewDeliveryTime;
    private TextView textViewOrderStatus;
    private TextView textViewOrderLate;
    private TextView textViewSubTotal;
    private TextView textViewDeliveryCost;
    private TextView textViewTotalPrice;
    private Button buttonReceiveOrder;
    private RecyclerView recyclerView;

    public OrderDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            if ((arguments != null) && (arguments.containsKey(ARG_ITEM))) {
                this.order = arguments.getParcelable(ARG_ITEM);
            }
        }
        else if (savedInstanceState.containsKey(SAVED_ORDER)) {
            this.order = savedInstanceState.getParcelable(SAVED_ORDER);
        }
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_detail, container, false);
        this.initUi(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.order != null) {
            this.initData();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_ORDER, this.order);
    }

    private void initUi(View view) {
        this.textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
        this.textViewOrderType = view.findViewById(R.id.textViewOrderType);
        this.labelDeliveryAddress = view.findViewById(R.id.labelDeliveryAddress);
        this.textViewDeliveryAddress = view.findViewById(R.id.textViewDeliveryAddress);
        this.textViewOrderTime = view.findViewById(R.id.textViewOrderDateTime);
        this.labelDeliveryDate = view.findViewById(R.id.labelDeliveryDate);
        this.textViewDeliveryTime = view.findViewById(R.id.textViewDeliveryDateTime);
        this.textViewOrderStatus = view.findViewById(R.id.textViewOrderStatus);
        this.textViewOrderLate = view.findViewById(R.id.textViewOrderLate);
        this.textViewSubTotal = view.findViewById(R.id.textViewSubTotal);
        this.textViewDeliveryCost = view.findViewById(R.id.textViewDeliveryCost);
        this.textViewTotalPrice = view.findViewById(R.id.textViewTotalPrice);
        this.buttonReceiveOrder = view.findViewById(R.id.buttonReceiveOrder);
        this.buttonReceiveOrder.setVisibility(View.GONE);
        this.recyclerView = view.findViewById(R.id.recycleViewProducts);
    }

    private void initData() {
        textViewOrderNumber.setText("#"+order.getId());
        if(order.getOrderType().equals(Order.OrderType.HOME_DELIVERY)) {
            textViewOrderType.setText(R.string.home_delivery);
            labelDeliveryAddress.setText(R.string.delivery_address);
            textViewDeliveryAddress.setText(order.getDeliveryAddress().getFullAddress());
            labelDeliveryDate.setText(R.string.delivery_date_label);
        }
        else {
            textViewOrderType.setText(R.string.takeaway);
            labelDeliveryAddress.setText(R.string.pickup_address);
            textViewDeliveryAddress.setText(order.getRestaurateur().getAddress().getFullAddress());
            labelDeliveryDate.setText(R.string.pickup_date);
        }

        if(order.isLate()){
            textViewOrderLate.setVisibility(View.VISIBLE);
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
        else if (orderStatus.equals(Order.Status.READY)){
            textViewOrderStatus.setText(R.string.ready);
        }
        else if (orderStatus.equals(Order.Status.DELIVERED)) {
            textViewOrderStatus.setText(R.string.delivered);
        }

        textViewSubTotal.setText(Float.toString(order.getTotalCost()));
        float deliveryCost = order.getRestaurateur().getDeliveryCost();
        textViewDeliveryCost.setText(Float.toString(deliveryCost));
        textViewTotalPrice.setText(Float.toString(order.getTotalCost()+deliveryCost));

        if(order.getOrderType().equals(Order.OrderType.TAKEAWAY)) {
            if(orderStatus.equals(Order.Status.READY)) {
                buttonReceiveOrder.setVisibility(View.VISIBLE);
                buttonReceiveOrder.setText(R.string.send_code);
                buttonReceiveOrder.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), OrderReceiverActivity.class);
                    intent.putExtra(OrderReceiverActivity.ARG_ITEM, order); //TODO Sistema parametro
                    startActivity(intent);
                });
            }
        }

        recyclerView.setAdapter(new ProductRecyclerViewAdapter(order.getProducts()));
    }
}
