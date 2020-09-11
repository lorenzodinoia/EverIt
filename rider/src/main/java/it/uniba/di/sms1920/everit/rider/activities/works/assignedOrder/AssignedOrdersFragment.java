package it.uniba.di.sms1920.everit.rider.activities.works.assignedOrder;

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

public class AssignedOrdersFragment extends Fragment{
    private RecyclerView assignedOrdersRecyclerView;
    private AssignedOrderRecyclerViewAdapter assignedOrdersRecyclerViewAdapter;
    private List<Order> assignedOrderList = new ArrayList<>();
    private TextView textViewEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;

    public AssignedOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proposals, container, false);
        this.initUi(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.loadData();
    }

    private void initUi(View view) {
        this.assignedOrdersRecyclerView = view.findViewById(R.id.proposal_list);
        this.textViewEmpty = view.findViewById(R.id.textViewEmpty);
        this.swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        this.swipeRefreshLayout.setOnRefreshListener(this::loadData);
        this.setupRecyclerView();
    }

    private void setupRecyclerView() {
        this.assignedOrdersRecyclerViewAdapter = new AssignedOrderRecyclerViewAdapter(this, assignedOrderList, false);
        this.assignedOrdersRecyclerView.setAdapter(this.assignedOrdersRecyclerViewAdapter);
    }

    public void loadData() {
        RiderRequest riderRequest = new RiderRequest();
        riderRequest.readAssignedOrders(new RequestListener<Collection<Order>>() {
            @Override
            public void successResponse(Collection<Order> response) {
                stopRefreshLayout();
                assignedOrderList.clear();
                assignedOrderList.addAll(response);
                if (assignedOrdersRecyclerViewAdapter == null) {
                    setupRecyclerView();
                }
                else {
                    assignedOrdersRecyclerViewAdapter.notifyDataSetChanged();
                }

                if (assignedOrderList.size() > 0) {
                    textViewEmpty.setVisibility(View.INVISIBLE);
                }
                else {
                    textViewEmpty.setVisibility(View.VISIBLE);
                    textViewEmpty.setText(R.string.assigned_order_empty_placeholder);
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

    public static class AssignedOrderRecyclerViewAdapter extends RecyclerView.Adapter<AssignedOrderRecyclerViewAdapter.ViewHolder> {
        private final AssignedOrdersFragment parentFragment;
        private final List<Order> assignedOrderList;
        private final boolean twoPaneMode;
        private final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order item = (Order) view.getTag();
                if (twoPaneMode) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(AssignedOrderDetailFragment.ARG_ITEM, item);
                    AssignedOrderDetailFragment fragment = new AssignedOrderDetailFragment();
                    fragment.setArguments(arguments);
                    parentFragment.getChildFragmentManager().beginTransaction().replace(R.id.proposal_detail_container, fragment).commit();
                }
                else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, AssignedOrderDetailActivity.class);
                    intent.putExtra(AssignedOrderDetailActivity.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        AssignedOrderRecyclerViewAdapter(AssignedOrdersFragment parentFragment, List<Order> assignedOrderList, boolean twoPaneMode) {
            this.parentFragment = parentFragment;
            this.assignedOrderList = assignedOrderList;
            this.twoPaneMode = twoPaneMode;
        }

        @NonNull
        @Override
        public AssignedOrderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposal_list_content, parent, false);
            return new AssignedOrderRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AssignedOrdersFragment.AssignedOrderRecyclerViewAdapter.ViewHolder holder, int position) {
            if(!assignedOrderList.isEmpty()) {
                Order item = this.assignedOrderList.get(position);
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
                    if (item.isLate()) {
                        holder.chipOrderLate.setVisibility(View.VISIBLE);
                    }
                    holder.textViewRestaurateur.setText(item.getRestaurateur().getShopName());
                    holder.textViewPickupAddress.setText(item.getRestaurateur().getAddress().getFullAddress());
                    holder.textViewDeliveryAddress.setText(item.getDeliveryAddress().getFullAddress());
                    holder.textViewDeliveryCost.setText(String.valueOf(item.getRestaurateur().getDeliveryCost()));
                    holder.itemView.setTag(item);
                    holder.itemView.setOnClickListener(this.onClickListener);
                }
            }
        }

        @Override
        public int getItemCount() {
            return this.assignedOrderList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewOrderNumber;
            final ImageView imageView;
            final Chip chipOrderLate;
            final TextView textViewRestaurateur;
            final TextView textViewPickupAddress;
            final TextView textViewDeliveryAddress;
            final TextView textViewDeliveryCost;

            public ViewHolder(@NonNull View view) {
                super(view);
                textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
                imageView = view.findViewById(R.id.imageViewRestaurantLogo);
                chipOrderLate = view.findViewById(R.id.chipOrderLate);
                textViewRestaurateur = view.findViewById(R.id.textViewRestaurateur);
                textViewDeliveryAddress = view.findViewById(R.id.textViewAddressToDeliver);
                textViewPickupAddress = view.findViewById(R.id.textViewAddressToPickup);
                textViewDeliveryCost = view.findViewById(R.id.textViewDeliveryCost);
            }
        }
    }
}