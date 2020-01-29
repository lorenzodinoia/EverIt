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
        //TODO Inserire logica per scegliere activity da avviare
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
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
