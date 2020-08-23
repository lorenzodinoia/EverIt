package it.uniba.di.sms1920.everit.rider.activities.works.proposal;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Proposal;
import it.uniba.di.sms1920.everit.utils.request.ProposalRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class ProposalDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM = "item";

    private Proposal proposal;
    private MaterialButton buttonAccept;
    private MaterialButton buttonRefuse;
    private LinearLayout linearLayoutRestaurateurAddress,  linearLayoutRestaurateurPhoneNumber, linearLayoutAddressDeliver, linearLayoutPickupTimeProposal;
    private TextView textViewOrderNumber, textViewPickupTime, textViewRestaurateurName, textViewRestaurateurPhone, textViewRestaurateurAddress, textViewDeliverAddress;

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
        this.textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);

        this.linearLayoutRestaurateurAddress = view.findViewById(R.id.linearLayoutRestaurateurAddress);
        this.textViewRestaurateurAddress = view.findViewById(R.id.textViewRestaurateurAddress);

        this.linearLayoutRestaurateurPhoneNumber = view.findViewById(R.id.linearLayoutRestaurateurPhoneNumber);
        this.textViewRestaurateurPhone = view.findViewById(R.id.textViewRestaurateurPhoneNumber);

        this.linearLayoutAddressDeliver = view.findViewById(R.id.linearLayoutAddressDeliver);
        this.textViewDeliverAddress = view.findViewById(R.id.textViewDeliverAddress);

        this.linearLayoutPickupTimeProposal = view.findViewById(R.id.linearLayoutPickupTimeProposal);
        this.textViewPickupTime = view.findViewById(R.id.textViewPickupTimeProposal);

        this.buttonAccept = view.findViewById(R.id.buttonAccept);
        buttonAccept.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_message_y_n);

            TextView title = dialog.findViewById(R.id.textViewTitle);
            title.setText(R.string.proposal_accept_long);
            TextView message = dialog.findViewById(R.id.textViewMessage);
            message.setText(R.string.proposal_accept_explanation);

            MaterialButton positiveButton = dialog.findViewById(R.id.btnOk);
            positiveButton.setOnClickListener(v1 -> {
                acceptProposal();
            });

            MaterialButton negativeButton = dialog.findViewById(R.id.btnCancel);
            negativeButton.setOnClickListener(v1 -> {
                dialog.dismiss();
            });

            dialog.show();
        });
        this.buttonRefuse = view.findViewById(R.id.buttonRefuse);
        buttonRefuse.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_message_y_n);

            TextView title = dialog.findViewById(R.id.textViewTitle);
            title.setText(R.string.proposal_refuse_long);
            TextView message = dialog.findViewById(R.id.textViewMessage);
            message.setText(R.string.proposal_refuse_explanation);

            MaterialButton positiveButton = dialog.findViewById(R.id.btnOk);
            positiveButton.setOnClickListener(v1 -> {
                refuseProposal();
            });

            MaterialButton negativeButton = dialog.findViewById(R.id.btnCancel);
            negativeButton.setOnClickListener(v1 -> {
                dialog.dismiss();
            });

            dialog.show();
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
            remainingTimeString = getString(R.string.pickup_at)+": "+this.proposal.getPickupTimeAsString();
        }
        else {
            remainingTimeString = String.format(Locale.getDefault(), "%s: %s (%s)", getString(R.string.pickup_at), this.proposal.getPickupTimeAsString(), getString(R.string.pickup_late));
        }

        this.textViewRestaurateurName.setText(proposal.getRestaurateur().getShopName());
        this.textViewOrderNumber.setText("#"+proposal.getOrder().getId());

        this.linearLayoutRestaurateurAddress.setOnClickListener(v -> {
            //TODO implementare apertura mappa
        });
        this.textViewRestaurateurAddress.setText(proposal.getRestaurateurAddress());

        this.linearLayoutRestaurateurPhoneNumber.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + proposal.getRestaurateur().getPhoneNumber()));
            startActivity(intent);
        });
        this.textViewRestaurateurPhone.setText(proposal.getRestaurateur().getPhoneNumber());

        this.linearLayoutAddressDeliver.setOnClickListener(v -> {
            //TODO implementare apertura mappa
        });
        this.textViewDeliverAddress.setText(proposal.getDeliveryAddress());


        this.textViewPickupTime.setText(remainingTimeString);

    }

    private void acceptProposal() {
        ProposalRequest proposalRequest = new ProposalRequest();
        proposalRequest.accept(this.proposal.getId(), new RequestListener<Boolean>() {
            @Override
            public void successResponse(Boolean response) {
                if (response) {
                    /*String explanationString = "";
                    //String explanationString = getResources().getQuantityString(R.plurals.proposal_accepted_explanation, proposal.getRemainingTime(), proposal.getRemainingTime());
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    dialogBuilder.setTitle(R.string.proposal_accepted)
                            .setMessage(explanationString)
                            .setPositiveButton(R.string.ok_default, (dialog, which) -> {
                                dialog.dismiss();
                                getActivity().finish();
                            })
                            .show();*/
                    //TODO aggiungere messaggio di feedback
                }
            }

            @Override
            public void errorResponse(RequestException error) {
                promptErrorMessage(error.getMessage());
            }
        });
    }

    private void refuseProposal() {
        ProposalRequest proposalRequest = new ProposalRequest();
        proposalRequest.refuse(this.proposal.getId(), new RequestListener<Boolean>() {
            @Override
            public void successResponse(Boolean response) {
                if (response) {
                    getActivity().finish();
                }
            }

            @Override
            public void errorResponse(RequestException error) {
                promptErrorMessage(error.getMessage());
            }
        });
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