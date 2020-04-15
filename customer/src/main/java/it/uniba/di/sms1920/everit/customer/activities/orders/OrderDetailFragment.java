package it.uniba.di.sms1920.everit.customer.activities.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
            TextView textViewDeliveryAddress = (TextView) rootView.findViewById(R.id.textViewDeliveryAddress);
            TextView textViewTotalPrice = (TextView) rootView.findViewById(R.id.textViewPrice);
            TextView textViewOrderTime = (TextView) rootView.findViewById(R.id.textViewOrderDateTime);
            TextView textViewDeliveryTime = (TextView) rootView.findViewById(R.id.textViewDeliveryDateTime);
            TextView textViewOrderNotes = (TextView) rootView.findViewById(R.id.textViewOrderNotesBox);
            TextView textViewDeliveryNotes = (TextView) rootView.findViewById(R.id.textViewDeliveryNotesBox);
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleViewProducts);

            DateFormat dateFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT, Locale.getDefault());
            String orderTimeAsString = dateFormat.format(order.getCreatedAt());
            String deliveryTimeAsString = "";
            if (order.isDelivered()) {
                if (order.getActualDeliveryTime() != null) {
                    deliveryTimeAsString = dateFormat.format(order.getActualDeliveryTime());
                }
            }
            else {
                //TODO Aggiungere un simbolo per avvertire che l'ordine è ancora da consegnare
                deliveryTimeAsString = dateFormat.format(order.getEstimatedDeliveryTime());
            }

            textViewDeliveryAddress.setText(order.getDeliveryAddress());
            textViewOrderTime.setText(orderTimeAsString);
            textViewDeliveryTime.setText(deliveryTimeAsString);
            textViewOrderNotes.setText(order.getOrderNotes());
            textViewDeliveryNotes.setText(order.getDeliveryNotes());
            textViewTotalPrice.setText(String.format(Locale.getDefault(), "€ %.2f", order.getTotalCost()));

            recyclerView.setAdapter(new ProductRecyclerViewAdapter(order.getProducts()));
        }

        return rootView;
    }

    /*
    RecyclerView class for handling products list
     */
    public static class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {
        private final List<ProductItem> productItems = new ArrayList<>();

        ProductRecyclerViewAdapter(Map<Product, Integer> productItems) {
            this.productItems.clear();
            for (Map.Entry<Product, Integer> entry : productItems.entrySet()) {
                final ProductItem productItem = new ProductItem(entry.getKey(), entry.getValue());
                this.productItems.add(productItem);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_content, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            ProductItem productItem = productItems.get(position);
            String productText = (productItem.getQuantity() > 1) ? String.format(Locale.getDefault(),"%s (x %d)", productItem.getProduct().getName(), productItem.getQuantity())
                    : productItem.getProduct().getName();
            holder.textViewProductName.setText(productText);
            holder.textViewPrice.setText(String.format(Locale.getDefault(),"€ %.2f", productItem.getProduct().getPrice()));

            holder.itemView.setTag(productItem);
        }

        @Override
        public int getItemCount() {
            return productItems.size();
        }

        private static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewProductName;
            final TextView textViewPrice;

            ViewHolder(View view) {
                super(view);
                textViewProductName = (TextView) view.findViewById(R.id.textViewProductName);
                textViewPrice = (TextView) view.findViewById(R.id.textViewPrice);
            }
        }

        private static class ProductItem {
            private Product product;
            private int quantity;

            private ProductItem(Product product, int quantity) {
                this.product = product;
                this.quantity = quantity;
            }

            private Product getProduct() {
                return product;
            }

            private int getQuantity() {
                return quantity;
            }
        }
    }
}
