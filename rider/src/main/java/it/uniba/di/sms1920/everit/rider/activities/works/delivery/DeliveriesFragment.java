package it.uniba.di.sms1920.everit.rider.activities.works.delivery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.RiderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DeliveriesFragment extends Fragment {
    private RecyclerView deliveriesRecyclerView;
    private DeliveryRecyclerViewAdapter deliveriesRecyclerViewAdapter;
    private List<Order> deliveryList = new ArrayList<>();
    private TextView textViewEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;

    public DeliveriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proposals, container, false);
        this.initComponents(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.loadData();
    }

    private void initComponents(View view) {
        this.deliveriesRecyclerView = view.findViewById(R.id.proposal_list);
        this.textViewEmpty = view.findViewById(R.id.textViewEmpty);
        this.swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        this.swipeRefreshLayout.setOnRefreshListener(this::loadData);
        this.setupRecyclerView();
    }

    private void setupRecyclerView() {
        this.deliveriesRecyclerViewAdapter = new DeliveryRecyclerViewAdapter(this, this.deliveryList, false);
        this.deliveriesRecyclerView.setAdapter(this.deliveriesRecyclerViewAdapter);
    }

    public void loadData() {
        RiderRequest riderRequest = new RiderRequest();
        riderRequest.readDeliveries(new RequestListener<Collection<Order>>() {
            @Override
            public void successResponse(Collection<Order> response) {
                stopRefreshLayout();
                deliveryList.clear();
                deliveryList.addAll(response);
                if (deliveriesRecyclerViewAdapter == null) {
                    setupRecyclerView();
                }
                else {
                    deliveriesRecyclerViewAdapter.notifyDataSetChanged();
                }
                if (deliveryList.size() > 0) {
                    textViewEmpty.setVisibility(View.INVISIBLE);
                }
                else {
                    textViewEmpty.setVisibility(View.VISIBLE);
                    textViewEmpty.setText(R.string.delivery_empty_placeholder);
                }
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

    private void stopRefreshLayout() {
        if (this.swipeRefreshLayout != null) {
            this.swipeRefreshLayout.setRefreshing(false);
        }
    }

    public static class DeliveryRecyclerViewAdapter extends RecyclerView.Adapter<DeliveryRecyclerViewAdapter.ViewHolder> {
        private final DeliveriesFragment parentFragment;
        private final List<Order> deliveryList;
        private final boolean twoPaneMode;
        private final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order item = (Order) view.getTag();
                if (twoPaneMode) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(DeliveryDetailFragment.ARG_ITEM, item);
                    DeliveryDetailFragment fragment = new DeliveryDetailFragment();
                    fragment.setArguments(arguments);
                    parentFragment.getChildFragmentManager().beginTransaction().replace(R.id.proposal_detail_container, fragment).commit();
                }
                else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, DeliveryDetailActivity.class);
                    intent.putExtra(DeliveryDetailFragment.ARG_ITEM_ID, item.getId());
                    context.startActivity(intent);
                }
            }
        };

        public DeliveryRecyclerViewAdapter(DeliveriesFragment parentFragment, List<Order> deliveryList, boolean twoPaneMode) {
            this.parentFragment = parentFragment;
            this.deliveryList = deliveryList;
            this.twoPaneMode = twoPaneMode;
        }

        @NonNull
        @Override
        public DeliveryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposal_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DeliveriesFragment.DeliveryRecyclerViewAdapter.ViewHolder holder, int position) {
            if(!deliveryList.isEmpty()) {
                Order item = this.deliveryList.get(position);
                if (item != null) {
                    holder.textViewOrderNumber.setText(String.format(Locale.getDefault(), "#%d", item.getId()));
                    if (item.getRestaurateur().getImagePath() != null) {
                        String imageUrl = String.format("%s/images/%s", Constants.SERVER_HOST, item.getRestaurateur().getImagePath());
                        Picasso.get()
                                .load(imageUrl)
                                .error(R.mipmap.icon)
                                .placeholder(R.mipmap.icon)
                                .transform(new CropCircleTransformation())
                                .fit()
                                .into(holder.imageView);

                    }
                    holder.textViewRestaurateur.setText(item.getRestaurateur().getShopName());
                    holder.textViewLabelPickupAddress.setVisibility(View.GONE);
                    holder.textViewPickupAddress.setVisibility(View.GONE);
                    holder.textViewDeliveryAddress.setText(item.getDeliveryAddress().getFullAddress());
                    if (item.isLate()) {
                        holder.chipOrderLate.setVisibility(View.VISIBLE);
                    }
                    holder.itemView.setTag(item);
                    holder.itemView.setOnClickListener(this.onClickListener);
                }
            }
        }

        @Override
        public int getItemCount() {
            return this.deliveryList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewOrderNumber;
            final ImageView imageView;
            final Chip chipOrderLate;
            final TextView textViewRestaurateur;
            final TextView textViewPickupAddress;
            final TextView textViewLabelPickupAddress;
            final TextView textViewDeliveryAddress;

            public ViewHolder(@NonNull View view) {
                super(view);
                textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
                imageView = view.findViewById(R.id.imageViewRestaurantLogo);
                chipOrderLate = view.findViewById(R.id.chipOrderLate);
                textViewRestaurateur = view.findViewById(R.id.textViewRestaurateur);
                textViewDeliveryAddress = view.findViewById(R.id.textViewAddressToDeliver);
                textViewPickupAddress = view.findViewById(R.id.textViewAddressToPickup);
                textViewLabelPickupAddress = view.findViewById(R.id.labelAddressToPickup);
            }
        }
    }
}