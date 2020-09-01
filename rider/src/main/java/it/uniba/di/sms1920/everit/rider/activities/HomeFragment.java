package it.uniba.di.sms1920.everit.rider.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Arrays;

import it.uniba.di.sms1920.everit.rider.BackgroundLocationService;
import it.uniba.di.sms1920.everit.rider.IBackgroundLocationService;
import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.provider.LocationProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.RiderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class HomeFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private Context context;
    private IBackgroundLocationService backgroundLocationService;
    private ToggleButton buttonService;
    private boolean shouldShowExplanationForLocation = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            backgroundLocationService = IBackgroundLocationService.Stub.asInterface(service);
            if ((buttonService != null) && (backgroundLocationService != null)) {
                checkServiceButton();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            backgroundLocationService = null;
            if (buttonService != null) {
                uncheckServiceButton();
            }
        }
    };
    private final CompoundButton.OnCheckedChangeListener buttonServiceCheckChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (hasPermissions()) {
                    startLocationService();
                }
                else if (shouldShowExplanationForLocation) {
                    showExplanationForLocation();
                }
                else {
                    requestPermissions();
                }
            }
            else {
                RiderRequest riderRequest = new RiderRequest();
                riderRequest.canStopService(new RequestListener<Boolean>() {
                    @Override
                    public void successResponse(Boolean response) {
                        if (response) {
                            try {
                                if (backgroundLocationService != null) {
                                    backgroundLocationService.stop();
                                }
                                else {
                                    Intent serviceIntent = new Intent(getContext(), BackgroundLocationService.class);
                                    context.stopService(serviceIntent);
                                }
                            }
                            catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            Toast.makeText(getContext(), "Hai dei lavori in sospeso", Toast.LENGTH_LONG).show(); //TODO Sostituire con messaggio appropriato
                            checkServiceButton();
                        }
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        Context context = getContext();
                        if (context != null) {
                            Utility.showGenericMessage(context, error.getMessage());
                        }
                    }
                });
            }
        }
    };

    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_home, container, false);
        this.context = getContext();
        if (this.context != null) {
            this.initComponent(viewRoot);
        }
        return viewRoot;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(buttonService.isChecked()){
            Intent serviceIntent = new Intent(context, BackgroundLocationService.class);
            context.bindService(serviceIntent, this.serviceConnection, 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (context != null) {
            context.unbindService(this.serviceConnection);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            if (requestCode == LocationProvider.REQUEST_PERMISSION_BACKGROUND_GPS) {
                int permissionIndex = Arrays.asList(permissions).indexOf(LocationProvider.PERMISSION_BACKGROUND_GPS);
                if ((permissionIndex != -1) && (grantResults.length > 0) && (grantResults[permissionIndex] == PackageManager.PERMISSION_GRANTED)) {
                    this.startLocationService();
                }
                else {
                    Toast.makeText(this.context, this.context.getString(R.string.message_no_gps_permission), Toast.LENGTH_LONG).show();
                    uncheckServiceButton();
                }
            }
        }
        else {
            if (requestCode == LocationProvider.REQUEST_PERMISSION_GPS) {
                int permissionIndex = Arrays.asList(permissions).indexOf(LocationProvider.PERMISSION_GPS);
                if ((permissionIndex != -1) && (grantResults.length > 0) && (grantResults[permissionIndex] == PackageManager.PERMISSION_GRANTED)) {
                    this.startLocationService();
                }
                else {
                    Toast.makeText(this.context, this.context.getString(R.string.message_no_gps_permission), Toast.LENGTH_LONG).show();
                    uncheckServiceButton();
                }
            }
        }
    }

    private void checkServiceButton() {
        buttonService.setOnCheckedChangeListener(null);
        buttonService.setChecked(true);
        buttonService.setOnCheckedChangeListener(buttonServiceCheckChangedListener);
    }

    private void uncheckServiceButton() {
        buttonService.setOnCheckedChangeListener(null);
        buttonService.setChecked(false);
        buttonService.setOnCheckedChangeListener(buttonServiceCheckChangedListener);
    }

    private void initComponent(View viewRoot) {
        this.buttonService = viewRoot.findViewById(R.id.toggleButtonLocationService);
        this.buttonService.setOnCheckedChangeListener(this.buttonServiceCheckChangedListener);

        Intent serviceIntent = new Intent(getContext(), BackgroundLocationService.class);
        this.context.bindService(serviceIntent, this.serviceConnection, 0);
    }

    private boolean hasPermissions() {
        boolean hasPermissions;
        LocationProvider.PermissionStatus status;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            status = LocationProvider.hasBackgroundPermissions(this);
        }
        else {
            status = LocationProvider.hasPermissions(this);
        }

        hasPermissions = (status == LocationProvider.PermissionStatus.GRANTED);
        this.shouldShowExplanationForLocation = (status == LocationProvider.PermissionStatus.NOT_GRANTED_SHOW_EXPLANATION);

        return hasPermissions;
    }

    private void requestPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            LocationProvider.requestBackgroundPermissions(HomeFragment.this);
        }
        else {
            LocationProvider.requestPermissions(HomeFragment.this);
        }
    }

    private void showExplanationForLocation() {
        Context context = this.getContext();
        if (context != null) {
            new AlertDialog.Builder(context)
                    .setMessage(this.getString(R.string.gps_permission_explanation))
                    .setPositiveButton(this.getString(R.string.ok_default), (dialog, which) -> this.requestPermissions())
                    .setNegativeButton(this.getString(R.string.cancel_default), (dialog, which) -> {
                        dialog.dismiss();
                        Toast.makeText(this.context, this.context.getString(R.string.gps_permission_explanation), Toast.LENGTH_LONG).show();
                    })
                    .create()
                    .show();
        }
    }

    private void startLocationService() {
        if (Providers.getAuthProvider() != null) {
            String authToken = Providers.getAuthProvider().getAuthToken();
            Intent serviceIntent = new Intent(this.context, BackgroundLocationService.class);
            serviceIntent.setAction(BackgroundLocationService.ACTION_START_WORKING);
            serviceIntent.putExtra(BackgroundLocationService.PARAMETER_AUTH_TOKEN, authToken);

            this.context.startService(serviceIntent);
        }
    }
}
