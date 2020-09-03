package it.uniba.di.sms1920.everit.customer.activities.orders;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.BaseActivity;
import it.uniba.di.sms1920.everit.customer.activities.HomeFragment;
import it.uniba.di.sms1920.everit.customer.activities.cartActivity.CartActivity;
import it.uniba.di.sms1920.everit.customer.activities.LoginActivity;
import it.uniba.di.sms1920.everit.customer.activities.orders.tab.OrderTabManagerFragment;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class OrderListActivity extends AppCompatActivity {
    private boolean twoPaneMode;
    @SuppressLint("UseSparseArrays")
    private final ArrayList<Order> orderList = new ArrayList<>();

    private TextView textViewEmptyOrders;
    private OrderRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        this.initUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.loadData();
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar_default);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.order_detail_container) != null) {
            this.twoPaneMode = true;
        }

        this.textViewEmptyOrders = findViewById(R.id.textViewEmptyOrderHistoryCustomer);
        View recyclerView = findViewById(R.id.order_list);
        this.setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerViewAdapter = new OrderRecyclerViewAdapter(this, orderList, twoPaneMode);
        recyclerView.setAdapter(this.recyclerViewAdapter);
    }

    private void loadData() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.readAll(new RequestListener<Collection<Order>>() {
            @Override
            public void successResponse(Collection<Order> response) {
                orderList.clear();
                if(!response.isEmpty()) {
                    textViewEmptyOrders.setVisibility(View.INVISIBLE);
                    orderList.addAll(response);
                }
                else {
                    textViewEmptyOrders.setVisibility(View.VISIBLE);
                    textViewEmptyOrders.setText(R.string.no_orders);
                    textViewEmptyOrders.bringToFront();
                }
                if (recyclerViewAdapter != null) {
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResponse(RequestException error) {
                promptErrorMessage(error.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                finish();
                break;
            }


            case R.id.goTo_cart: {
                if (Providers.getAuthProvider().getUser() != null) {
                    Intent cartIntent = new Intent(getApplicationContext(), CartActivity.class);
                    startActivity(cartIntent);
                }
                else{
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    loginIntent.putExtra(LoginActivity.INTENT_FLAG, 0);
                    startActivity(loginIntent);
                }
                break;
            }
        }

        return super.onOptionsItemSelected(item);
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
                    arguments.putParcelable(OrderDetailFragment.ARG_ITEM, item);
                    OrderTabManagerFragment fragment = new OrderTabManagerFragment();
                    fragment.setArguments(arguments);
                    parentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.order_detail_container, fragment).commit();
                }
                else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra(OrderDetailActivity.ARG_ITEM_ID, item.getId());

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
                if(item.getOrderType().equals(Order.OrderType.HOME_DELIVERY)){
                    holder.chipListCustomerOrders.setText(R.string.home_delivery);
                    holder.chipListCustomerOrders.setChipIcon(ContextCompat.getDrawable(parentActivity, R.drawable.ic_delivery_12px));
                    holder.chipListCustomerOrders.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(parentActivity, R.color.colorPrimary)));
                }
                else{
                    holder.chipListCustomerOrders.setText(R.string.takeaway);
                    holder.chipListCustomerOrders.setChipIcon(ContextCompat.getDrawable(parentActivity, R.drawable.ic_take_away_12px));
                    holder.chipListCustomerOrders.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(parentActivity, R.color.colorAccent)));
                }

                Order.Status status = item.getStatus();
                switch (status){
                    case ORDERED:
                        holder.chipStatusOrder.setText(R.string.ordered);
                        break;
                    case CONFIRMED:
                        holder.chipStatusOrder.setText(R.string.confirmed);
                        break;
                    case IN_PROGRESS:
                        holder.chipStatusOrder.setText(R.string.in_preparation);
                        break;
                    case DELIVERING:
                        holder.chipStatusOrder.setText(R.string.delivering);
                        break;
                    case READY:
                        holder.chipStatusOrder.setText(R.string.ready);
                        break;
                    case DELIVERED:
                        holder.chipStatusOrder.setText(R.string.delivered);
                        break;
                }

                if(item.isLate()){
                    holder.chipOrderLate.setVisibility(View.VISIBLE);
                }

                holder.textViewOrderNumber.setText("#"+item.getId());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
                LocalDateTime estimatedDeliveryTime = item.getEstimatedDeliveryTime();
                String dateAsString = "";
                if (item.isDelivered()) {
                    if (item.getActualDeliveryTime() != null) {
                        dateAsString = estimatedDeliveryTime.format(formatter);
                    }
                }
                else {
                    dateAsString = estimatedDeliveryTime.format(formatter);
                }

                holder.textViewActivityName.setText(item.getRestaurateur().getShopName());
                float totalCost = item.getTotalCost() + item.getRestaurateur().getDeliveryCost();
                holder.textViewPrice.setText(String.format(Locale.getDefault(), "%.2f", totalCost));
                holder.textViewDeliveryDate.setText(dateAsString);
                if(item.getRestaurateur().getImagePath() != null){
                    String imageUrl = String.format("%s/images/%s", Constants.SERVER_HOST, item.getRestaurateur().getImagePath());
                    Picasso.get()
                            .load(imageUrl)
                            .error(R.mipmap.icon)
                            .placeholder(R.mipmap.icon)
                            .transform(new CropCircleTransformation())
                            .fit()
                            .into(holder.imageViewRestaurateur);
                }
                Order.Status orderStatus = item.getStatus();
                if (orderStatus.equals(Order.Status.ORDERED)) {
                    holder.textViewOrderStatus.setText(R.string.ordered);
                }
                else if (orderStatus.equals(Order.Status.IN_PROGRESS)) {
                    holder.textViewOrderStatus.setText(R.string.in_preparation);
                }
                else if (orderStatus.equals(Order.Status.DELIVERING)) {
                    holder.textViewOrderStatus.setText(R.string.delivering);
                }
                else if (orderStatus.equals(Order.Status.DELIVERED)) {
                    holder.textViewOrderStatus.setText(R.string.delivered);
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
            final Chip chipListCustomerOrders;
            final Chip chipStatusOrder;
            final Chip chipOrderLate;
            final TextView textViewOrderNumber;
            final TextView textViewActivityName;
            final TextView textViewPrice;
            final TextView textViewDeliveryDate;
            final TextView textViewOrderStatus;
            final ImageView imageViewRestaurateur;

            ViewHolder(View view) {
                super(view);
                chipListCustomerOrders = view.findViewById(R.id.chipListCustomerOrders);
                chipStatusOrder = view.findViewById(R.id.chipStatusOrder);
                chipOrderLate = view.findViewById(R.id.chipOrderLate);
                textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
                textViewActivityName = view.findViewById(R.id.textViewActivityName);
                textViewPrice = view.findViewById(R.id.textViewPrice);
                textViewDeliveryDate = view.findViewById(R.id.textViewOrderDate);
                textViewOrderStatus = view.findViewById(R.id.textViewOrderStatus);
                imageViewRestaurateur = view.findViewById(R.id.imageViewRestaurateur);
            }
        }
    }
}
