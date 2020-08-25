package it.uniba.di.sms1920.everit.rider.activities.deliverHistory;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import it.uniba.di.sms1920.everit.rider.R;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.RiderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class DeliveryHistoryListActivity extends AppCompatActivity {

    private TextView textViewEmptyDeliveredOrders;
    private View recyclerView;
    private boolean mTwoPane;
    private List<Order> deliveryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveryhistory_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (findViewById(R.id.deliveryhistory_detail_container) != null) {
            mTwoPane = true;
        }

        textViewEmptyDeliveredOrders = findViewById(R.id.textViewEmptyDeliveredOrders);
        recyclerView = findViewById(R.id.deliveryhistory_list);
        assert recyclerView != null;

    }

    @Override
    protected void onStart() {
        super.onStart();

        RiderRequest riderRequest = new RiderRequest();
        riderRequest.readDeliveredOrders(new RequestListener<Collection<Order>>() {
            @Override
            public void successResponse(Collection<Order> response) {
                deliveryList.clear();
                if(response.isEmpty()){
                    textViewEmptyDeliveredOrders.setVisibility(View.VISIBLE);
                    textViewEmptyDeliveredOrders.bringToFront();
                }
                else{
                    deliveryList.addAll(response);
                    setupRecyclerView((RecyclerView) recyclerView);
                }

                setupRecyclerView((RecyclerView) recyclerView);
            }

            @Override
            public void errorResponse(RequestException error) {
                promptErrorMessage(error.getMessage());
            }
        });

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new DeliveryHistoryRecyclerViewAdapter(this, deliveryList, mTwoPane));
    }

    public static class DeliveryHistoryRecyclerViewAdapter extends RecyclerView.Adapter<DeliveryHistoryRecyclerViewAdapter.ViewHolder> {

        private final DeliveryHistoryListActivity mParentActivity;
        private final List<Order> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order item = (Order) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(DeliveryHistoryDetailFragment.ARG_ITEM_ID, item);
                    DeliveryHistoryDetailFragment fragment = new DeliveryHistoryDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.deliveryhistory_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, DeliveryHistoryDetailActivity.class);
                    intent.putExtra(DeliveryHistoryDetailFragment.ARG_ITEM_ID, item);

                    context.startActivity(intent);
                }
            }
        };

        DeliveryHistoryRecyclerViewAdapter(DeliveryHistoryListActivity parent, List<Order> items, boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.deliveryhistory_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Order deliverOrder = mValues.get(position);
            if(deliverOrder != null) {

                if(deliverOrder.getRestaurateur().getImagePath() != null){
                    String imageUrl = String.format("%s/images/%s", Constants.SERVER_HOST, deliverOrder.getRestaurateur().getImagePath());
                    Picasso.get()
                            .load(imageUrl)
                            .error(R.mipmap.icon)
                            .placeholder(R.mipmap.icon)
                            .transform(new CropCircleTransformation())
                            .fit()
                            .into(holder.imageViewRestaurateur);
                }

                holder.textViewShopName.setText(deliverOrder.getRestaurateur().getShopName());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
                LocalDateTime actualDeliveryTime = deliverOrder.getActualDeliveryTime();
                String dateAsString = actualDeliveryTime.format(formatter);
                holder.textViewDeliveryTimeOrder.setText(dateAsString);

                holder.textViewCustomerAddress.setText(deliverOrder.getDeliveryAddress().getFullAddress());
                holder.textViewOrderNumber.setText("#"+deliverOrder.getId());

                holder.itemView.setTag(mValues.get(position));
                holder.itemView.setOnClickListener(mOnClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView imageViewRestaurateur;
            final TextView textViewShopName;
            final TextView textViewDeliveryTimeOrder;
            final TextView textViewCustomerAddress;
            final TextView textViewOrderNumber;

            ViewHolder(View view) {
                super(view);
                imageViewRestaurateur = view.findViewById(R.id.imageViewRestaurateur);
                textViewShopName = view.findViewById(R.id.textViewShopName);
                textViewDeliveryTimeOrder = view.findViewById(R.id.textViewDeliveryTimeOrder);
                textViewCustomerAddress = view.findViewById(R.id.textViewCustomerAddress);
                textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}