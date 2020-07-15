package it.uniba.di.sms1920.everit.restaurateur.activities.orderHistory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import it.uniba.di.sms1920.everit.restaurateur.R;

import it.uniba.di.sms1920.everit.restaurateur.activities.activeOrders.OrderListFragment;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * An activity representing a list of DoneOrders. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DoneOrderDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class DoneOrderListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public static final List<Order> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doneorder_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (findViewById(R.id.doneorder_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.doneorder_list);
        assert recyclerView != null;

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.readDoneOrders(new RequestListener<Collection<Order>>() {
            @Override
            public void successResponse(Collection<Order> response) {
                orders.clear();
                if(!response.isEmpty()){
                    orders.addAll(response);
                }
                else{
                    //TODO gestire recyclerView senza dati
                }
                setupRecyclerView((RecyclerView) recyclerView);
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO gestire error response
            }
        });
    }

    public static Order getOrderById(long id) {
        Order order = null;

        for (Order o : DoneOrderListActivity.orders) {
            if (o.getId() == id) {
                order = o;
                break;
            }
        }

        return order;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new DoneOrderRecyclerViewAdapter(this, orders, mTwoPane));
    }

    public static class DoneOrderRecyclerViewAdapter
            extends RecyclerView.Adapter<DoneOrderRecyclerViewAdapter.ViewHolder> {

        private final DoneOrderListActivity mParentActivity;
        private final List<Order> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order item = (Order) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putLong(DoneOrderDetailFragment.ARG_ITEM_ID, item.getId());
                    DoneOrderDetailFragment fragment = new DoneOrderDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.doneorder_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, DoneOrderDetailActivity.class);
                    intent.putExtra(DoneOrderDetailFragment.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        DoneOrderRecyclerViewAdapter(DoneOrderListActivity parent,
                                      List<Order> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.doneorder_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.textViewOrderNumber.setText("#" + mValues.get(position).getId());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
            //TODO fare il controllo per mettere actual delivery time?
            LocalDateTime estimatedDeliveryTime = mValues.get(position).getEstimatedDeliveryTime();
            String dateAsString = estimatedDeliveryTime.format(formatter);
            holder.textViewOrderDeliveryTime.setText(dateAsString);

            Restaurateur restaurateur = (Restaurateur) Providers.getAuthProvider().getUser();
            holder.textViewOrderPrice.setText(mValues.get(position).getTotalCost()+restaurateur.getDeliveryCost() + "€");

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewOrderNumber;
            final TextView textViewOrderDeliveryTime;
            final TextView textViewOrderPrice;

            ViewHolder(View view) {
                super(view);
                textViewOrderNumber = view.findViewById(R.id.textViewDoneOrderNumber);
                textViewOrderDeliveryTime = view.findViewById(R.id.textViewDeliveryTimeDoneOrder);
                textViewOrderPrice = view.findViewById(R.id.textViewDoneOrderPrice);
            }
        }
    }
}