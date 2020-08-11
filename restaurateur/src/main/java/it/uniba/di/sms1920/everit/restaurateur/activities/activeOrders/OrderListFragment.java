package it.uniba.di.sms1920.everit.restaurateur.activities.activeOrders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.BaseActivity;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class OrderListFragment extends Fragment {

    private static final int NOT_CONFIRMED = 0;
    private static final int TO_DO = 1;

    private  boolean mTwoPane;
    public static final List<Order> orderList = new ArrayList<>();
    private BaseActivity mParent;
    private int index;
    private OrderRecyclerViewAdapter adapter;
    View recyclerView;
    private TextView textViewEmptyData;

    //TODO capire come aggiornare i dati alla chiusura dell'orderDetailFragment

    public OrderListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_order_list, container, false);

        if (view.findViewById(R.id.order_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        textViewEmptyData = view.findViewById(R.id.textViewEmptyDataActiveOrders);
        recyclerView = view.findViewById(R.id.order_list);
        assert recyclerView != null;

        OrderRequest orderRequest = new OrderRequest();
        if(index == NOT_CONFIRMED) {
            orderRequest.readPendingOrders(new RequestListener<Collection<Order>>() {
                @Override
                public void successResponse(Collection<Order> response) {
                    orderList.clear();
                    if (!response.isEmpty()) {
                        textViewEmptyData.setVisibility(View.INVISIBLE);
                        orderList.addAll(response);
                        setupRecyclerView((RecyclerView) recyclerView);
                    } else {
                        textViewEmptyData.setVisibility(View.VISIBLE);
                        textViewEmptyData.setText(R.string.empty_order_not_confirmed);
                    }

                }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO gestire error response
                }
            });
        }
        else if(index == TO_DO){
            orderRequest.readToDoOrders(new RequestListener<Collection<Order>>() {
                @Override
                public void successResponse(Collection<Order> response) {
                    orderList.clear();
                    if(response.isEmpty()){
                        textViewEmptyData.setVisibility(View.INVISIBLE);
                        orderList.addAll(response);
                        setupRecyclerView((RecyclerView) recyclerView);
                    }
                    else{
                        textViewEmptyData.setVisibility(View.VISIBLE);
                        textViewEmptyData.setText(R.string.empty_order_to_do);
                    }
                }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO gestire error response
                }
            });
        }
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof BaseActivity){
            //mParent = (BaseActivity) context;
        }
    }

    public static Order getOrderById(long id) {
        Order order = null;

        for (Order o : OrderListFragment.orderList) {
            if (o.getId() == id) {
                order = o;
                break;
            }
        }

        return order;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public void refreshData(){
        adapter.notifyDataSetChanged();
    }

    public void updateData(){
        OrderRequest orderRequest = new OrderRequest();
        if(index == NOT_CONFIRMED){
            orderRequest.readPendingOrders(new RequestListener<Collection<Order>>() {
                @Override
                public void successResponse(Collection<Order> response) {
                    orderList.clear();
                    if(!response.isEmpty()){
                        textViewEmptyData.setVisibility(View.INVISIBLE);
                        orderList.addAll(response);
                    }
                    else{
                        textViewEmptyData.setVisibility(View.VISIBLE);
                        textViewEmptyData.setText(R.string.empty_order_not_confirmed);
                    }

                    if(adapter == null){
                        setupRecyclerView((RecyclerView) recyclerView);
                    }
                    else{
                        adapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO gestire error response
                }
            });
        }
        else if(index == TO_DO){
            orderRequest.readToDoOrders(new RequestListener<Collection<Order>>() {
                @Override
                public void successResponse(Collection<Order> response) {
                    orderList.clear();
                    if(!response.isEmpty()){
                        textViewEmptyData.setVisibility(View.INVISIBLE);
                        orderList.addAll(response);
                    }
                    else{
                        textViewEmptyData.setVisibility(View.VISIBLE);
                        textViewEmptyData.setText(R.string.empty_order_to_do);
                    }

                    if(adapter == null){
                        setupRecyclerView((RecyclerView) recyclerView);
                    }
                    else{
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO gestire error response
                }
            });
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new OrderListFragment.OrderRecyclerViewAdapter(this, orderList, mTwoPane);
        recyclerView.setAdapter(adapter);
    }

    public static class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderListFragment.OrderRecyclerViewAdapter.ViewHolder> {
        private final OrderListFragment parentActivity;
        private final List<Order> orders;
        private final boolean twoPaneMode;
        private final View.OnClickListener itemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order item = (Order) view.getTag();
                if (twoPaneMode) {
                    Bundle arguments = new Bundle();
                    arguments.putLong(OrderDetailFragment.ARG_ITEM_ID, item.getId());
                    arguments.putInt(OrderDetailFragment.INDEX, parentActivity.index);
                    OrderDetailFragment fragment = new OrderDetailFragment();
                    fragment.setArguments(arguments);
                    parentActivity.mParent.getSupportFragmentManager().beginTransaction().replace(R.id.order_detail_container, fragment).commit();
                }
                else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra(OrderDetailFragment.ARG_ITEM_ID, item.getId());
                    intent.putExtra(OrderDetailFragment.INDEX, parentActivity.index);
                    context.startActivity(intent);
                }
            }
        };

        OrderRecyclerViewAdapter(OrderListFragment parent, List<Order> orders, boolean twoPane) {
            this.orders = orders;
            this.parentActivity = parent;
            this.twoPaneMode = twoPane;
        }

        @Override
        public OrderListFragment.OrderRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_content, parent, false);
            return new OrderListFragment.OrderRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final OrderListFragment.OrderRecyclerViewAdapter.ViewHolder holder, int position) {
            Order item = this.orders.get(position);
            if (item != null) {
                Restaurateur restaurateur = (Restaurateur) Providers.getAuthProvider().getUser();
                holder.textViewOrderNumber.setText("#" + item.getId());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
                LocalDateTime estimatedDeliveryTime = item.getEstimatedDeliveryTime();
                String dateAsString = estimatedDeliveryTime.format(formatter);
                holder.textViewDeliveryDate.setText(dateAsString);
                holder.textViewPrice.setText((item.getTotalCost() + restaurateur.getDeliveryCost()) + "");

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
            final TextView textViewDeliveryDate;
            final TextView textViewPrice;

            ViewHolder(View view) {
                super(view);
                textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
                textViewPrice = view.findViewById(R.id.textViewOrderPrice);
                textViewDeliveryDate = view.findViewById(R.id.textViewDeliveryTimeOrder);
            }
        }
    }
}