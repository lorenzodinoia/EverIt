package it.uniba.di.sms1920.everit.restaurateur.activities.activeOrders;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
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

    public static final String ARG_ITEM_ID = "item_id";
    public static final String INDEX = "fragment_index";

    private OrderDetailActivity mParent;
    private Order order;
    private int index;

    private MaterialButton confirmButton;
    private MaterialButton searchRider;

    private String timePicker;
    public OrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(INDEX)) {
            index = getArguments().getInt(INDEX);
            order = mParent.getOrder();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_detail, container, false);

        if (order != null) {
            TextView labelDeliveryDate = rootView.findViewById(R.id.labelDeliveryDate);
            TextView textViewLabelOrderNumber = rootView.findViewById(R.id.textViewLabelOrderNumber);
            TextView textViewOrderNumber = rootView.findViewById(R.id.textViewOrderNumber);
            TextView textViewDeliveryTime = rootView.findViewById(R.id.textViewDeliveryDateTime);
            RecyclerView recyclerView = rootView.findViewById(R.id.recycleViewProducts);
            TextView textViewOrderNotes = rootView.findViewById(R.id.textViewOrderNotes);
            TextView textViewOrderDeliveryPrice = rootView.findViewById(R.id.textViewDeliveryCost);
            TextView textViewSubTotalOrderPrice = rootView.findViewById(R.id.textViewSubTotal);
            TextView textViewOrderTotalPrice = rootView.findViewById(R.id.textViewTotalPrice);

            confirmButton = rootView.findViewById(R.id.btnConfirmOrder);
            searchRider = rootView.findViewById(R.id.btnSearchRider);
            bindButton();

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

        return rootView;
    }

    private void bindButton(){

        if(index == 0){
            confirmButton.setVisibility(View.VISIBLE);
            confirmButton.setOnClickListener(v -> {
                OrderRequest orderRequest = new OrderRequest();
                if(order.getOrderType().equals(Order.OrderType.HOME_DELIVERY)) {
                    orderRequest.markAsConfirmed(order.getId(), new RequestListener<Order>() {
                        @Override
                        public void successResponse(Order response) {
                            mParent.finish();
                        }

                        @Override
                        public void errorResponse(RequestException error) {
                            promptErrorMessage(error.getMessage());
                        }
                    });
                }
                else{
                    orderRequest.markAsInProgress(order.getId(), new RequestListener<Order>() {
                        @Override
                        public void successResponse(Order response) {
                            mParent.finish();
                        }

                        @Override
                        public void errorResponse(RequestException error) {
                            promptErrorMessage(error.getMessage());
                        }
                    });
                }
            });

            searchRider.setVisibility(View.GONE);
        }
        else{
            if(order.isLate()){
                disableConfirmButton();
            }
            else{
                confirmButton.setFocusable(true);
                confirmButton.setClickable(true);
                confirmButton.setVisibility(View.VISIBLE);
                confirmButton.setText(R.string.late_button);
                confirmButton.setBackgroundColor(ContextCompat.getColor(mParent, R.color.colorWarning));
                confirmButton.setOnClickListener(v -> {
                    OrderRequest orderRequest = new OrderRequest();
                    orderRequest.markAsLate(order.getId(), new RequestListener<Order>() {
                        @Override
                        public void successResponse(Order response) {
                            order.setLate(true);
                            disableConfirmButton();
                        }

                        @Override
                        public void errorResponse(RequestException error) {
                            promptErrorMessage(error.getMessage());
                        }
                    });
                });
            }

            if(order.getStatus().equals(Order.Status.CONFIRMED)){
                searchRider.setText(R.string.search_rider);
                ProposalRequest proposalRequest = new ProposalRequest();
                proposalRequest.checkProposalsState(order.getId(), new RequestListener<Boolean>() {
                    @Override
                    public void successResponse(Boolean response) {
                        if(response){
                            disableSearchRiderButton();
                        }
                        else{
                            searchRider.setOnClickListener(v -> {
                                OrderRequest orderRequest = new OrderRequest();
                                //TODO prendere in qualche modo il pickup time
                                Dialog dialog = new Dialog(mParent);
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
            else if(order.getStatus().equals(Order.Status.IN_PROGRESS)){
                if(order.getOrderType().equals(Order.OrderType.HOME_DELIVERY)){
                    searchRider.setVisibility(View.GONE);
                }
                else{
                    searchRider.setText(R.string.order_ready);
                    searchRider.setOnClickListener(v -> {
                        OrderRequest orderRequest = new OrderRequest();
                        orderRequest.markAsReady(order.getId(), new RequestListener<Order>() {
                            @Override
                            public void successResponse(Order response) {
                                confirmButton.setVisibility(View.GONE);
                                setButtonDeliverOrder();
                            }

                            @Override
                            public void errorResponse(RequestException error) {
                                promptErrorMessage(error.getMessage());
                            }
                        });
                    });
                }
            }
            else{
                confirmButton.setVisibility(View.GONE);
                setButtonDeliverOrder();
            }
        }
    }

    private void disableSearchRiderButton(){
        searchRider.setFocusable(false);
        searchRider.setClickable(false);
        searchRider.setBackgroundColor(ContextCompat.getColor(mParent, R.color.lightGreyAccent));
    }

    private void setButtonDeliverOrder(){
        searchRider.setText(R.string.deliver_order);
        searchRider.setOnClickListener(v -> {
            //TODO aggiungere metodo per consegna ordine takeaway
        });
    }

    private void disableConfirmButton(){
        confirmButton.setText(R.string.late_button);
        confirmButton.setFocusable(false);
        confirmButton.setClickable(false);
        confirmButton.setBackgroundColor(ContextCompat.getColor(mParent, R.color.lightGreyAccent));
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
            mParent = (OrderDetailActivity) context;
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
        Dialog dialog = new Dialog(mParent);
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
}