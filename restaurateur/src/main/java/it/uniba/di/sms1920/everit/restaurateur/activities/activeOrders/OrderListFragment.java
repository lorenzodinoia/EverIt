package it.uniba.di.sms1920.everit.restaurateur.activities.activeOrders;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.BaseActivity;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class OrderListFragment extends Fragment {
    public static final String ARG_ORDER_TYPE = "order_type";
    public static final int ORDER_TYPE_NOT_CONFIRMED = 0;
    public static final int ORDER_TYPE_TO_DO = 1;

    private boolean twoPaneMode;
    public ArrayList<Order> orderList = new ArrayList<>();
    private BaseActivity parentBaseActivity;
    private int orderType;
    private OrderRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private TextView textViewEmptyData;
    private SwipeRefreshLayout swipeRefreshLayout;

    public OrderListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        Bundle arguments = getArguments();
        if ((arguments != null) && (arguments.containsKey(ARG_ORDER_TYPE))) {
            this.orderType = arguments.getInt(ARG_ORDER_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_order_list, container, false);
        this.initUi(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.loadData();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof BaseActivity) {
            this.parentBaseActivity = (BaseActivity) context;
        }
    }

    private void initUi(View view) {
        if (view.findViewById(R.id.order_detail_container) != null) {
            this.twoPaneMode = true;
        }
        this.swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        this.swipeRefreshLayout.setOnRefreshListener(this::loadData);
        this.recyclerView = view.findViewById(R.id.order_list);
        this.textViewEmptyData = view.findViewById(R.id.textViewEmptyDataActiveOrders);
        this.setupRecyclerView();
    }

    private void setupRecyclerView() {
        this.recyclerViewAdapter = new OrderListFragment.OrderRecyclerViewAdapter(this, orderList, twoPaneMode, parentBaseActivity);
        this.recyclerView.setAdapter(recyclerViewAdapter);
    }

    public void loadData() {
        OrderRequest orderRequest = new OrderRequest();
        if (this.orderType == ORDER_TYPE_NOT_CONFIRMED) {
            orderRequest.readPendingOrders(new RequestListener<Collection<Order>>() {
                @Override
                public void successResponse(Collection<Order> response) {
                    stopRefreshLayout();
                    setupData(response);
                }

                @Override
                public void errorResponse(RequestException error) {
                    stopRefreshLayout();
                    Context context = getContext();
                    if (context != null) {
                        Utility.showGenericMessage(context, error.getMessage());
                    }
                }
            });
        }
        else if (this.orderType == ORDER_TYPE_TO_DO) {
            orderRequest.readToDoOrders(new RequestListener<Collection<Order>>() {
                @Override
                public void successResponse(Collection<Order> response) {
                    setupData(response);
                    stopRefreshLayout();
                }

                @Override
                public void errorResponse(RequestException error) {
                    stopRefreshLayout();
                    Context context = getContext();
                    if (context != null) {
                        Utility.showGenericMessage(context, error.getMessage());
                    }
                }
            });
        }
    }

    private void setupData(Collection<Order> orders) {
        this.orderList.clear();
        if (!orders.isEmpty()) {
            this.textViewEmptyData.setVisibility(View.INVISIBLE);
            this.orderList.addAll(orders);
        }
        else {
            this.textViewEmptyData.setVisibility(View.VISIBLE);
            if (this.orderType == ORDER_TYPE_NOT_CONFIRMED) {
                this.textViewEmptyData.setText(R.string.empty_order_not_confirmed);
            }
            else if (this.orderType == ORDER_TYPE_TO_DO) {
                this.textViewEmptyData.setText(R.string.empty_order_to_do);
            }
        }
        if (this.recyclerViewAdapter != null) {
            this.recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void stopRefreshLayout() {
        if (this.swipeRefreshLayout != null) {
            this.swipeRefreshLayout.setRefreshing(false);
        }
    }

    public static class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderListFragment.OrderRecyclerViewAdapter.ViewHolder> {
        private final BaseActivity mParent;
        private final OrderListFragment parentActivity;
        private final List<Order> orders;
        private final boolean twoPaneMode;
        private final View.OnClickListener itemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order item = (Order) view.getTag();
                if (twoPaneMode) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(OrderDetailFragment.ARG_ITEM, item);
                    OrderDetailFragment fragment = new OrderDetailFragment();
                    fragment.setArguments(arguments);
                    parentActivity.parentBaseActivity.getSupportFragmentManager().beginTransaction().replace(R.id.order_detail_container, fragment).commit();
                }
                else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra(OrderDetailActivity.ARG_ITEM_ID, item.getId());
                    context.startActivity(intent);
                }
            }
        };

        OrderRecyclerViewAdapter(OrderListFragment parent, List<Order> orders, boolean twoPane, BaseActivity mParent) {
            this.orders = orders;
            this.parentActivity = parent;
            this.twoPaneMode = twoPane;
            this.mParent = mParent;
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
                if(item.getOrderType().equals(Order.OrderType.HOME_DELIVERY)){
                    holder.chipListActiveOrders.setText(R.string.home_delivery);
                    holder.chipListActiveOrders.setChipIcon(ContextCompat.getDrawable(mParent, R.drawable.ic_delivery_12px));
                    holder.chipListActiveOrders.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(mParent, R.color.colorPrimary)));
                }
                else{
                    holder.chipListActiveOrders.setText(R.string.take_away);
                    holder.chipListActiveOrders.setChipIcon(ContextCompat.getDrawable(mParent, R.drawable.ic_take_away_12px));
                    holder.chipListActiveOrders.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(mParent, R.color.colorAccent)));
                }

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
            final Chip chipListActiveOrders;
            final TextView textViewOrderNumber;
            final TextView textViewDeliveryDate;
            final TextView textViewPrice;

            ViewHolder(View view) {
                super(view);
                chipListActiveOrders = view.findViewById(R.id.chipListActiveOrders);
                textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
                textViewPrice = view.findViewById(R.id.textViewOrderPrice);
                textViewDeliveryDate = view.findViewById(R.id.textViewDeliveryTimeOrder);
            }
        }
    }
}