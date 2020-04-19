package it.uniba.di.sms1920.everit.customer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.NotificationService;
import it.uniba.di.sms1920.everit.utils.PreferencesManager;
import it.uniba.di.sms1920.everit.utils.credentials.CredentialsManager;
import it.uniba.di.sms1920.everit.utils.request.AuthProvider;
import it.uniba.di.sms1920.everit.utils.request.RequestManager;

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
            Intent intent = new Intent(LauncherActivity.this, BaseActivity.class);
            startActivity(intent);
            finish();
        }, ((int) DELAY * 1000));
    }

    private void initServices() {
        Context context = getApplicationContext();

        RequestManager.init(context);
        AuthProvider.init(Constants.Variants.CUSTOMER);
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
