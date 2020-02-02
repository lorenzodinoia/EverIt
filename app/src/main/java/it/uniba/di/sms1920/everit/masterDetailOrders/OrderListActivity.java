package it.uniba.di.sms1920.everit.masterDetailOrders;

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
import android.widget.Toast;

import it.uniba.di.sms1920.everit.R;
import it.uniba.di.sms1920.everit.adapter.Adapter;
import it.uniba.di.sms1920.everit.adapter.AdapterProvider;
import it.uniba.di.sms1920.everit.models.Order;
import it.uniba.di.sms1920.everit.models.Product;
import it.uniba.di.sms1920.everit.models.ProductCategory;
import it.uniba.di.sms1920.everit.models.Restaurateur;
import it.uniba.di.sms1920.everit.request.OrderRequest;
import it.uniba.di.sms1920.everit.request.RequestListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    public static final Map<Long, Order> orderMap = new HashMap<Long, Order>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.order_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.order_list);
        assert recyclerView != null;

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
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, orderMap, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final OrderListActivity mParentActivity;
        private final Map<Long, Order> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order item = (Order) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(OrderDetailFragment.ARG_ITEM_ID, Long.toString(item.getId()));
                    OrderDetailFragment fragment = new OrderDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.order_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra(OrderDetailFragment.ARG_ITEM_ID, Long.toString(item.getId()));

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(OrderListActivity parent,
                                      Map<Long, Order> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            double totalPrice = 0;
            Map<Product, Integer> products = mValues.get(position).getProducts();
            for(Map.Entry<Product, Integer> i : products.entrySet()){
                totalPrice += i.getValue()*i.getKey().getPrice(); //Total Price = Quantity[i]*Price[i]
            }

            holder.mIdView.setText(String.format("ID %d", mValues.get(position).getId()));
            holder.mActivityNameView.setText(mValues.get(position).getRestaurateur().getShopName());
            holder.mPriceView.setText(String.format("€ %s", Double.toString(totalPrice)));
            DateFormat dateFormat = new SimpleDateFormat(" dd/MM/yyyy hh:mm", Locale.getDefault());
            String strDate = dateFormat.format(mValues.get(position).getActualDeliveryTime());
            holder.mDeliveryDate.setText(strDate);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mActivityNameView;
            final TextView mPriceView;
            final TextView mDeliveryDate;
            final ImageView mImageRestaurateur;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.textViewId);
                mActivityNameView = (TextView) view.findViewById(R.id.textViewActivityName);
                mPriceView = (TextView) view.findViewById(R.id.textViewPrice);
                mDeliveryDate = (TextView) view.findViewById(R.id.textViewOrderDate);
                mImageRestaurateur = (ImageView) view.findViewById(R.id.imageViewRestaurateur);
            }
        }
    }
}
