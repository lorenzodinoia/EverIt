package it.uniba.di.sms1920.everit.customer.activities.orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.CartActivity;
import it.uniba.di.sms1920.everit.customer.activities.LoginActivity;
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
    public static final List<Order> orderList = new ArrayList<>();

    //TODO aggiungere gestione se non ci sono dati (anche nelle review)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_default);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (findViewById(R.id.order_detail_container) != null) {
            /*
             * Se il layout è presente vuol dire che l'app è installata su un dispositivo di grandi dimensioni
             * Pertanto si utilizza la modalità con due pannelli
             */
            this.twoPaneMode = true;
        }

        View recyclerView = findViewById(R.id.order_list);
        assert recyclerView != null;

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.readAll(new RequestListener<Collection<Order>>() {
            @Override
            public void successResponse(Collection<Order> response) {
                if(!response.isEmpty()) {
                    orderList.clear();
                    orderList.addAll(response);
                    setupRecyclerView((RecyclerView) recyclerView);
                }
                else{

                }
            }

            @Override
            public void errorResponse(RequestException error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                super.onBackPressed();
            }

            case R.id.goTo_cart:{
                if(Providers.getAuthProvider().getUser() != null) {
                    Intent cartIntent = new Intent(getApplicationContext(), CartActivity.class);
                    startActivity(cartIntent);
                }else{
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginIntent);
                }
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new OrderRecyclerViewAdapter(this, orderList, twoPaneMode));
    }

    public static Order getOrderById(long id) {
        Order order = null;

        for (Order o : OrderListActivity.orderList) {
            if (o.getId() == id) {
                order = o;
                break;
            }
        }

        return order;
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

                holder.textViewActivityName.setText(item.getRestaurateur().getShopName());
                holder.textViewPrice.setText(String.format(Locale.getDefault(), "€ %.2f", item.getTotalCost()));
                holder.textViewDeliveryDate.setText(dateAsString);
                if(item.getRestaurateur().getImagePath() != null){
                    String imageUrl = String.format("%s/%s", Constants.SERVER_HOST, item.getRestaurateur().getImagePath());
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
            final TextView textViewActivityName;
            final TextView textViewPrice;
            final TextView textViewDeliveryDate;
            final TextView textViewOrderStatus;
            final ImageView imageViewRestaurateur;

            ViewHolder(View view) {
                super(view);
                textViewActivityName = (TextView) view.findViewById(R.id.textViewActivityName);
                textViewPrice = (TextView) view.findViewById(R.id.textViewPrice);
                textViewDeliveryDate = (TextView) view.findViewById(R.id.textViewOrderDate);
                textViewOrderStatus = (TextView) view.findViewById(R.id.textViewOrderStatus);
                imageViewRestaurateur = (ImageView) view.findViewById(R.id.imageViewRestaurateur);
            }
        }
    }
}
