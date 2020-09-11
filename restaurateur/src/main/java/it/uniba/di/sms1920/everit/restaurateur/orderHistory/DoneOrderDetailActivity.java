package it.uniba.di.sms1920.everit.restaurateur.orderHistory;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;

import java.util.Objects;

import it.uniba.di.sms1920.everit.restaurateur.R;

public class DoneOrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doneorder_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putLong(DoneOrderDetailFragment.ARG_ITEM_ID,
                    getIntent().getLongExtra(DoneOrderDetailFragment.ARG_ITEM_ID, 0));
            DoneOrderDetailFragment fragment = new DoneOrderDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.doneorder_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            navigateUpTo(new Intent(this, DoneOrderListActivity.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}