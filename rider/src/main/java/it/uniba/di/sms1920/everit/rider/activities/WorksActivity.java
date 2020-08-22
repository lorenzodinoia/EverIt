package it.uniba.di.sms1920.everit.rider.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.rider.activities.assignedOrder.AssignedOrdersFragment;
import it.uniba.di.sms1920.everit.rider.activities.delivery.DeliveriesFragment;
import it.uniba.di.sms1920.everit.rider.activities.delivery.DeliveriesPagerAdapter;
import it.uniba.di.sms1920.everit.rider.activities.proposal.ProposalsFragment;
import it.uniba.di.sms1920.everit.utils.DataBinder;

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
        DeliveriesPagerAdapter deliveriesPagerAdapter = new DeliveriesPagerAdapter(getSupportFragmentManager());
        deliveriesPagerAdapter.addFragment(new ProposalsFragment(), getString(R.string.label_proposal));
        deliveriesPagerAdapter.addFragment(new AssignedOrdersFragment(), getString(R.string.label_assigned_orders));
        deliveriesPagerAdapter.addFragment(new DeliveriesFragment(), getString(R.string.label_deliveries));

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(deliveriesPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedTab = deliveriesPagerAdapter.getItem(tab.getPosition());
                if (selectedTab instanceof DataBinder) {
                    ((DataBinder) selectedTab).refreshData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
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