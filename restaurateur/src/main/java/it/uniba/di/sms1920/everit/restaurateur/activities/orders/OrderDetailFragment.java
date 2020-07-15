package it.uniba.di.sms1920.everit.restaurateur.activities.orders;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String INDEX = "fragment_index";


    private Order order;
    private int index;

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
            index = getArguments().getInt(INDEX);
            long id = getArguments().getLong(ARG_ITEM_ID);
            order = OrderListFragment.getOrderById(id);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            /*if (appBarLayout != null) {
                //appBarLayout.setTitle(order.content);
            }*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_detail, container, false);

        if (order != null) {
            TextView textViewOrderNumber = rootView.findViewById(R.id.textViewOrderNumberProduct);
            TextView textViewDeliveryTime = rootView.findViewById(R.id.textViewOrderDeliveryTime);
            //TODO se si imposta un ordine come confermato viene visualizzato in entrambe le tab
            RecyclerView recyclerView = rootView.findViewById(R.id.productsRecyclerView);
            TextView textViewOrderNotes = rootView.findViewById(R.id.textViewOrderNotes);
            TextView textViewOrderDeliveryPrice = rootView.findViewById(R.id.textViewOrderDeliveryPrice);
            TextView textViewSubTotalOrderPrice = rootView.findViewById(R.id.textViewOrderSubTotalPrice);
            TextView textViewOrderTotalPrice = rootView.findViewById(R.id.textViewOrderTotalPrice);
            MaterialButton confirmButton = rootView.findViewById(R.id.btnConfirmOrder);
            if(index != 0){
                confirmButton.setText(R.string.late_button);
            }

            textViewOrderNumber.setText(Long.toString(order.getId()));

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
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderRequest orderRequest = new OrderRequest();
                    if(index == 0){
                        orderRequest.markAsConfirmed(order.getId(), new RequestListener<Order>() {
                            @Override
                            public void successResponse(Order response) {
                                Log.d("test", "Edited");
                            }

                            @Override
                            public void errorResponse(RequestException error) {
                                Log.d("test", "Not edited");
                            }
                        });
                    }
                    else if(index == 1){
                        orderRequest.markAsLate(order.getId(), new RequestListener<Order>() {
                            @Override
                            public void successResponse(Order response) {
                                Log.d("test", "Late");
                            }

                            @Override
                            public void errorResponse(RequestException error) {
                                Log.d("test", "Not late");
                            }
                        });
                    }
                }
            });


        }

        return rootView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<Product> products = new ArrayList<>(order.getProducts().keySet());
        List<Integer> quantity = new ArrayList<>(order.getProducts().values());
        ProductsRecyclerViewAdapter adapter = new OrderDetailFragment.ProductsRecyclerViewAdapter(this, products, quantity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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