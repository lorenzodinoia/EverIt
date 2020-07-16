package it.uniba.di.sms1920.everit.rider.activities;

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
import android.widget.Toast;
import android.widget.ToggleButton;

import it.uniba.di.sms1920.everit.rider.BackgroundLocationService;
import it.uniba.di.sms1920.everit.rider.IBackgroundLocationService;
import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.provider.LocationProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;

public class HomeFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private Context context;
    private ServiceConnection serviceConnection;
    private IBackgroundLocationService backgroundLocationService;

    public HomeFragment() {
        this.serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                backgroundLocationService = IBackgroundLocationService.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                backgroundLocationService = null;
            }
        };
    }

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

    private void initComponent(View viewRoot) {
        Intent serviceIntent = new Intent(getContext(), BackgroundLocationService.class);
        this.context.bindService(serviceIntent, this.serviceConnection, 0);

        ToggleButton buttonService = viewRoot.findViewById(R.id.toggleButtonLocationService);
        buttonService.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (LocationProvider.hasPermissions(this.context)) {
                    startLocationService();
                }
                else {
                    LocationProvider.requestPermissions(HomeFragment.this, true);
                }
            }
            else {
                try {
                    if (backgroundLocationService != null) {
                        backgroundLocationService.stop();
                    }
                    else {
                        this.context.stopService(serviceIntent);
                    }
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LocationProvider.REQUEST_PERMISSION_GPS) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                this.startLocationService();
            }
            else {
                if (this.context != null) {
                    Toast.makeText(this.context, this.context.getString(R.string.message_no_gps_permission), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
