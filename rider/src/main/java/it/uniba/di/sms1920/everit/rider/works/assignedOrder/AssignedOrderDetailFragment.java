package it.uniba.di.sms1920.everit.rider.works.assignedOrder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

import it.uniba.di.sms1920.everit.rider.BackgroundLocationService;
import it.uniba.di.sms1920.everit.rider.IBackgroundLocationService;
import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.RiderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class AssignedOrderDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM = "item";
    private static final String SAVED_ASSIGNED_ORDER = "saved.assigned_order";

    private Order assignedOrder;
    private MaterialButton buttonPickup, buttonRefuse;
    private LinearLayout linearLayoutRestaurateurAddress,  linearLayoutRestaurateurPhoneNumber, linearLayoutAddressDeliver, linearLayoutOrderLate;
    private TextView textViewOrderNumber, textViewPickupTime, textViewRestaurateurName, textViewRestaurateurPhone, textViewRestaurateurAddress, textViewDeliverAddress;
    private TextView textViewDeliveryCost;
    private Activity parentActivity;

    private IBackgroundLocationService backgroundLocationService;
    private ServiceConnection backgroundLocationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            backgroundLocationService = IBackgroundLocationService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            backgroundLocationService = null;
        }
    };

    public AssignedOrderDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            if (arguments != null && arguments.containsKey(ARG_ITEM)) {
                this.assignedOrder = arguments.getParcelable(ARG_ITEM);
            }
        }
        else {
            this.assignedOrder = savedInstanceState.getParcelable(SAVED_ASSIGNED_ORDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proposal_detail, container, false);
        this.initUi(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.parentActivity = this.getActivity();
        if (this.parentActivity != null) {
            Intent serviceIntent = new Intent(this.parentActivity, BackgroundLocationService.class);
            this.parentActivity.bindService(serviceIntent, this.backgroundLocationServiceConnection, 0);
        }
        if (this.assignedOrder != null) {
            this.initData();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (this.parentActivity != null) {
            this.parentActivity.unbindService(this.backgroundLocationServiceConnection);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_ASSIGNED_ORDER, this.assignedOrder);
    }

    private void initUi(View view) {
        this.textViewRestaurateurName = view.findViewById(R.id.textViewRestaurateurName);
        this.textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);

        this.linearLayoutRestaurateurAddress = view.findViewById(R.id.linearLayoutRestaurateurAddress);
        this.textViewRestaurateurAddress = view.findViewById(R.id.textViewRestaurateurAddress);

        this.linearLayoutRestaurateurPhoneNumber = view.findViewById(R.id.linearLayoutRestaurateurPhoneNumber);
        this.textViewRestaurateurPhone = view.findViewById(R.id.textViewRestaurateurPhoneNumber);

        this.linearLayoutAddressDeliver = view.findViewById(R.id.linearLayoutAddressDeliver);
        this.textViewDeliverAddress = view.findViewById(R.id.textViewDeliverAddress);

        this.textViewPickupTime = view.findViewById(R.id.textViewPickupTimeProposal);

        this.linearLayoutOrderLate = view.findViewById(R.id.linearLayoutOrderLate);

        this.textViewDeliveryCost = view.findViewById(R.id.textViewDeliveryCost);

        this.buttonPickup = view.findViewById(R.id.buttonAccept);
        this.buttonPickup.setText(R.string.pickup_button);
        this.buttonPickup.setOnClickListener(v -> {
            if (backgroundLocationService != null) {
                try {
                    double latitude = backgroundLocationService.getLastLatitude();
                    double longitude = backgroundLocationService.getLastLongitude();
                    if ((latitude != 0) && (longitude != 0)) {
                        RiderRequest riderRequest = new RiderRequest();
                        riderRequest.pickupOrder(this.assignedOrder.getId(), latitude, longitude, new RequestListener<Boolean>() {
                            @Override
                            public void successResponse(Boolean response) {
                                //TODO controllare
                                Dialog dialog = new Dialog(parentActivity);
                                dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

                                TextView title = dialog.findViewById(R.id.textViewTitle);
                                title.setText(R.string.order_withdrawn);

                                TextView message = dialog.findViewById(R.id.textViewMessage);
                                message.setText(R.string.order_withdrawn_explanation);

                                MaterialButton btnOk = dialog.findViewById(R.id.btnOk);
                                btnOk.setOnClickListener(v1 -> {
                                    dialog.dismiss();
                                    getActivity().finish();
                                });

                                dialog.show();
                                /**
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                                dialogBuilder.setTitle(R.string.order_withdrawn)
                                        .setMessage(R.string.order_withdrawn_explanation)
                                        .setPositiveButton(R.string.ok_default, (dialog, which) -> {
                                            dialog.dismiss();
                                            getActivity().finish();
                                        })
                                        .show();
                                 */
                            }

                            @Override
                            public void errorResponse(RequestException error) {
                                promptErrorMessage(error.getMessage());
                            }
                        });
                    }
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        this.buttonRefuse = view.findViewById(R.id.buttonRefuse);
        this.buttonRefuse.setVisibility(View.GONE);
    }

    private void initData() {
        String remainingTimeString;

        if(assignedOrder.isLate()){
            linearLayoutOrderLate.setVisibility(View.VISIBLE);
            remainingTimeString = String.format("%s (%s)", this.assignedOrder.getPickupTimeAsString(), getString(R.string.pickup_late));
            //remainingTimeString = String.format("%s: %s (%s)", getString(R.string.pickup_at), this.assignedOrder.getPickupTimeAsString(), getString(R.string.pickup_late));
        }else{
            remainingTimeString = this.assignedOrder.getPickupTimeAsString();
           // remainingTimeString = getString(R.string.pickup_at)+": "+this.assignedOrder.getPickupTimeAsString();
        }


        this.textViewRestaurateurName.setText(assignedOrder.getRestaurateur().getShopName());
        this.textViewOrderNumber.setText("#"+assignedOrder.getId());

        this.linearLayoutRestaurateurAddress.setOnClickListener(v -> {
            Utility.showLocationOnMap(getContext(), assignedOrder.getRestaurateur().getAddress().getLatitude(),
                    assignedOrder.getRestaurateur().getAddress().getLongitude(),
                    assignedOrder.getRestaurateur().getShopName());
        });
        this.textViewRestaurateurAddress.setText(assignedOrder.getRestaurateur().getAddress().getFullAddress());

        this.linearLayoutRestaurateurPhoneNumber.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + assignedOrder.getRestaurateur().getPhoneNumber()));
            startActivity(intent);
        });
        this.textViewRestaurateurPhone.setText(assignedOrder.getRestaurateur().getPhoneNumber());

        this.linearLayoutAddressDeliver.setOnClickListener(v -> {
            Utility.showLocationOnMap(getContext(), assignedOrder.getDeliveryAddress().getLatitude(),
                    assignedOrder.getDeliveryAddress().getLongitude(),
                    "");
        });
        this.textViewDeliverAddress.setText(assignedOrder.getDeliveryAddress().getFullAddress());

        this.textViewPickupTime.setText(remainingTimeString);

        this.textViewDeliveryCost.setText(String.format(Locale.getDefault(), "\u20AC %.1f", assignedOrder.getRestaurateur().getDeliveryCost()));
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
        });

        dialog.show();
    }
}