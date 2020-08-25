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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import it.uniba.di.sms1920.everit.rider.activities.BaseActivity;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.request.core.ObjectRequest;

public class BackgroundLocationService extends Service {
    public static final String ACTION_START_WORKING = "service.work";
    public static final String PARAMETER_AUTH_TOKEN = "auth-token";
    private static final String LOCATION_UPDATE_SERVER_URL = String.format("%s/api/rider/location", Constants.SERVER_HOST);
    private static final String START_WORKING_SERVER_URL = String.format("%s/api/rider/start", Constants.SERVER_HOST);
    private static final String STOP_WORKING_SERVER_URL = String.format("%s/api/rider/stop", Constants.SERVER_HOST);
    private static final String REQUEST_BODY_LATITUDE = "latitude";
    private static final String REQUEST_BODY_LONGITUDE = "longitude";
    private static final String CONSOLE_TAG = "LOCATION SERVICE";

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
    private Location lastLocation;
    private String authToken;
    private RequestQueue requestQueue;
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
        this.requestQueue = Volley.newRequestQueue(this);
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

            @Override
            public double getLastLatitude() throws RemoteException {
                Location lastLocation = BackgroundLocationService.this.lastLocation;
                if (lastLocation != null) {
                    return lastLocation.getLatitude();
                }
                else {
                    return 0;
                }
            }

            @Override
            public double getLastLongitude() throws RemoteException {
                Location lastLocation = BackgroundLocationService.this.lastLocation;
                if (lastLocation != null) {
                    return lastLocation.getLongitude();
                }
                else {
                    return 0;
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if ((intent.getAction() != null) && (intent.getAction().equals(ACTION_START_WORKING)) && (intent.hasExtra(PARAMETER_AUTH_TOKEN))) {
            this.authToken = intent.getStringExtra(PARAMETER_AUTH_TOKEN);
            this.startWorking();
            Log.d(CONSOLE_TAG, "Started");
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(CONSOLE_TAG, "Destroyed");
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
        ObjectRequest serverRequest = new ObjectRequest(Request.Method.POST, START_WORKING_SERVER_URL, null,
            response -> Log.d(CONSOLE_TAG, "Rider's service started on the server"),
            error -> {
                Log.e(CONSOLE_TAG, "UUnable to start rider's service on the server: " + error.toString());
                this.working = false;
                this.stop();
            }, this.authToken);
        this.requestQueue.add(serverRequest);
    }

    private void stopWorking() {
        this.stopLocationUpdates();
        if (this.working) {
            this.working = false;
            ObjectRequest serverRequest = new ObjectRequest(Request.Method.POST, STOP_WORKING_SERVER_URL, null,
                    response -> Log.d(CONSOLE_TAG, "Rider's service stopped on the server"),
                    error -> Log.e(CONSOLE_TAG, "Unable to stop rider's service on the server: " + error.toString()), this.authToken);
            this.requestQueue.add(serverRequest);
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        this.fusedLocationProviderClient.requestLocationUpdates(locationRequestSettings, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        this.fusedLocationProviderClient.removeLocationUpdates(this.locationCallback);
    }

    private void onNewLocation(Location location) {
        String logString = String.format(Locale.getDefault(), "%f, %f", location.getLatitude(), location.getLongitude());
        Log.d(CONSOLE_TAG, logString);
        this.lastLocation = location;
        this.uploadNewLocation(location);
    }

    private void uploadNewLocation(Location location) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(REQUEST_BODY_LATITUDE, location.getLatitude());
            requestBody.put(REQUEST_BODY_LONGITUDE, location.getLongitude());
        }
        catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        ObjectRequest serverRequest = new ObjectRequest(Request.Method.POST, LOCATION_UPDATE_SERVER_URL, requestBody,
                response -> Log.d(CONSOLE_TAG, "Location pushed successfully"),
                error -> Log.e(CONSOLE_TAG, "There was an error on pushing new location: " + error.toString()), this.authToken);
        this.requestQueue.add(serverRequest);
    }

    public void stop() {
        super.stopSelf();
    }
}
