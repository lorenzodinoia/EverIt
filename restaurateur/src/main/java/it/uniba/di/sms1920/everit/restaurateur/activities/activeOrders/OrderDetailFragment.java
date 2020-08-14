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
import android.widget.TextView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class OrderDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String INDEX = "fragment_index";

    private OrderDetailActivity mParent;
    private Order order;
    private int index;

    private MaterialButton confirmButton;

    public OrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            index = getArguments().getInt(INDEX);
            long id = getArguments().getLong(ARG_ITEM_ID);
            order = OrderListFragment.getOrderById(id);
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
            if(index != 0){
                confirmButton.setText(R.string.late_button);
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

            if((index == 1) && (order.isLate())){
                disableConfirmButton();
            }

            confirmButton.setOnClickListener(v -> {
                OrderRequest orderRequest = new OrderRequest();
                if(index == 0){
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
                else if(index == 1){
                    orderRequest.markAsLate(order.getId(), new RequestListener<Order>() {
                        @Override
                        public void successResponse(Order response) {
                            disableConfirmButton();
                        }

                        @Override
                        public void errorResponse(RequestException error) {
                            promptErrorMessage(error.getMessage());
                        }
                    });
                }
            });

        }

        return rootView;
    }

    private void disableConfirmButton(){
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

        private final View.OnClickListener itemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
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

            ViewHolder(View view) {
                super(view);
                textViewProductName = view.findViewById(R.id.textViewProductName);
                textViewQuantity = view.findViewById(R.id.textViewQuantity);
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