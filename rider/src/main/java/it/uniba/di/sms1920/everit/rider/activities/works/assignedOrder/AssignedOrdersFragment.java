package it.uniba.di.sms1920.everit.rider.activities.works.assignedOrder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.RiderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AssignedOrdersFragment extends Fragment{
    private RecyclerView assignedOrdersRecyclerView;
    AssignedOrderRecyclerViewAdapter assignedOrdersRecyclerViewAdapter;
    private List<Order> assignedOrderList = new ArrayList<>();
    private TextView textViewEmpty;

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
        this.initComponents(view);
        this.refreshData();
        return view;
    }

    private void initComponents(View view) {
        this.assignedOrdersRecyclerView = view.findViewById(R.id.proposal_list);
        this.textViewEmpty = view.findViewById(R.id.textViewEmpty);
    }

    private void setupRecyclerView() {
        this.assignedOrdersRecyclerViewAdapter = new AssignedOrderRecyclerViewAdapter(this, assignedOrderList, false);
        this.assignedOrdersRecyclerView.setAdapter(this.assignedOrdersRecyclerViewAdapter);
    }

    /*@Override
    public void refreshData() {
        RiderRequest riderRequest = new RiderRequest();
        riderRequest.readAssignedOrders(new RequestListener<Collection<Order>>() {
            @Override
            public void successResponse(Collection<Order> response) {
                assignedOrderList = new ArrayList<>(response);
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
                promptErrorMessage(error.getMessage());
            }
        });
    }*/

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    public void refreshData() {
        RiderRequest riderRequest = new RiderRequest();
        riderRequest.readAssignedOrders(new RequestListener<Collection<Order>>() {
            @Override
            public void successResponse(Collection<Order> response) {
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
                promptErrorMessage(error.getMessage());
            }
        });
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
                    arguments.putLong(AssignedOrderDetailFragment.ARG_ITEM_ID, item.getId());
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
                    holder.textViewOrderNumber.setText("#" + item.getId());

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

            public ViewHolder(@NonNull View view) {
                super(view);
                textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
                imageView = view.findViewById(R.id.imageViewRestaurantLogo);
                chipOrderLate = view.findViewById(R.id.chipOrderLate);
                textViewRestaurateur = view.findViewById(R.id.textViewRestaurateur);
                textViewDeliveryAddress = view.findViewById(R.id.textViewAddressToDeliver);
                textViewPickupAddress = view.findViewById(R.id.textViewAddressToPickup);
            }
        }
    }

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
            getActivity().finish();
        });

        dialog.show();
    }
}