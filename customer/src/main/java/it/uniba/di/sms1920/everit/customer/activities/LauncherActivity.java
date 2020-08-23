package it.uniba.di.sms1920.everit.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import it.uniba.di.sms1920.everit.customer.AppLoader;
import it.uniba.di.sms1920.everit.customer.R;

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
        try
        {
            AppLoader.loadComponents(this.getApplicationContext());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(LauncherActivity.this, BaseActivity.class);
            startActivity(intent);
            finish();
        }, ((int) DELAY * 1000));
    }
}
