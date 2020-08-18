package it.uniba.di.sms1920.everit.rider.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import it.uniba.di.sms1920.everit.rider.BackgroundLocationService;
import it.uniba.di.sms1920.everit.rider.IBackgroundLocationService;
import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.RiderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AssignedOrderDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM = "item";

    private Order assignedOrder;
    private TextView textViewPickupTime;
    private TextView textViewRestaurateurName;
    private TextView textViewRestaurateurPhone;
    private TextView textViewRestaurateurAddress;
    private TextView textViewOrderNumber;
    private TextView textViewCustomerAddress;
    private ImageView imageViewRestaurateurLogo;
    private MaterialButton buttonPickupOrder;

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
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(ARG_ITEM)) {
                this.assignedOrder = arguments.getParcelable(ARG_ITEM);
            }
            else if (arguments.containsKey(ARG_ITEM_ID)) {
                long assignedOrderId = arguments.getLong(ARG_ITEM_ID);
                RiderRequest riderRequest = new RiderRequest();
                riderRequest.readAssignedOrder(assignedOrderId, new RequestListener<Order>() {
                    @Override
                    public void successResponse(Order response) {
                        assignedOrder = response;
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
    public void onStart() {
        super.onStart();
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, BackgroundLocationService.class);
            activity.bindService(intent, this.backgroundLocationServiceConnection, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assigned_order_detail, container, false);

        this.textViewRestaurateurName = view.findViewById(R.id.textViewRestaurateurName);
        this.textViewRestaurateurPhone = view.findViewById(R.id.textViewRestaurateurPhone);
        this.textViewRestaurateurAddress = view.findViewById(R.id.textViewRestaurateurAddress);
        this.textViewPickupTime = view.findViewById(R.id.textViewPickupTime);
        this.textViewOrderNumber = view.findViewById(R.id.textViewOrderNumber);
        this.textViewCustomerAddress = view.findViewById(R.id.textViewCustomerAddress);
        this.imageViewRestaurateurLogo = view.findViewById(R.id.imageViewRestaurateurLogo);
        this.buttonPickupOrder = view.findViewById(R.id.buttonPickupOrder);
        this.buttonPickupOrder.setOnClickListener(v -> {
            if (backgroundLocationService != null) {
                try {
                    double latitude = backgroundLocationService.getLastLatitude();
                    double longitude = backgroundLocationService.getLastLongitude();
                    if ((latitude != 0) && (longitude != 0)) {
                        RiderRequest riderRequest = new RiderRequest();
                        riderRequest.pickupOrder(this.assignedOrder.getId(), latitude, longitude, new RequestListener<Boolean>() {
                            @Override
                            public void successResponse(Boolean response) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                                dialogBuilder.setTitle(R.string.order_withdrawn)
                                        .setMessage(R.string.order_withdrawn_explanation)
                                        .setPositiveButton(R.string.ok_default, (dialog, which) -> {
                                            dialog.dismiss();
                                            getActivity().finish();
                                        })
                                        .show();
                            }

                            @Override
                            public void errorResponse(RequestException error) {
                                Utility.showGenericMessage(getContext(), getString(R.string.message_generic_error), error.getMessage());
                            }
                        });
                    }
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        if (this.assignedOrder != null) {
            this.initComponents();
        }

        return view;
    }

    private void initComponents() {
        int remainingTime = this.assignedOrder.getRemainingTime();
        String remainingTimeString;

        if (remainingTime >= 0) {
            remainingTimeString = getResources().getQuantityString(R.plurals.pickup_minutes, remainingTime, remainingTime);
        }
        else {
            remainingTimeString = String.format(Locale.getDefault(), "%s (%s)", this.assignedOrder.getPickupTimeAsString(), getString(R.string.pickup_late));
        }

        this.textViewRestaurateurName.setText(this.assignedOrder.getRestaurateur().getShopName());
        this.textViewRestaurateurPhone.setText(this.assignedOrder.getRestaurateur().getPhoneNumber());
        this.textViewRestaurateurAddress.setText(this.assignedOrder.getRestaurateur().getAddress().getFullAddress());
        this.textViewPickupTime.setText(remainingTimeString);
        this.textViewOrderNumber.setText(String.format(Locale.getDefault(), "%s %d", getContext().getString(R.string.label_order_number), this.assignedOrder.getId()));
        this.textViewCustomerAddress.setText(this.assignedOrder.getDeliveryAddress().getFullAddress());

        String imageUrl = String.format("%s/images/%s", Constants.SERVER_HOST, this.assignedOrder.getRestaurateur().getImagePath());
        Picasso.get()
                .load(imageUrl)
                .error(R.mipmap.icon)
                .placeholder(R.mipmap.icon)
                .transform(new CropCircleTransformation())
                .fit()
                .into(this.imageViewRestaurateurLogo);
    }
}