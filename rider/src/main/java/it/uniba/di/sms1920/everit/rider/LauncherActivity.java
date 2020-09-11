package it.uniba.di.sms1920.everit.rider;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import it.uniba.di.sms1920.everit.utils.models.Rider;
import it.uniba.di.sms1920.everit.utils.provider.AuthProvider;
import it.uniba.di.sms1920.everit.utils.provider.NoSuchCredentialException;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class LauncherActivity extends AppCompatActivity {
    public static final String FLAG_ONLY_INITIALIZATIONS = "launcher.only_initializations";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            AppLoader.loadComponents(getApplicationContext());
        }
        catch (Exception e) {
            e.printStackTrace();
            this.finish();
        }

        if (Providers.getAuthProvider().getUser() == null) {
            try {
                AuthProvider<Rider> authProvider = Providers.getAuthProvider();
                authProvider.loginFromSavedCredential(new RequestListener<Rider>() {
                    @Override
                    public void successResponse(Rider response) {
                        if (!getIntent().hasExtra(FLAG_ONLY_INITIALIZATIONS)) {
                            Intent loadBaseActivity = new Intent(getApplicationContext(), BaseActivity.class);
                            startActivity(loadBaseActivity);
                        }
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
}
