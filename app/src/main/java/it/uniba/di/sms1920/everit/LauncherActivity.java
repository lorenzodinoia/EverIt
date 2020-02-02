package it.uniba.di.sms1920.everit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.security.GeneralSecurityException;

import it.uniba.di.sms1920.everit.credentials.CredentialsManager;
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
            if (BuildConfig.FLAVOR.equals(Constants.Flavors.CUSTOMER)) {
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
        try {
            CredentialsManager.init(getApplicationContext());
        }
        catch (GeneralSecurityException | IOException e) {
            Log.e("CREDENTIALS", "Unable to initialize CredentialsManager class");
        }
    }
}
