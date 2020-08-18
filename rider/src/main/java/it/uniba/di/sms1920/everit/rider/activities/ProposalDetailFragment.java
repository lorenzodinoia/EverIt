package it.uniba.di.sms1920.everit.rider.activities;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Proposal;
import it.uniba.di.sms1920.everit.utils.request.ProposalRequest;
import it.uniba.di.sms1920.everit.utils.request.RiderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ProposalDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM = "item";

    private Proposal proposal;
    private MaterialButton buttonAccept;
    private MaterialButton buttonRefuse;
    private TextView textViewPickupTime;
    private TextView textViewRestaurateurName;
    private TextView textViewRestaurateurPhone;
    private TextView textViewRestaurateurAddress;
    private TextView textViewCustomerAddress;
    private ImageView imageViewRestaurateurLogo;

    public ProposalDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(ARG_ITEM)) {
                this.proposal = arguments.getParcelable(ARG_ITEM);
            }
            else if (arguments.containsKey(ARG_ITEM_ID)) {
                long id = getArguments().getLong(ARG_ITEM_ID);
                ProposalRequest proposalRequest = new ProposalRequest();
                proposalRequest.read(id, new RequestListener<Proposal>() {
                    @Override
                    public void successResponse(Proposal response) {
                        proposal = response;
                        initComponents();
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        Utility.showGenericMessage(getContext(), getString(R.string.message_generic_error), error.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proposal_detail, container, false);

        this.textViewRestaurateurName = view.findViewById(R.id.textViewRestaurateurName);
        this.textViewRestaurateurPhone = view.findViewById(R.id.textViewRestaurateurPhone);
        this.textViewRestaurateurAddress = view.findViewById(R.id.textViewRestaurateurAddress);
        this.textViewPickupTime = view.findViewById(R.id.textViewPickupTime);
        this.textViewCustomerAddress = view.findViewById(R.id.textViewCustomerAddress);
        this.imageViewRestaurateurLogo = view.findViewById(R.id.imageViewRestaurateurLogo);
        this.buttonAccept = view.findViewById(R.id.buttonAccept);
        buttonAccept.setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            dialogBuilder.setTitle(R.string.proposal_accept_long)
                    .setMessage(R.string.proposal_accept_explanation)
                    .setPositiveButton(R.string.proposal_accept_long, (dialog, which) -> this.acceptProposal())
                    .setNegativeButton(R.string.cancel_default, (dialog, which) -> dialog.dismiss())
                    .show();
        });
        this.buttonRefuse = view.findViewById(R.id.buttonRefuse);
        buttonRefuse.setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            dialogBuilder.setTitle(R.string.proposal_refuse_long)
                    .setMessage(R.string.proposal_refuse_explanation)
                    .setPositiveButton(R.string.proposal_refuse_long, (dialog, which) -> this.refuseProposal())
                    .setNegativeButton(R.string.cancel_default, (dialog, which) -> dialog.dismiss())
                    .show();
        });

        if (this.proposal != null) {
            this.initComponents();
        }

        return view;
    }

    private void initComponents() {
        int remainingTime = this.proposal.getRemainingTime();
        String remainingTimeString;

        if (remainingTime >= 0) {
            remainingTimeString = getResources().getQuantityString(R.plurals.pickup_minutes, remainingTime, remainingTime);
        }
        else {
            remainingTimeString = String.format(Locale.getDefault(), "%s (%s)", this.proposal.getPickupTimeAsString(), getString(R.string.pickup_late));
        }

        this.textViewRestaurateurName.setText(proposal.getRestaurateur().getShopName());
        this.textViewRestaurateurPhone.setText(proposal.getRestaurateur().getPhoneNumber());
        this.textViewRestaurateurAddress.setText(proposal.getRestaurateurAddress());
        this.textViewPickupTime.setText(remainingTimeString);
        this.textViewCustomerAddress.setText(proposal.getDeliveryAddress());

        String imageUrl = String.format("%s/images/%s", Constants.SERVER_HOST, proposal.getRestaurateur().getImagePath());
        Picasso.get()
                .load(imageUrl)
                .error(R.mipmap.icon)
                .placeholder(R.mipmap.icon)
                .transform(new CropCircleTransformation())
                .fit()
                .into(this.imageViewRestaurateurLogo);
    }

    private void acceptProposal() {
        ProposalRequest proposalRequest = new ProposalRequest();
        proposalRequest.accept(this.proposal.getId(), new RequestListener<Boolean>() {
            @Override
            public void successResponse(Boolean response) {
                if (response) {
                    String explanationString = getResources().getQuantityString(R.plurals.proposal_accepted_explanation, proposal.getRemainingTime(), proposal.getRemainingTime());
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    dialogBuilder.setTitle(R.string.proposal_accepted)
                            .setMessage(explanationString)
                            .setPositiveButton(R.string.ok_default, (dialog, which) -> {
                                dialog.dismiss();
                                getActivity().finish();
                            })
                            .show();
                }
            }

            @Override
            public void errorResponse(RequestException error) {
                Utility.showGenericMessage(getContext(), getString(R.string.message_generic_error), error.getMessage());
            }
        });
    }

    private void refuseProposal() {
        ProposalRequest proposalRequest = new ProposalRequest();
        proposalRequest.refuse(this.proposal.getId(), new RequestListener<Boolean>() {
            @Override
            public void successResponse(Boolean response) {
                if (response) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    dialogBuilder.setTitle(R.string.proposal_refused)
                            .setPositiveButton(R.string.ok_default, (dialog, which) -> {
                                dialog.dismiss();
                                getActivity().finish();
                            })
                            .show();
                }
            }

            @Override
            public void errorResponse(RequestException error) {
                Utility.showGenericMessage(getContext(), getString(R.string.message_generic_error), error.getMessage());
            }
        });
    }
}