package it.uniba.di.sms1920.everit.rider.deliverHistory;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;

import java.util.Objects;

import it.uniba.di.sms1920.everit.rider.R;

public class DeliveryHistoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveryhistory_detail);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();

            arguments.putParcelable(DeliveryHistoryDetailFragment.ARG_ITEM_ID,
                    getIntent().getParcelableExtra(DeliveryHistoryDetailFragment.ARG_ITEM_ID));

            DeliveryHistoryDetailFragment fragment = new DeliveryHistoryDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.deliveryhistory_detail_container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, DeliveryHistoryListActivity.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}