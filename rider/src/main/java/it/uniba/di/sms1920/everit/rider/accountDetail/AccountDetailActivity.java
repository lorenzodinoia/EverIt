package it.uniba.di.sms1920.everit.rider.accountDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.Objects;

import it.uniba.di.sms1920.everit.rider.R;

public class AccountDetailActivity extends AppCompatActivity {

    public static final String ARG_RIDER = "rider";

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        toolbar = findViewById(R.id.toolbar_default);
        toolbar.setTitle(R.string.account_detail);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(savedInstanceState == null) {
            AccountDetailFragment fragment = new AccountDetailFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.containerAccountDetailRider, fragment).addToBackStack(null).commit();
        }

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

}