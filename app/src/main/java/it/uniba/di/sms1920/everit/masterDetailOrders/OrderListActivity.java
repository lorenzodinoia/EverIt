package it.uniba.di.sms1920.everit.masterDetailOrders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.uniba.di.sms1920.everit.R;
import it.uniba.di.sms1920.everit.models.Order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrderListActivity extends AppCompatActivity {
    private boolean twoPaneMode;
    @SuppressLint("UseSparseArrays")
    public static final Map<Integer, Order> orderMap = new HashMap<>();

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
            twoPaneMode = true;
        }

        View recyclerView = findViewById(R.id.order_list);
        assert recyclerView != null; //TODO Sto assert pare messo qui perchè pesava il culo mettere un if del cazzo, e meno male che lo ha scritto Google...

        //TODO decommentare per attivare richiesta
        /*OrderRequest orderRequest = new OrderRequest();
        orderRequest.readAll(new RequestListener<Collection<Order>>() {
            @Override
            public void successResponse(Collection<Order> response) {
                for(Order i : response){
                    orderMap.put(i.getId(), i);
                }

                Adapter adapter = AdapterProvider.getAdapterFor(Order.class);
                setupRecyclerView((RecyclerView) recyclerView);
            }

            @Override
            public void errorResponse(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
            }
        });*/


        //TODO Fake data but can't access to restaurateur builder
        /*Map<Product, Integer> products = new HashMap<Product, Integer>();
        for(int i=0; i < 25; i++){
            products.put(new Product("product name", 20, "product details", new ProductCategory("product category"), new Restaurateur), i)
        }
        for(int i=0; i < 25; i++){
            orderMap.put(i, new Order.Builder("delivery address", new Date(), products,));
        }
        setupRecyclerView((RecyclerView) recyclerView);*/
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new OrderRecyclerViewAdapter(this, orderMap, twoPaneMode));
    }

    public static class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {
        private final OrderListActivity parentActivity;
        private final Map<Integer, Order> items;
        private final boolean twoPaneMode;
        private final View.OnClickListener itemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order item = (Order) view.getTag();
                if (twoPaneMode) {
                    Bundle arguments = new Bundle();
                    arguments.putString(OrderDetailFragment.ARG_ITEM_ID, Long.toString(item.getId()));
                    OrderDetailFragment fragment = new OrderDetailFragment();
                    fragment.setArguments(arguments);
                    parentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.order_detail_container, fragment)
                            .commit();
                }
                else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra(OrderDetailFragment.ARG_ITEM_ID, Long.toString(item.getId()));

                    context.startActivity(intent);
                }
            }
        };

        OrderRecyclerViewAdapter(OrderListActivity parent, Map<Integer, Order> items, boolean twoPane) {
            this.items = items;
            parentActivity = parent;
            twoPaneMode = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_content, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Order item = this.items.get(position);
            if (item != null) {
                holder.textViewId.setText(String.format("ID %d", item.getId()));
                holder.textViewActivityName.setText(item.getRestaurateur().getShopName());
                holder.textViewPrice.setText(String.format("€ %.2f", item.getTotalCost()));
                DateFormat dateFormat = new SimpleDateFormat(" dd/MM/yyyy hh:mm", Locale.getDefault());
                String stringDate = dateFormat.format(item.getActualDeliveryTime());
                holder.textViewDeliveryDate.setText(stringDate);

                holder.itemView.setTag(item);
                holder.itemView.setOnClickListener(itemOnClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
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
