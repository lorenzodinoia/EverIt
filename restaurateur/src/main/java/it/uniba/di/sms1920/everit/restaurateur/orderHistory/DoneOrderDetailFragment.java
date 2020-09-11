package it.uniba.di.sms1920.everit.restaurateur.orderHistory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.Providers;

public class DoneOrderDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private Order mItem;
    private TextView labelOrdertype;
    private TextView textViewOrderType;
    private TextView labelDeliveryDate;
    private TextView textViewLabelOrderNumber;
    private TextView textViewOrderNumber;
    private TextView textViewDeliveryTime;
    private RecyclerView recyclerView;
    private TextView textViewOrderNotes;
    private TextView textViewOrderDeliveryPrice;
    private TextView textViewSubTotalOrderPrice;
    private TextView textViewOrderTotalPrice;


    public DoneOrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            if (getArguments().containsKey(ARG_ITEM_ID)) {
                long id = getArguments().getLong(ARG_ITEM_ID);
                mItem = DoneOrderListActivity.getOrderById(id);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.doneorder_detail, container, false);

        if (mItem != null) {
            labelOrdertype = rootView.findViewById(R.id.textViewLabelOrderType);
            textViewOrderType = rootView.findViewById(R.id.textViewOrderType);
            labelDeliveryDate = rootView.findViewById(R.id.labelDeliveryDate);
            textViewLabelOrderNumber = rootView.findViewById(R.id.textViewLabelOrderNumber);
            textViewOrderNumber = rootView.findViewById(R.id.textViewOrderNumber);
            textViewDeliveryTime = rootView.findViewById(R.id.textViewDeliveryDateTime);
            recyclerView = rootView.findViewById(R.id.recycleViewProducts);
            textViewOrderNotes = rootView.findViewById(R.id.textViewOrderNotes);
            textViewOrderDeliveryPrice = rootView.findViewById(R.id.textViewSubTotal);
            textViewSubTotalOrderPrice = rootView.findViewById(R.id.textViewDeliveryCost);
            textViewOrderTotalPrice = rootView.findViewById(R.id.textViewTotalPrice);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mItem != null) {
            labelOrdertype.setText(R.string.order_type);
            if (mItem.getOrderType().equals(Order.OrderType.HOME_DELIVERY)) {
                textViewOrderType.setText(R.string.home_delivery);
            } else {
                textViewOrderType.setText(R.string.take_away);
            }

            textViewLabelOrderNumber.setText(getString(R.string.order_number) + ":");
            labelDeliveryDate.setText(getString(R.string.delivery_date_label) + ":");

            textViewOrderNumber.setText("#" + mItem.getId());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
            LocalDateTime estimatedDeliveryTime = mItem.getEstimatedDeliveryTime();
            String dateAsString = estimatedDeliveryTime.format(formatter);
            textViewDeliveryTime.setText(dateAsString);

            textViewSubTotalOrderPrice.setText(Float.toString(mItem.getTotalCost()));
            Restaurateur restaurateur = (Restaurateur) Providers.getAuthProvider().getUser();
            textViewOrderNotes.setText(mItem.getOrderNotes());
            float deliveryCost = restaurateur.getDeliveryCost();
            textViewOrderDeliveryPrice.setText(Float.toString(deliveryCost));
            textViewOrderTotalPrice.setText(Float.toString(mItem.getTotalCost() + deliveryCost));

            setupRecyclerView(recyclerView);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<Product> products = new ArrayList<>(mItem.getProducts().keySet());
        List<Integer> quantity = new ArrayList<>(mItem.getProducts().values());
        DoneOrderDetailFragment.ProductsRecyclerViewAdapter adapter = new DoneOrderDetailFragment.ProductsRecyclerViewAdapter(this, products, quantity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public static class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<DoneOrderDetailFragment.ProductsRecyclerViewAdapter.ViewHolder> {
        private final DoneOrderDetailFragment parentActivity;
        private final List<Product> products;
        private final List<Integer> quantity;

        private final View.OnClickListener itemOnClickListener = view -> {
            return;
        };

        ProductsRecyclerViewAdapter(DoneOrderDetailFragment parent, List<Product> products, List<Integer> quantity) {
            this.products = products;
            this.quantity = quantity;
            this.parentActivity = parent;
        }

        @Override
        public DoneOrderDetailFragment.ProductsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_detail, parent, false);
            return new DoneOrderDetailFragment.ProductsRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final DoneOrderDetailFragment.ProductsRecyclerViewAdapter.ViewHolder holder, int position) {
            Product item = this.products.get(position);
            int productQuantity = this.quantity.get(position);
            if (item != null) {
                holder.textViewProductName.setText(item.getName());
                if(productQuantity > 1) {
                    holder.textViewQuantity.setText(String.format("(x %d)", productQuantity));
                }else{
                    holder.textViewQuantity.setText("");
                }

                float totalPrice = item.getPrice() * productQuantity;
                holder.textViewPrice.setText(String.valueOf(totalPrice));

                holder.itemView.setTag(item);
                holder.itemView.setOnClickListener(itemOnClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewProductName;
            final TextView textViewQuantity;
            final TextView textViewPrice;

            ViewHolder(View view) {
                super(view);
                textViewProductName = view.findViewById(R.id.textViewProductName);
                textViewQuantity = view.findViewById(R.id.textViewQuantity);
                textViewPrice = view.findViewById(R.id.textViewPrice);
            }
        }
    }
}