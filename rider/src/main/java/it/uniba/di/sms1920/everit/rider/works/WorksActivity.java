package it.uniba.di.sms1920.everit.rider.works;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.rider.works.assignedOrder.AssignedOrdersFragment;
import it.uniba.di.sms1920.everit.rider.works.delivery.DeliveriesFragment;
import it.uniba.di.sms1920.everit.rider.works.proposal.ProposalsFragment;

public class WorksActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        toolbar.setTitle(getString(R.string.activity_my_works));
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.initComponents();
    }

    private void initComponents() {
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.viewPager);

        DeliveriesPagerAdapter deliveriesPagerAdapter = new DeliveriesPagerAdapter(getSupportFragmentManager(), 0);
        deliveriesPagerAdapter.addFragment(new ProposalsFragment(), getString(R.string.label_proposal));
        deliveriesPagerAdapter.addFragment(new AssignedOrdersFragment(), getString(R.string.label_assigned_orders));
        deliveriesPagerAdapter.addFragment(new DeliveriesFragment(), getString(R.string.label_deliveries));

        viewPager.setAdapter(deliveriesPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}