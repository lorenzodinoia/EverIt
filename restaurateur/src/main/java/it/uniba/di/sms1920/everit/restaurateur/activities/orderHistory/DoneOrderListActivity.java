package it.uniba.di.sms1920.everit.restaurateur.activities.orderHistory;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import it.uniba.di.sms1920.everit.restaurateur.R;

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

public class DoneOrderListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    public static final List<Order> orders = new ArrayList<>();
    private TextView textViewEmptyOrders;

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
            mTwoPane = true;
        }

        textViewEmptyOrders = findViewById(R.id.textViewEmptyDataOrderHistory);
        View recyclerView = findViewById(R.id.doneorder_list);
        assert recyclerView != null;

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.readDoneOrders(new RequestListener<Collection<Order>>() {
            @Override
            public void successResponse(Collection<Order> response) {
                orders.clear();
                if(!response.isEmpty()){
                    textViewEmptyOrders.setVisibility(View.INVISIBLE);
                    orders.addAll(response);
                }
                else{
                    textViewEmptyOrders.setVisibility(View.VISIBLE);
                    textViewEmptyOrders.setText(R.string.empty_order_history);
                }
                setupRecyclerView((RecyclerView) recyclerView);
            }

            @Override
            public void errorResponse(RequestException error) {
                promptErrorMessage(error.getMessage());
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

    public static class DoneOrderRecyclerViewAdapter extends RecyclerView.Adapter<DoneOrderRecyclerViewAdapter.ViewHolder> {
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

        DoneOrderRecyclerViewAdapter(DoneOrderListActivity parent, List<Order> items, boolean twoPane) {
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

            if(mValues.get(position).getOrderType().equals(Order.OrderType.HOME_DELIVERY)){
                holder.chipListActiveOrders.setText(R.string.home_delivery);
                holder.chipListActiveOrders.setChipIcon(ContextCompat.getDrawable(mParentActivity, R.drawable.ic_delivery_12px));
                holder.chipListActiveOrders.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(mParentActivity, R.color.colorPrimary)));
            }
            else{
                holder.chipListActiveOrders.setText(R.string.take_away);
                holder.chipListActiveOrders.setChipIcon(ContextCompat.getDrawable(mParentActivity, R.drawable.ic_take_away_12px));
                holder.chipListActiveOrders.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(mParentActivity, R.color.colorAccent)));
            }
            holder.textViewOrderNumber.setText("#" + mValues.get(position).getId());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);

            LocalDateTime estimatedDeliveryTime = mValues.get(position).getEstimatedDeliveryTime();
            String dateAsString = estimatedDeliveryTime.format(formatter);
            holder.textViewOrderDeliveryTime.setText(dateAsString);

            Restaurateur restaurateur = (Restaurateur) Providers.getAuthProvider().getUser();
            holder.textViewOrderPrice.setText(mValues.get(position).getTotalCost()+restaurateur.getDeliveryCost() + "");

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final Chip chipListActiveOrders;
            final TextView textViewOrderNumber;
            final TextView textViewOrderDeliveryTime;
            final TextView textViewOrderPrice;

            ViewHolder(View view) {
                super(view);
                chipListActiveOrders = view.findViewById(R.id.chipListActiveOrders);
                textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
                textViewOrderDeliveryTime = view.findViewById(R.id.textViewDeliveryTimeOrder);
                textViewOrderPrice = view.findViewById(R.id.textViewOrderPrice);
            }
        }
    }

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }
}