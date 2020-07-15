package it.uniba.di.sms1920.everit.rider.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import it.uniba.di.sms1920.everit.rider.BackgroundLocationService;
import it.uniba.di.sms1920.everit.rider.IBackgroundLocationService;
import it.uniba.di.sms1920.everit.rider.R;

public class HomeFragment extends Fragment {
    private Intent locationServiceIntent;
    private ServiceConnection serviceConnection;
    private IBackgroundLocationService backgroundLocationService;

    public HomeFragment() {
        this.locationServiceIntent = new Intent(getContext(), BackgroundLocationService.class);
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
        this.initComponent(viewRoot);
        return viewRoot;
    }

    private void initComponent(View viewRoot) {
        getContext().bindService(this.locationServiceIntent, this.serviceConnection, 0);

        ToggleButton buttonService = viewRoot.findViewById(R.id.toggleButtonLocationService);
    }
}
