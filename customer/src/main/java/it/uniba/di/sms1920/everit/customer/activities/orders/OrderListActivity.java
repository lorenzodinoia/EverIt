package it.uniba.di.sms1920.everit.customer.activities.orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.AuthProvider;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.RequestListener;


public class OrderListActivity extends AppCompatActivity {
    private boolean twoPaneMode;
    @SuppressLint("UseSparseArrays")
    public static final List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.order_detail_container) != null) {
            /*
             * Se il layout è presente vuol dire che l'app è installata su un dispositivo di grandi dimensioni
             * Pertanto si utilizza la modalità con due pannelli
             */
            this.twoPaneMode = true;
        }

        View recyclerView = findViewById(R.id.order_list);
        assert recyclerView != null; //TODO Sto assert pare messo qui perchè pesava il culo mettere un if del cazzo, e meno male che lo ha scritto Google...

        //TODO Rimuovere login. Inserito solo a scopo di test
        AuthProvider.getInstance().login("mario.rossi@gmail.com", "password123", new RequestListener<Boolean>() {
            @Override
            public void successResponse(Boolean response) {
                OrderRequest orderRequest = new OrderRequest();
                orderRequest.readAll(new RequestListener<Collection<Order>>() {
                    @Override
                    public void successResponse(Collection<Order> response) {
                        //TODO Salvare order list in modo da non rifare la richiesta ad ogni rotazione dello schermo
                        orderList.clear();
                        orderList.addAll(response);
                        setupRecyclerView((RecyclerView) recyclerView);
                    }

                    @Override
                    public void errorResponse(String error) {
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void errorResponse(String error) {
                Toast.makeText(getApplicationContext(), "Niente login", Toast.LENGTH_LONG).show();
            }
        }, false);
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
                    parentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.order_detail_container, fragment)
                            .commit();
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
                    //TODO Aggiungere un simbolo per avvertire che l'ordine è ancora da consegnare
                    dateAsString = dateFormat.format(item.getEstimatedDeliveryTime());
                }

                holder.textViewId.setText(String.format(Locale.getDefault(), "# %d", item.getId()));
                holder.textViewActivityName.setText(item.getRestaurateur().getShopName());
                holder.textViewPrice.setText(String.format(Locale.getDefault(), "€ %.2f", item.getTotalCost()));
                holder.textViewDeliveryDate.setText(dateAsString);

                holder.itemView.setTag(item);
                holder.itemView.setOnClickListener(itemOnClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewId;
            final TextView textViewActivityName;
            final TextView textViewPrice;
            final TextView textViewDeliveryDate;
            final ImageView imageViewRestaurateur;

            ViewHolder(View view) {
                super(view);
                textViewId = (TextView) view.findViewById(R.id.textViewId);
                textViewActivityName = (TextView) view.findViewById(R.id.textViewActivityName);
                textViewPrice = (TextView) view.findViewById(R.id.textViewPrice);
                textViewDeliveryDate = (TextView) view.findViewById(R.id.textViewOrderDate);
                imageViewRestaurateur = (ImageView) view.findViewById(R.id.imageViewRestaurateur);
            }
        }
    }
}