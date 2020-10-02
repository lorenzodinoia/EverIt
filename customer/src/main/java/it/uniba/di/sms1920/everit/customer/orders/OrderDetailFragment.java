package it.uniba.di.sms1920.everit.customer.orders;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

import it.uniba.di.sms1920.everit.customer.ProductRecyclerViewAdapter;
import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;

public class OrderDetailFragment extends Fragment {
    public static final String ARG_ITEM = "item";
    private static final String SAVED_ORDER = "saved.order";

    private OrderDetailActivity orderDetailActivity;

    private Order order;

    private TextView textViewRestaurateurName;
    private TextView textViewOrderNumber;
    private TextView textViewOrderType;
    private TextView labelDeliveryAddress;
    private TextView textViewDeliveryAddress;
    private TextView textViewOrderTime;
    private TextView labelDeliveryDate;
    private TextView textViewDeliveryTime;
    private TextView textViewOrderStatus;
    private TextView textViewOrderLate;
    private TextView textViewValidationCode;
    private TextView textViewSubTotal;
    private TextView textViewDeliveryCost;
    private TextView textViewTotalPrice;
    private Chip chipLate;
    private Button buttonReceiveOrder;
    private RecyclerView recyclerView;
    private MaterialCardView cardViewValidationCode;
    private TextView labelValidationCodeHint;

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OrderDetailActivity) {
            this.orderDetailActivity = (OrderDetailActivity) context;
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
        this.textViewRestaurateurName = view.findViewById(R.id.textViewRestaurateurName);
        this.textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
        this.textViewOrderType = view.findViewById(R.id.textViewOrderType);
        this.labelDeliveryAddress = view.findViewById(R.id.labelDeliveryAddress);
        this.textViewDeliveryAddress = view.findViewById(R.id.textViewDeliveryAddress);
        this.textViewOrderTime = view.findViewById(R.id.textViewOrderDateTime);
        this.labelDeliveryDate = view.findViewById(R.id.labelDeliveryDate);
        this.textViewDeliveryTime = view.findViewById(R.id.textViewDeliveryDateTime);
        this.textViewOrderStatus = view.findViewById(R.id.textViewOrderStatus);
        this.textViewOrderLate = view.findViewById(R.id.textViewOrderLate);
        this.chipLate = view.findViewById(R.id.chipLate);
        this.textViewSubTotal = view.findViewById(R.id.textViewSubTotal);
        this.textViewDeliveryCost = view.findViewById(R.id.textViewDeliveryCost);
        this.textViewTotalPrice = view.findViewById(R.id.textViewTotalPrice);
        this.buttonReceiveOrder = view.findViewById(R.id.buttonReceiveOrder);
        this.buttonReceiveOrder.setVisibility(View.GONE);
        this.recyclerView = view.findViewById(R.id.recycleViewProducts);
        this.textViewValidationCode = view.findViewById(R.id.textViewValidationCode);
        this.cardViewValidationCode = view.findViewById(R.id.cardViewValidationCode);
        this.labelValidationCodeHint = view.findViewById(R.id.labelValidationCodeHint);
    }

    private void initData() {
        textViewOrderNumber.setText(String.format(Locale.getDefault(), "%s%d", getString(R.string.order_number_label_short), order.getId()));
        textViewRestaurateurName.setText(order.getRestaurateur().getShopName());

        if(order.getOrderType().equals(Order.OrderType.HOME_DELIVERY)) {
            textViewOrderType.setText(R.string.home_delivery);
            labelDeliveryAddress.setText(R.string.delivery_address_double_dots);
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
            chipLate.setVisibility(View.VISIBLE);
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
        else if (orderStatus.equals(Order.Status.REFUSED)){
            textViewOrderStatus.setText(R.string.refused);
        }

        textViewSubTotal.setText(Float.toString(order.getTotalCost()));
        float deliveryCost = order.getRestaurateur().getDeliveryCost();
        textViewDeliveryCost.setText(Float.toString(deliveryCost));
        textViewTotalPrice.setText(Float.toString(order.getTotalCost()+deliveryCost));

        if((orderStatus.equals(Order.Status.READY)) || (orderStatus.equals(Order.Status.DELIVERING))) {
            /*
            buttonReceiveOrder.setVisibility(View.VISIBLE);
            buttonReceiveOrder.setText(R.string.send_code);
            buttonReceiveOrder.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), OrderReceiverActivity.class);
                intent.putExtra(OrderReceiverActivity.ARG_ITEM, order);
                startActivity(intent);
            });*/
            cardViewValidationCode.setVisibility(View.VISIBLE);
            textViewValidationCode.setText(order.getValidationCode());
            if (orderDetailActivity != null) {
                if (orderDetailActivity.isNfcEnabled()) {
                    labelValidationCodeHint.setText(R.string.order_validation_code_hint_nfc);
                }
            }
        }

        recyclerView.setAdapter(new ProductRecyclerViewAdapter(order.getProducts()));
    }
}
