package it.uniba.di.sms1920.everit.rider;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class BackgroundLocationService extends Service {
    private static final long LOCATION_UPDATES_INTERVAL = 2000;
    private static final int LOCATION_UPDATES_PRIORITY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
    
    private static LocationRequest locationRequestSettings;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    public BackgroundLocationService() {
        locationRequestSettings = LocationRequest.create();
        locationRequestSettings.setInterval(LOCATION_UPDATES_INTERVAL);
        locationRequestSettings.setPriority(LOCATION_UPDATES_PRIORITY);
    }

    @Override
    public void onCreate() {
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        this.setLocationCallback();
        this.startLocationUpdates();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void setLocationCallback() {
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    onNewLocation(locationResult.getLastLocation());
                }
            }
        };
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        this.fusedLocationProviderClient.requestLocationUpdates(locationRequestSettings, locationCallback, Looper.getMainLooper());
    }

    private void onNewLocation(Location location) {
        Log.d("POSIZIONE", location.toString());
    }
}
