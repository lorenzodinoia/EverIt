package it.uniba.di.sms1920.everit.customer.activities.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.orders.tab.NotesFragment;
import it.uniba.di.sms1920.everit.customer.activities.orders.tab.OrderTabPagerAdapter;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.InfoFragment;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.MenuFragment;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.ResultTabPagerAdapter;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.ReviewListFragment;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;

/**
 * An activity representing a single Order detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link OrderListActivity}.
 */
public class OrderDetailActivity extends AppCompatActivity {

    private Order order;
    private TabLayout tabLayout;
    private OrderTabPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_default);
        long idObj = getIntent().getLongExtra(OrderDetailFragment.ARG_ITEM_ID, 0);
        order = OrderListActivity.getOrderById(idObj);

        if (order != null) {
            toolbar.setTitle(order.getRestaurateur().getShopName());
        }

        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            tabLayout = findViewById(R.id.tabs);
            viewPager = findViewById(R.id.viewPager);

            pagerAdapter = new OrderTabPagerAdapter(getSupportFragmentManager(), 0);
            OrderDetailFragment detailFragment = new OrderDetailFragment();
            NotesFragment notesFragment = new NotesFragment();

            pagerAdapter.addFragment(detailFragment, getString(R.string.order_detail));
            pagerAdapter.addFragment(notesFragment, getString(R.string.notes));

            viewPager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, OrderListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Order getOrder(){
        /*if(order.getDeliveryNotes().isEmpty()){
            order.setDeliveryNotes("");
        }

        if(order.getOrderNotes().isEmpty()){
            order.setOrderNotes("");
        }*/

        return order;
    }

}
