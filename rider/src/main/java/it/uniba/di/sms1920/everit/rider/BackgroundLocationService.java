package it.uniba.di.sms1920.everit.rider;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import it.uniba.di.sms1920.everit.rider.activities.BaseActivity;

public class BackgroundLocationService extends Service {
    public static final String ACTION_START_WORKING = "service.work";

    private static final class LocationUpdatesSettings {
        private static final long INTERVAL = 10000;
        private static final int PRIORITY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
        private static final float MIN_DISPLACEMENT = 50f;
    }

    private static final class NotificationSettings {
        private static final String CHANNEL_NAME = "Location updates service";
        private static final String CHANNEL_ID = "Location-Service";
        private static final int ID = 673;
    }

    private static LocationRequest locationRequestSettings;

    private boolean working;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
                onNewLocation(locationResult.getLastLocation());
            }
        }
    };

    public BackgroundLocationService() {
        locationRequestSettings = LocationRequest.create();
        locationRequestSettings.setInterval(LocationUpdatesSettings.INTERVAL);
        locationRequestSettings.setFastestInterval(LocationUpdatesSettings.INTERVAL);
        locationRequestSettings.setSmallestDisplacement(LocationUpdatesSettings.MIN_DISPLACEMENT);
        locationRequestSettings.setPriority(LocationUpdatesSettings.PRIORITY);
    }

    @Override
    public void onCreate() {
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new IBackgroundLocationService.Stub() {
            @Override
            public boolean isWorking() throws RemoteException {
                return BackgroundLocationService.this.working;
            }

            @Override
            public void stop() throws RemoteException {
                BackgroundLocationService.this.stop();
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if ((intent.getAction() != null) && (intent.getAction().equals(ACTION_START_WORKING))) {
            this.startWorking();
            Log.d("SERVIZIO", "Avviato");
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("SERVIZIO", "Distrutto");
        this.stopWorking();
        super.onDestroy();
    }

    private void initNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final String channelName = NotificationSettings.CHANNEL_NAME;
            final int channelLevel = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NotificationSettings.CHANNEL_ID, channelName, channelLevel);
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private Notification createNotification() {
        this.initNotificationChannel();

        Intent notificationIntent = new Intent(getApplicationContext(), BaseActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, NotificationSettings.CHANNEL_ID)
                .setContentTitle(getApplicationContext().getString(R.string.notification_location_title))
                .setContentText(getApplicationContext().getString(R.string.notification_location_body))
                .setSmallIcon(R.mipmap.icon_round)
                .setContentIntent(pendingIntent)
                .build();
        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;

        return notification;
    }

    private void startWorking() {
        this.startLocationUpdates();
        Notification notification = this.createNotification();
        this.working = true;
        super.startForeground(NotificationSettings.ID, notification);
    }

    private void stopWorking() {
        this.stopLocationUpdates();
        this.working = false;
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        this.fusedLocationProviderClient.requestLocationUpdates(locationRequestSettings, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        this.fusedLocationProviderClient.removeLocationUpdates(this.locationCallback);
    }

    private void onNewLocation(Location location) {
        Log.d("SERVIZIO", location.toString());
    }

    public void stop() {
        super.stopSelf();
    }
}
