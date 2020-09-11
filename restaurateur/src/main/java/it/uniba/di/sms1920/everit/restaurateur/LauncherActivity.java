package it.uniba.di.sms1920.everit.restaurateur;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.AuthProvider;
import it.uniba.di.sms1920.everit.utils.provider.NoSuchCredentialException;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class LauncherActivity extends AppCompatActivity {
    public static final String FLAG_ONLY_INITIALIZATIONS = "launcher.only_initializations";
    private static final float DELAY = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            AppLoader.loadComponents(this.getApplicationContext());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        AuthProvider<Restaurateur> authProvider = Providers.getAuthProvider();
        if(authProvider.getUser() == null) {
            try {
                authProvider.loginFromSavedCredential(new RequestListener<Restaurateur>() {
                    @Override
                    public void successResponse(Restaurateur response) {
                        if (!getIntent().hasExtra(FLAG_ONLY_INITIALIZATIONS)) {
                            Intent loadBaseActivity= new Intent(getApplicationContext(), BaseActivity.class);
                            startActivity(loadBaseActivity);
                        }
                        finish();
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        Intent returnLogin= new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(returnLogin);
                        finish();
                    }
                });
            }
            catch (NoSuchCredentialException e) {
                Intent returnLogin= new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(returnLogin);
                finish();
            }
        }
    }
}
