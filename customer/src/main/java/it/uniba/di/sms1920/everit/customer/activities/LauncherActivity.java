package it.uniba.di.sms1920.everit.customer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;

import java.io.FileNotFoundException;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.results.ResultDetailActivity;
import it.uniba.di.sms1920.everit.customer.cart.Cart;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.provider.Providers;

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
        try {
            Providers.init(context, Constants.Variants.CUSTOMER);
            Cart.init(context);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MapEngine mapEngine = MapEngine.getInstance(); //HERE SDK MapEngine initialization
        ApplicationContext appContext = new ApplicationContext(context);
        mapEngine.init(appContext, error -> {
            if (error != OnEngineInitListener.Error.NONE) {
                Log.e("HERE-SDK", error.getDetails());
            }
        });
    }
}
