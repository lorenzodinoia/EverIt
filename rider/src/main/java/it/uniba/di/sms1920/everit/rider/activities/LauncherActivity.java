package it.uniba.di.sms1920.everit.rider.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.threetenabp.AndroidThreeTen;

import java.io.IOException;
import java.security.GeneralSecurityException;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.NotificationService;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.Rider;
import it.uniba.di.sms1920.everit.utils.provider.NoSuchCredentialException;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.AccessRequest;
import it.uniba.di.sms1920.everit.utils.provider.RequestProvider;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

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

        AndroidThreeTen.init(getApplicationContext());

        if (Providers.getAuthProvider().getUser() == null){
            try {
                Providers.getAuthProvider().loginFromSavedCredential(new RequestListener<Rider>() {
                    @Override
                    public void successResponse(Rider response) {
                        Intent goToBaseActivity = new Intent(getApplicationContext(), BaseActivity.class);
                        startActivity(goToBaseActivity);
                        finish();
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        Intent returnToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(returnToLogin);
                        finish();
                    }
                });
            }
            catch (NoSuchCredentialException e) {
                Intent returnToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(returnToLogin);
                finish();
            }
        }
    }

    private void initServices() {
        Context context = getApplicationContext();
        try {
            Providers.init(context, Constants.Variants.RIDER);
            NotificationService.initNotificationChannel(context);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
