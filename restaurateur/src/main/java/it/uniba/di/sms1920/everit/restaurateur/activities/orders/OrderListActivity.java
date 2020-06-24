package it.uniba.di.sms1920.everit.restaurateur.activities.orders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.uniba.di.sms1920.everit.restaurateur.R;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * An activity representing a list of Orders. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link OrderDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class OrderListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public static final List<Order> orderList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.order_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.order_list);
        assert recyclerView != null;

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.readPendingOrders(new RequestListener<Collection<Order>>() {
            @Override
            public void successResponse(Collection<Order> response) {
                if(!response.isEmpty()){
                    orderList.clear();
                    orderList.addAll(response);
                    setupRecyclerView((RecyclerView) recyclerView);
                }
                else{
                    //TODO gestire caso in cui non ci sono ordini
                }
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO gestire error response
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }



    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new OrderRecyclerViewAdapter(this, orderList, mTwoPane));
    }


    public static class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {
        private final OrderListActivity parentActivity;
        private final List<Order> orders;
        private final boolean twoPaneMode;
        private final View.OnClickListener itemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order item = (Order) view.getTag();
                if (twoPaneMode) {
                    Bundle arguments = new Bundle();
                    arguments.putLong(OrderDetailFragment.ARG_ITEM_ID, item.getId());
                    OrderDetailFragment fragment = new OrderDetailFragment();
                    fragment.setArguments(arguments);
                    parentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.order_detail_container, fragment).commit();
                }
                else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra(OrderDetailFragment.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        OrderRecyclerViewAdapter(OrderListActivity parent, List<Order> orders, boolean twoPane) {
            this.orders = orders;
            this.parentActivity = parent;
            this.twoPaneMode = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Order item = this.orders.get(position);
            if (item != null) {
                String dateAsString = "";
                DateFormat dateFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT, Locale.getDefault());
                if (item.isDelivered()) {
                    if (item.getActualDeliveryTime() != null) {
                        dateAsString = dateFormat.format(item.getActualDeliveryTime());
                    }
                }
                else {
                    dateAsString = dateFormat.format(item.getEstimatedDeliveryTime());
                }

                holder.textViewOrderNumber.setText(item.getRestaurateur().getShopName());
                holder.textViewPrice.setText(String.format(Locale.getDefault(), "€ %.2f", item.getTotalCost()));
                holder.textViewDeliveryDate.setText(dateAsString);
                Order.Status orderStatus = item.getStatus();
                if (orderStatus.equals(Order.Status.ORDERED)) {
                    holder.textViewOrderStatus.setText(R.string.not_confirmed);
                }
                else if (orderStatus.equals(Order.Status.IN_PROGRESS)) {
                    holder.textViewOrderStatus.setText(R.string.confirmed);
                }
                holder.itemView.setTag(item);
                holder.itemView.setOnClickListener(itemOnClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewOrderNumber;
            final TextView textViewOrderStatus;
            final TextView textViewDeliveryDate;
            final TextView textViewPrice;

            ViewHolder(View view) {
                super(view);
                textViewOrderNumber = (TextView) view.findViewById(R.id.textViewOrderNumber);
                textViewPrice = (TextView) view.findViewById(R.id.textViewPrice);
                textViewDeliveryDate = (TextView) view.findViewById(R.id.textViewDeliveryTimeOrder);
                textViewOrderStatus = (TextView) view.findViewById(R.id.textViewOrderState);
            }
        }
    }
}