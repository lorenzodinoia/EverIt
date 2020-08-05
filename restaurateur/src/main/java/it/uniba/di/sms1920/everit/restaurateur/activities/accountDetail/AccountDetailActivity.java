package it.uniba.di.sms1920.everit.restaurateur.activities.accountDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

import java.util.Objects;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class AccountDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    Restaurateur restaurateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        toolbar = findViewById(R.id.toolbar_default);
        toolbar.setTitle(R.string.account_detail);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
        restaurateurRequest.getCurrentUser(new RequestListener() {
            @Override
            public void successResponse(Object response) {
                restaurateur = (Restaurateur) response;
                AccountDetailFragment fragment = new AccountDetailFragment();
                FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
                fragmentManager.add(R.id.containerSettings, fragment).addToBackStack(null).commit();
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO gestire errorResponse
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            FragmentManager fm = getSupportFragmentManager();
            if(fm.getBackStackEntryCount() > 1){
                fm.popBackStack();
            }
            else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public Restaurateur getRestaurateur(){
        return restaurateur;
    }

}