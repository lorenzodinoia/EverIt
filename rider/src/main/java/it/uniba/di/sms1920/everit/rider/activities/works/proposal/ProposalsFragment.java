package it.uniba.di.sms1920.everit.rider.activities.works.proposal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.DataBinder;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Proposal;
import it.uniba.di.sms1920.everit.utils.request.ProposalRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ProposalsFragment extends Fragment implements DataBinder {
    private RecyclerView proposalRecyclerView;
    private ProposalRecyclerViewAdapter proposalRecyclerViewAdapter;
    private List<Proposal> proposalList = new ArrayList<>();
    private TextView textViewEmpty;

    public ProposalsFragment() {
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
        this.proposalRecyclerView = view.findViewById(R.id.proposal_list);
        this.textViewEmpty = view.findViewById(R.id.textViewEmpty);
    }

    private void setupRecyclerView() {
        this.proposalRecyclerViewAdapter = new ProposalRecyclerViewAdapter(this, this.proposalList, false);
        this.proposalRecyclerView.setAdapter(this.proposalRecyclerViewAdapter);
    }

    @Override
    public void refreshData() {
        ProposalRequest proposalRequest = new ProposalRequest();
        proposalRequest.readAll(new RequestListener<Collection<Proposal>>() {
            @Override
            public void successResponse(Collection<Proposal> response) {
                proposalList = new ArrayList<>(response);
                if (proposalRecyclerViewAdapter == null) {
                    setupRecyclerView();
                }
                else {
                    proposalRecyclerViewAdapter.notifyDataSetChanged();
                }
                if (proposalList.size() > 0) {
                    textViewEmpty.setVisibility(View.INVISIBLE);
                }
                else {
                    textViewEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void errorResponse(RequestException error) {
                textViewEmpty.setVisibility(View.VISIBLE);
                Utility.showGenericMessage(getContext(), getString(R.string.message_generic_error), error.getMessage());
            }
        });
    }

    public static class ProposalRecyclerViewAdapter extends RecyclerView.Adapter<ProposalRecyclerViewAdapter.ViewHolder> {
        private final ProposalsFragment parentFragment;
        private final List<Proposal> proposalList;
        private final boolean twoPaneMode;
        private final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Proposal item = (Proposal) view.getTag();
                if (twoPaneMode) {
                    Bundle arguments = new Bundle();
                    arguments.putLong(ProposalDetailFragment.ARG_ITEM_ID, item.getId());
                    ProposalDetailFragment fragment = new ProposalDetailFragment();
                    fragment.setArguments(arguments);
                    parentFragment.getChildFragmentManager().beginTransaction().replace(R.id.proposal_detail_container, fragment).commit();
                }
                else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ProposalDetailActivity.class);
                    intent.putExtra(ProposalDetailActivity.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        ProposalRecyclerViewAdapter(ProposalsFragment parentFragment, List<Proposal> proposalList, boolean twoPaneMode) {
            this.parentFragment = parentFragment;
            this.proposalList = proposalList;
            this.twoPaneMode = twoPaneMode;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposal_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Proposal item = this.proposalList.get(position);
            if (item != null) {
                holder.textViewOrderNumber.setText("#"+item.getId());
                if(item.getRestaurateur().getImagePath() != null){
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
                holder.textViewPickupAddress.setText(item.getRestaurateurAddress());
                holder.textViewDeliveryAddress.setText(item.getDeliveryAddress());
                holder.itemView.setTag(item);
                holder.itemView.setOnClickListener(this.onClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return this.proposalList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewOrderNumber;
            final ImageView imageView;
            final TextView textViewRestaurateur;
            final TextView textViewPickupAddress;
            final TextView textViewDeliveryAddress;

            public ViewHolder(@NonNull View view) {
                super(view);
                textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
                imageView = view.findViewById(R.id.imageViewRestaurantLogo);
                textViewRestaurateur = view.findViewById(R.id.textViewRestaurateur);
                textViewDeliveryAddress = view.findViewById(R.id.textViewAddressToDeliver);
                textViewPickupAddress = view.findViewById(R.id.textViewAddressToPickup);
            }
        }
    }
}