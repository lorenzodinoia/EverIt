package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.io.IOException;
import java.security.GeneralSecurityException;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.NotificationService;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
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
        Handler handler = new Handler();

        if(Providers.getAuthProvider().getUser() == null){
            try {
                Providers.getAuthProvider().loginFromSavedCredential(new RequestListener<Restaurateur>() {
                    @Override
                    public void successResponse(Restaurateur response) {
                        Intent returnLogin= new Intent(getApplicationContext(), BaseActivity.class);
                        startActivity(returnLogin);
                        finish();
                    }


                    @Override
                    public void errorResponse(RequestException error) {
                        Intent returnLogin= new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(returnLogin);
                        finish();
                    }
                });
            } catch (NoSuchCredentialException e) {
                Intent returnLogin= new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(returnLogin);
                finish();
            }

            /**
            handler.postDelayed(() -> {
                Intent intent = new Intent(LauncherActivity.this, BaseActivity.class);
                startActivity(intent);
                finish();
            }, ((int) DELAY * 1000));
             */
            //TODO Capire se serve
        }
    }

    private void initServices() {
        Context context = getApplicationContext();
        try {
            Providers.init(context, Constants.Variants.RESTAURATEUR);
            NotificationService.initNotificationChannel(getApplicationContext());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MapEngine mapEngine = MapEngine.getInstance(); //HERE SDK MapEngine initialization
        ApplicationContext appContext = new ApplicationContext(context);
        mapEngine.init(appContext, error -> {
            if (error != OnEngineInitListener.Error.NONE) {
                Log.e("HERE-SDK", error.name());
            }
        });

        AndroidThreeTen.init(getApplicationContext());
    }
}
