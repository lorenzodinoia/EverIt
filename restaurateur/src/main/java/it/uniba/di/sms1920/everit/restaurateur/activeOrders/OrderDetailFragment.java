package it.uniba.di.sms1920.everit.restaurateur.activeOrders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.DeliverOrderActivity;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.ProposalRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class OrderDetailFragment extends Fragment {
    public static final String ARG_ITEM = "item";
    private static final String SAVED_ORDER = "saved.order";

    private OrderDetailActivity parentActivity;
    private Order order;

    private TextView labelOrderType;
    private TextView textViewOrderType;
    private TextView labelDeliveryDate;
    private TextView textViewLabelOrderNumber;
    private TextView textViewOrderNumber;
    private TextView textViewDeliveryTime;
    private RecyclerView recyclerView;
    private TextView textViewOrderNotes;
    private TextView textViewOrderDeliveryPrice;
    private TextView textViewSubTotalOrderPrice;
    private TextView textViewOrderTotalPrice;

    private MaterialButton btnConfirmButton;
    private MaterialButton btnLate;

    private String timePicker;

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
        labelOrderType = view.findViewById(R.id.textViewLabelOrderType);
        textViewOrderType = view.findViewById(R.id.textViewOrderType);
        labelDeliveryDate = view.findViewById(R.id.labelDeliveryDate);
        textViewLabelOrderNumber = view.findViewById(R.id.textViewLabelOrderNumber);
        textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
        textViewDeliveryTime = view.findViewById(R.id.textViewDeliveryDateTime);
        recyclerView = view.findViewById(R.id.recycleViewProducts);
        textViewOrderNotes = view.findViewById(R.id.textViewOrderNotes);
        textViewOrderDeliveryPrice = view.findViewById(R.id.textViewDeliveryCost);
        textViewSubTotalOrderPrice = view.findViewById(R.id.textViewSubTotal);
        textViewOrderTotalPrice = view.findViewById(R.id.textViewTotalPrice);
        btnConfirmButton = view.findViewById(R.id.btnConfirmOrder);
        btnLate = view.findViewById(R.id.btnLate);
    }

    private void initData() {
        this.setUpButtons();

        //TODO rimuovere card order notes se non è presente

        labelOrderType.setText(R.string.order_type);
        if (order.getOrderType().equals(Order.OrderType.HOME_DELIVERY)){
            textViewOrderType.setText(R.string.home_delivery);
        }
        else {
            textViewOrderType.setText(R.string.take_away);
        }

        textViewLabelOrderNumber.setText(getString(R.string.order_number) + ":");
        labelDeliveryDate.setText(getString(R.string.delivery_date_label) + ":");
        textViewOrderNumber.setText("#"+order.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
        LocalDateTime estimatedDeliveryTime = order.getEstimatedDeliveryTime();
        String dateAsString = estimatedDeliveryTime.format(formatter);
        textViewDeliveryTime.setText(dateAsString);

        textViewSubTotalOrderPrice.setText(Float.toString(order.getTotalCost()));
        Restaurateur restaurateur = (Restaurateur) Providers.getAuthProvider().getUser();
        textViewOrderNotes.setText(order.getOrderNotes());
        float deliveryCost = restaurateur.getDeliveryCost();
        textViewOrderDeliveryPrice.setText(Float.toString(deliveryCost));
        textViewOrderTotalPrice.setText(Float.toString(order.getTotalCost() + deliveryCost));
        setupRecyclerView(recyclerView);
    }

    private void setUpButtons(){
        switch(order.getStatus()){
            case ORDERED:
                setUpButtonConfirmOrder();
                setUpButtonRefuseOrder();
                break;

            case CONFIRMED:
                btnLate.setVisibility(View.GONE);
                setUpButtonSearchRider();
                break;

            case IN_PROGRESS:
                setUpButtonLate();
                setUpButtonInProgress();
                break;

            default:
                setUpButtonLate();
                setUpButtonDeliverOrder();
                break;
        }
    }

    private void setUpButtonConfirmOrder(){
        btnConfirmButton.setVisibility(View.VISIBLE);
        btnConfirmButton.setText(R.string.confirm_order_button);
        btnConfirmButton.setOnClickListener(v -> {
            OrderRequest orderRequest = new OrderRequest();
            if(order.getOrderType().equals(Order.OrderType.HOME_DELIVERY)) {
                orderRequest.markAsConfirmed(order.getId(), new RequestListener<Order>() {
                    @Override
                    public void successResponse(Order response) {
                        parentActivity.finish();
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        promptErrorMessage(error.getMessage());
                    }
                });
            }
            else {
                orderRequest.markAsInProgress(order.getId(), new RequestListener<Order>() {
                    @Override
                    public void successResponse(Order response) {
                        parentActivity.finish();
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        promptErrorMessage(error.getMessage());
                    }
                });
            }
        });
    }

    private void setUpButtonRefuseOrder(){
        btnLate.setVisibility(View.VISIBLE);
        btnLate.setText(R.string.refuse_order_button);
        btnLate.setOnClickListener(v -> {
            Dialog dialog = new Dialog(parentActivity);
            dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_y_n);

            TextView title = dialog.findViewById(R.id.textViewTitle);
            title.setText(R.string.refuse_order_button);

            TextView message = dialog.findViewById(R.id.textViewMessage);
            message.setText(R.string.message_refuse_order);

            MaterialButton btnOk = dialog.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(v1 -> {

                OrderRequest orderRequest = new OrderRequest();
                orderRequest.markAsRefused(order.getId(), new RequestListener<Order>() {
                    @Override
                    public void successResponse(Order response) {
                        parentActivity.finish();
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        promptErrorMessage(error.getMessage());
                    }
                });
            });

            MaterialButton btnCancel = dialog.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(v1 -> dialog.dismiss());

            dialog.show();
        });
    }

    private void setUpButtonSearchRider(){
        btnConfirmButton.setText(R.string.search_rider);
        ProposalRequest proposalRequest = new ProposalRequest();
        proposalRequest.checkProposalsState(order.getId(), new RequestListener<Boolean>() {
            @Override
            public void successResponse(Boolean response) {
                if(response){
                    disableSearchRiderButton();
                }
                else{
                    btnConfirmButton.setOnClickListener(v -> {
                        OrderRequest orderRequest = new OrderRequest();
                        Dialog dialog = new Dialog(parentActivity);
                        dialog.setContentView(R.layout.dialog_set_pickup_time);
                        TextView textViewLabel = dialog.findViewById(R.id.textViewLabelPickupTimeSelection);
                        textViewLabel.setText(R.string.message_pickup_time_dialog);

                        NumberPicker numberPicker = dialog.findViewById(R.id.numberPicker);
                        numberPicker.setMinValue(10);
                        numberPicker.setMaxValue(60);

                        MaterialButton buttonDismiss = dialog.findViewById(R.id.buttonDismiss);
                        buttonDismiss.setOnClickListener(v1 -> dialog.dismiss());
                        MaterialButton buttonConfirm = dialog.findViewById(R.id.buttonConfirmPickupTime);
                        buttonConfirm.setOnClickListener(v1 -> {
                            LocalTime actualLocalTime = LocalTime.now();
                            LocalTime pickupLocalTime = actualLocalTime.plusMinutes(numberPicker.getValue());
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
                            timePicker = pickupLocalTime.format(formatter);
                            orderRequest.searchRider(order.getId(), timePicker, new RequestListener<String>() {
                                @Override
                                public void successResponse(String response) {
                                    dialog.dismiss();
                                    disableSearchRiderButton();
                                }

                                @Override
                                public void errorResponse(RequestException error) {
                                    dialog.dismiss();
                                    promptErrorMessage(error.getMessage());
                                }
                            });
                        });
                        dialog.show();
                    });
                }
            }

            @Override
            public void errorResponse(RequestException error) {
                promptErrorMessage(error.getMessage());
            }
        });
    }

    private void disableSearchRiderButton() {
        btnConfirmButton.setFocusable(false);
        btnConfirmButton.setClickable(false);
        btnConfirmButton.setBackgroundColor(ContextCompat.getColor(parentActivity, R.color.lightGreyAccent));
    }

    private void setUpButtonInProgress(){
        if (order.getOrderType().equals(Order.OrderType.HOME_DELIVERY)) {
            btnConfirmButton.setVisibility(View.GONE);
        }
        else {
            btnConfirmButton.setText(R.string.order_ready);
            btnConfirmButton.setOnClickListener(v -> {
                OrderRequest orderRequest = new OrderRequest();
                orderRequest.markAsReady(order.getId(), new RequestListener<Order>() {
                    @Override
                    public void successResponse(Order response) {
                        btnConfirmButton.setVisibility(View.GONE);
                        setUpButtonDeliverOrder();
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        promptErrorMessage(error.getMessage());
                    }
                });
            });
        }
    }

    private void setUpButtonLate(){
        btnLate.setVisibility(View.VISIBLE);
        btnLate.setText(R.string.late_button);
        if(order.isLate()){
            btnLate.setFocusable(false);
            btnLate.setClickable(false);
            btnLate.setBackgroundColor(ContextCompat.getColor(parentActivity, R.color.lightGreyAccent));
        }
        else{
            btnLate.setFocusable(true);
            btnLate.setClickable(true);
            btnLate.setOnClickListener(v -> {
                OrderRequest orderRequest = new OrderRequest();
                orderRequest.markAsLate(order.getId(), new RequestListener<Order>() {
                    @Override
                    public void successResponse(Order response) {
                        order.setLate(true);
                        btnLate.setFocusable(false);
                        btnLate.setClickable(false);
                        btnLate.setBackgroundColor(ContextCompat.getColor(parentActivity, R.color.lightGreyAccent));
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        promptErrorMessage(error.getMessage());
                    }
                });
            });
        }

    }

    private void setUpButtonDeliverOrder() {
        btnConfirmButton.setVisibility(View.VISIBLE);
        btnConfirmButton.setText(R.string.deliver_order);
        btnConfirmButton.setOnClickListener(v -> {
            Intent intent = new Intent(parentActivity, DeliverOrderActivity.class);
            intent.putExtra(DeliverOrderActivity.ARG_ITEM, order);
            startActivityForResult(intent, DeliverOrderActivity.REQUEST_CODE);

        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<Product> products = new ArrayList<>(order.getProducts().keySet());
        List<Integer> quantity = new ArrayList<>(order.getProducts().values());
        ProductsRecyclerViewAdapter adapter = new OrderDetailFragment.ProductsRecyclerViewAdapter(this, products, quantity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OrderDetailActivity){
            parentActivity = (OrderDetailActivity) context;
        }
    }

    public static class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<OrderDetailFragment.ProductsRecyclerViewAdapter.ViewHolder> {
        private final OrderDetailFragment parentActivity;
        private final List<Product> products;
        private final List<Integer> quantity;

        private final View.OnClickListener itemOnClickListener = view -> {
            return;
        };

        ProductsRecyclerViewAdapter(OrderDetailFragment parent, List<Product> products, List<Integer> quantity) {
            this.products = products;
            this.quantity = quantity;
            this.parentActivity = parent;
        }

        @Override
        public OrderDetailFragment.ProductsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_detail, parent, false);
            return new OrderDetailFragment.ProductsRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final OrderDetailFragment.ProductsRecyclerViewAdapter.ViewHolder holder, int position) {
            Product item = this.products.get(position);
            int productQuantity = this.quantity.get(position);
            if (item != null) {
                holder.textViewProductName.setText(item.getName());
                if(productQuantity > 1) {
                    holder.textViewQuantity.setText(String.format("(x %d)", productQuantity));
                }else{
                    holder.textViewQuantity.setText("");
                }

                float totalPrice = item.getPrice() * productQuantity;
                holder.textViewPrice.setText(String.valueOf(totalPrice));

                holder.itemView.setTag(item);
                holder.itemView.setOnClickListener(itemOnClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewProductName;
            final TextView textViewQuantity;
            final TextView textViewPrice;

            ViewHolder(View view) {
                super(view);
                textViewProductName = view.findViewById(R.id.textViewProductName);
                textViewQuantity = view.findViewById(R.id.textViewQuantity);
                textViewPrice = view.findViewById(R.id.textViewPrice);
            }
        }
    }

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(parentActivity);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == DeliverOrderActivity.REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                parentActivity.finish();
            }
        }
    }
}