package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import it.uniba.di.sms1920.everit.restaurateur.AppLoader;
import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.AuthProvider;
import it.uniba.di.sms1920.everit.utils.provider.NoSuchCredentialException;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
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
            }
            catch (NoSuchCredentialException e) {
                Intent returnLogin= new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(returnLogin);
                finish();
            }
        }
    }
}
