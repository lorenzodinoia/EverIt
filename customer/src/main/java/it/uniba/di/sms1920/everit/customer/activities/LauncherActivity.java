package it.uniba.di.sms1920.everit.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import it.uniba.di.sms1920.everit.customer.AppLoader;
import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.provider.AuthProvider;
import it.uniba.di.sms1920.everit.utils.provider.NoSuchCredentialException;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class LauncherActivity extends AppCompatActivity {
    public static final String FLAG_ONLY_INITIALIZATIONS = "launcher.only_initializations";
    public static final String FLAG_REQUIRE_LOGIN = "launcher.require_login";
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

        if (Providers.getAuthProvider().getUser() == null) {
            AuthProvider<Customer> authProvider = Providers.getAuthProvider();
            try {
                authProvider.loginFromSavedCredential(new RequestListener<Customer>() {
                    @Override
                    public void successResponse(Customer response) {
                        if (!getIntent().hasExtra(FLAG_ONLY_INITIALIZATIONS)) {
                            Intent intent = new Intent(LauncherActivity.this, BaseActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        if (getIntent().hasExtra(FLAG_REQUIRE_LOGIN)) {
                            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(loginIntent);
                            finish();
                        }
                        else {
                            Intent intent = new Intent(LauncherActivity.this, BaseActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
            catch (NoSuchCredentialException e) {
                if (getIntent().hasExtra(FLAG_REQUIRE_LOGIN)) {
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginIntent);
                }
                finish();
            }
        }
    }
}
