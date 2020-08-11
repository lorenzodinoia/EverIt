package it.uniba.di.sms1920.everit.restaurateur.activities.orderHistory;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.Providers;

public class DoneOrderDetailFragment extends Fragment {
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
    public DoneOrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            long id = getArguments().getLong(ARG_ITEM_ID);
            mItem = DoneOrderListActivity.getOrderById(id);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            /*if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.doneorder_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            TextView textViewOrderNumber = rootView.findViewById(R.id.textViewOrderNumber);
            TextView textViewDeliveryTime = rootView.findViewById(R.id.textViewDeliveryDateTime);
            RecyclerView recyclerView = rootView.findViewById(R.id.recycleViewProducts);
            TextView textViewOrderNotes = rootView.findViewById(R.id.textViewOrderNotes);
            TextView textViewOrderDeliveryPrice = rootView.findViewById(R.id.textViewSubTotal);
            TextView textViewSubTotalOrderPrice = rootView.findViewById(R.id.textViewDeliveryCost);
            TextView textViewOrderTotalPrice = rootView.findViewById(R.id.textViewTotalPrice);

            textViewOrderNumber.setText("#"+mItem.getId());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
            LocalDateTime estimatedDeliveryTime = mItem.getEstimatedDeliveryTime();
            String dateAsString = estimatedDeliveryTime.format(formatter);
            textViewDeliveryTime.setText(dateAsString);

            textViewSubTotalOrderPrice.setText(Float.toString(mItem.getTotalCost()));
            Restaurateur restaurateur = (Restaurateur) Providers.getAuthProvider().getUser();
            textViewOrderNotes.setText(mItem.getOrderNotes());
            float deliveryCost = restaurateur.getDeliveryCost();
            textViewOrderDeliveryPrice.setText(Float.toString(deliveryCost));
            textViewOrderTotalPrice.setText(Float.toString(mItem.getTotalCost() + deliveryCost));

            setupRecyclerView(recyclerView);

        }

        return rootView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<Product> products = new ArrayList<>(mItem.getProducts().keySet());
        List<Integer> quantity = new ArrayList<>(mItem.getProducts().values());
        DoneOrderDetailFragment.ProductsRecyclerViewAdapter adapter = new DoneOrderDetailFragment.ProductsRecyclerViewAdapter(this, products, quantity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public static class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<DoneOrderDetailFragment.ProductsRecyclerViewAdapter.ViewHolder> {
        private final DoneOrderDetailFragment parentActivity;
        private final List<Product> products;
        private final List<Integer> quantity;

        private final View.OnClickListener itemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        };

        ProductsRecyclerViewAdapter(DoneOrderDetailFragment parent, List<Product> products, List<Integer> quantity) {
            this.products = products;
            this.quantity = quantity;
            this.parentActivity = parent;
        }

        @Override
        public DoneOrderDetailFragment.ProductsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_detail, parent, false);
            return new DoneOrderDetailFragment.ProductsRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final DoneOrderDetailFragment.ProductsRecyclerViewAdapter.ViewHolder holder, int position) {
            Product item = this.products.get(position);
            int productQuantity = this.quantity.get(position);
            if (item != null) {
                holder.textViewProductName.setText(item.getName());
                holder.textViewQuantity.setText(String.format("x %d", productQuantity));

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
}