package it.uniba.di.sms1920.everit.rider.activities.works;

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
import it.uniba.di.sms1920.everit.rider.activities.works.assignedOrder.AssignedOrdersFragment;
import it.uniba.di.sms1920.everit.rider.activities.works.delivery.DeliveriesFragment;
import it.uniba.di.sms1920.everit.rider.activities.works.proposal.ProposalsFragment;

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
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedTab = deliveriesPagerAdapter.getItem(tab.getPosition());
                //TODO Verificare
                /*if (selectedTab instanceof DataBinder) {
                    ((DataBinder) selectedTab).refreshData();
                }*/
                /*
                if (selectedTab instanceof ProposalsFragment) {
                    ProposalsFragment fragment = (ProposalsFragment) deliveriesPagerAdapter.getItem(tab.getPosition());
                    fragment.loadData();
                }
                else if (selectedTab instanceof AssignedOrdersFragment) {
                    AssignedOrdersFragment fragment = (AssignedOrdersFragment) deliveriesPagerAdapter.getItem(tab.getPosition());
                    fragment.loadData();
                }
                else {
                    DeliveriesFragment fragment = (DeliveriesFragment) deliveriesPagerAdapter.getItem(tab.getPosition());
                    fragment.loadData();
                }
                 */
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