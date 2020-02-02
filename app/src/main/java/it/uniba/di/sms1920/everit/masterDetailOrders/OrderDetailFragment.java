package it.uniba.di.sms1920.everit.masterDetailOrders;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.uniba.di.sms1920.everit.R;
import it.uniba.di.sms1920.everit.masterDetailOrders.productList.ProductContent;
import it.uniba.di.sms1920.everit.models.Order;
import it.uniba.di.sms1920.everit.models.Product;


/**
 * A fragment representing a single Order detail screen.
 * This fragment is either contained in a {@link OrderListActivity}
 * in two-pane mode (on tablets) or a {@link OrderDetailActivity}
 * on handsets.
 */
public class OrderDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Order mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            long id = Long.parseLong(getArguments().getString(ARG_ITEM_ID));
            mItem = OrderListActivity.orderMap.get(id);

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
            textViewTotalPrice.setText(String.format("€ %.2f", getTotalPrice(mItem)));
            DateFormat dateFormat = new SimpleDateFormat(" dd/MM/yyyy hh:mm", Locale.getDefault());
            //String strDate = dateFormat.format(mItem.getActualDeliveryTime());
            textViewOrderTime.setText("Boh");
            String strDate = dateFormat.format(mItem.getActualDeliveryTime());
            textViewDeliveryTime.setText(strDate);
            textViewOrderNotes.setText(mItem.getOrderNotes());
            textViewDeliveryNotes.setText(mItem.getDeliveryNotes());

            recyclerView.setAdapter(new OrderDetailFragment.ProductItemRecyclerViewAdapter(mItem.getProducts()));

        }

        return rootView;
    }

    //Adapter for Product list

    public static class ProductItemRecyclerViewAdapter
            extends RecyclerView.Adapter<OrderDetailFragment.ProductItemRecyclerViewAdapter.ViewHolder> {

        private final Map<Product, Integer> mValues;

        ProductItemRecyclerViewAdapter(Map<Product, Integer> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_list_content, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            //TODO capire come prendere la chiave della mappa
            //holder.mProductNameView.setText(mValues.get(position).getKey());
            //holder.mPriceView.setText(String.format("€ %.2f", mValues.get(position).getPrice()));

            holder.itemView.setTag(mValues.get(position));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
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
    }

    private double getTotalPrice(Order order){
        double totalPrice = 0;
        for(Map.Entry<Product, Integer> i : mItem.getProducts().entrySet()){
            totalPrice += i.getKey().getPrice() + i.getValue();
        }

        return totalPrice;
    }
}
