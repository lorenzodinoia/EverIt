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

public class DeliveriesFragment extends Fragment {
    private RecyclerView deliveriesRecyclerView;
    private List<Order> deliveryList = new ArrayList<>();
    private TextView textViewEmpty;

    public DeliveriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliveries, container, false);
        this.initComponents(view);

        RiderRequest riderRequest = new RiderRequest();
        riderRequest.readDeliveries(new RequestListener<Collection<Order>>() {
            @Override
            public void successResponse(Collection<Order> response) {
                deliveryList = new ArrayList<>(response);
                if (deliveryList.size() > 0) {
                    textViewEmpty.setVisibility(View.INVISIBLE);
                    setupRecyclerView();
                }
                else {
                    textViewEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO Gestione errore richiesta
                textViewEmpty.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void initComponents(View view) {
        this.deliveriesRecyclerView = view.findViewById(R.id.deliveries_list);
        this.textViewEmpty = view.findViewById(R.id.textViewEmpty);
    }

    private void setupRecyclerView() {
        DeliveryRecyclerViewAdapter recyclerViewAdapter = new DeliveryRecyclerViewAdapter(this, this.deliveryList, false);
        this.deliveriesRecyclerView.setAdapter(recyclerViewAdapter);
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
                    arguments.putLong(DeliveryDetailFragment.ARG_ITEM_ID, item.getId());
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deliveries_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DeliveryRecyclerViewAdapter.ViewHolder holder, int position) {
            Order item = this.deliveryList.get(position);
            if (item != null) {
                holder.textViewRestaurateur.setText(item.getRestaurateur().getShopName());
                holder.textViewCustomerAddress.setText(item.getDeliveryAddress().getFullAddress());
                //TODO Aggiungere cliente
                holder.itemView.setTag(item);
                holder.itemView.setOnClickListener(this.onClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return this.deliveryList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewRestaurateur;
            final TextView textViewCustomerAddress;
            final TextView textViewCustomer;

            public ViewHolder(@NonNull View view) {
                super(view);
                textViewRestaurateur = view.findViewById(R.id.textViewRestaurateur);
                textViewCustomerAddress = view.findViewById(R.id.textViewCustomerAddress);
                textViewCustomer = view.findViewById(R.id.textViewCustomer);
            }
        }
    }
}