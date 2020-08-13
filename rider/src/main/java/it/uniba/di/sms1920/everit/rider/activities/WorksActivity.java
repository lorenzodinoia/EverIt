package it.uniba.di.sms1920.everit.rider.activities;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import it.uniba.di.sms1920.everit.rider.R;

public class WorksActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works);
        this.initComponents();
    }

    private void initComponents() {
        DeliveriesPagerAdapter deliveriesPagerAdapter = new DeliveriesPagerAdapter(getSupportFragmentManager());
        deliveriesPagerAdapter.addFragment(new ProposalsFragment(), getString(R.string.label_proposal));
        deliveriesPagerAdapter.addFragment(new AssignedOrdersFragment(), getString(R.string.label_assigned_orders));
        deliveriesPagerAdapter.addFragment(new DeliveriesFragment(), getString(R.string.label_deliveries));

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(deliveriesPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}