package it.uniba.di.sms1920.everit.rider.activities;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.RiderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class AssignedOrdersFragment extends Fragment {
    private RecyclerView assignedOrdersRecyclerView;
    private List<Order> assignedOrderList = new ArrayList<>();

    public AssignedOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assigned_orders, container, false);
        RiderRequest riderRequest = new RiderRequest();
        riderRequest.readAssignedOrders(new RequestListener<Collection<Order>>() {
            @Override
            public void successResponse(Collection<Order> response) {
                assignedOrderList = new ArrayList<>(response);
                setupRecyclerView();
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO Gestione errore richiesta
            }
        });
        this.initComponents(view);

        return view;
    }

    private void initComponents(View view) {
        this.assignedOrdersRecyclerView = view.findViewById(R.id.assigned_order_list);
    }

    private void setupRecyclerView() {
        AssignedOrderRecyclerViewAdapter recyclerViewAdapter = new AssignedOrderRecyclerViewAdapter(this, this.assignedOrderList, false);
        this.assignedOrdersRecyclerView.setAdapter(recyclerViewAdapter);
    }

    public static class AssignedOrderRecyclerViewAdapter extends RecyclerView.Adapter<AssignedOrderRecyclerViewAdapter.ViewHolder> {
        private final AssignedOrdersFragment parentFragment;
        private final List<Order> assignedOrderList;
        private final boolean towPaneMode;
        private final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order item = (Order) view.getTag();
                if (towPaneMode) {
                    Bundle arguments = new Bundle();
                    arguments.putLong(AssignedOrderDetailFragment.ARG_ITEM_ID, item.getId());
                    AssignedOrderDetailFragment fragment = new AssignedOrderDetailFragment();
                    fragment.setArguments(arguments);
                    parentFragment.getChildFragmentManager().beginTransaction().replace(R.id.assigned_order_detail_container, fragment).commit();
                }
                else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, AssignedOrderDetailActivity.class);
                    intent.putExtra(AssignedOrderDetailActivity.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        AssignedOrderRecyclerViewAdapter(AssignedOrdersFragment parentFragment, List<Order> assignedOrderList, boolean towPaneMode) {
            this.parentFragment = parentFragment;
            this.assignedOrderList = assignedOrderList;
            this.towPaneMode = towPaneMode;
        }

        @NonNull
        @Override
        public AssignedOrderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assigned_orders_list_content, parent, false);
            return new AssignedOrderRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Order item = this.assignedOrderList.get(position);
            if (item != null) {
                holder.textViewRestaurateur.setText(item.getRestaurateur().getShopName());
                holder.textViewAddress.setText(item.getDeliveryAddress().getFullAddress());
                holder.itemView.setTag(item);
                holder.itemView.setOnClickListener(this.onClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return this.assignedOrderList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewRestaurateur;
            final TextView textViewAddress;

            public ViewHolder(@NonNull View view) {
                super(view);
                textViewRestaurateur = view.findViewById(R.id.textViewRestaurateur);
                textViewAddress = view.findViewById(R.id.textViewAddress);
            }
        }
    }
}