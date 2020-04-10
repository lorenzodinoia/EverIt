package it.uniba.di.sms1920.everit.customer.activities.orders;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Product;

public class OrderDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private Order mItem;
    public OrderDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            long id = getArguments().getLong(ARG_ITEM_ID);
            mItem = OrderListActivity.getOrderById(id);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            /*if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getActivityName());
            }*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_detail, container, false);

        if (mItem != null) {
            //TODO Riabilitare date quando sistemate nell'adaptrer
            //EditText editTextActivityName = (EditText) rootView.findViewById(R.id.editTextActivityName);
            TextView textViewDeliveryAddress = (TextView) rootView.findViewById(R.id.textViewDeliveryAddress);
            TextView textViewTotalPrice = (TextView) rootView.findViewById(R.id.textViewPrice);
            TextView textViewOrderTime = (TextView) rootView.findViewById(R.id.textViewOrderDateTime);
            TextView textViewDeliveryTime = (TextView) rootView.findViewById(R.id.textViewDeliveryDateTime);
            TextView textViewOrderNotes = (TextView) rootView.findViewById(R.id.textViewOrderNotesBox);
            TextView textViewDeliveryNotes = (TextView) rootView.findViewById(R.id.textViewDeliveryNotesBox);
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleViewProducts);

            //((TextView) rootView.findViewById(R.id.constraintLayout)).setText(String.format("%s: $d",R.string.order_number, mItem.getId()));
            textViewDeliveryAddress.setText(mItem.getDeliveryAddress());
            textViewTotalPrice.setText(String.format(Locale.getDefault(), "€ %.2f", mItem.getTotalCost()));
            //DateFormat dateFormat = new SimpleDateFormat(" dd/MM/yyyy hh:mm", Locale.getDefault());
            //String strDate = dateFormat.format(mItem.getActualDeliveryTime());
            textViewOrderTime.setText("Boh");
            //String strDate = dateFormat.format(mItem.getActualDeliveryTime());
            //textViewDeliveryTime.setText(strDate);
            textViewOrderNotes.setText(mItem.getOrderNotes());
            textViewDeliveryNotes.setText(mItem.getDeliveryNotes());

            recyclerView.setAdapter(new ProductRecyclerViewAdapter(mItem.getProducts()));

        }

        return rootView;
    }

    //Adapter for Product list

    public static class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

        private final List<ProductItem> items = new ArrayList<>();

        ProductRecyclerViewAdapter(Map<Product, Integer> items) {
            this.items.clear();
            for (Map.Entry<Product, Integer> entry : items.entrySet()) {
                final ProductItem productItem = new ProductItem(entry.getKey(), entry.getValue());
                this.items.add(productItem);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_list_content, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mProductNameView.setText(items.get(position).getProduct().getName());
            holder.mPriceView.setText(String.format(Locale.getDefault(),"€ %.2f", items.get(position).getProduct().getPrice()));

            holder.itemView.setTag(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mProductNameView;
            final TextView mPriceView;

            ViewHolder(View view) {
                super(view);
                mProductNameView = (TextView) view.findViewById(R.id.textViewProductName);
                mPriceView = (TextView) view.findViewById(R.id.textViewPrice);
            }
        }

        private class ProductItem {
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
