package it.uniba.di.sms1920.everit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.iid.FirebaseInstanceId;

import it.uniba.di.sms1920.everit.request.AuthProvider;
import it.uniba.di.sms1920.everit.request.RequestManager;

public class LauncherActivity extends AppCompatActivity {
    private static final float DELAY = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.initServices();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Class destination = LoginActivity.class;
            if (BuildConfig.FLAVOR.equals(Constants.FLAVOR_CUSTOMER)) {
                try {
                    //destination = Class.forName("it.uniba.di.sms1920.everit.customer.HomeActivity");
                    destination = Class.forName("it.uniba.di.sms1920.everit.masterDetailOrders.OrderListActivity");
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent(LauncherActivity.this, destination);
            startActivity(intent);
            finish();
        }, ((int) DELAY * 1000));
    }

    private void initServices() {
        Context context = getApplicationContext();

        RequestManager.init(context);
        AuthProvider.init();
        NotificationService.initNotificationChannel(context);
        PreferencesManager.init(getApplicationContext());

        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        String newAppToken = FirebaseInstanceId.getInstance().getToken();
        String savedAppToken = preferencesManager.loadAppToken();
        if (savedAppToken == null) {
            PreferencesManager.getInstance().saveAppToken(newAppToken);
            //TODO Inviare token al server
        }
        else if (!savedAppToken.equals(newAppToken)) {
            PreferencesManager.getInstance().saveAppToken(newAppToken);
            //TODO Inviare nuovo token al server
        }
    }
}
