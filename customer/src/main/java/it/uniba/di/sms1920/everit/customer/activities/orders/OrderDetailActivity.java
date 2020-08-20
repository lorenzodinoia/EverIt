package it.uniba.di.sms1920.everit.customer.activities.orders;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.orders.tab.NotesFragment;
import it.uniba.di.sms1920.everit.customer.activities.orders.tab.OrderTabManagerFragment;
import it.uniba.di.sms1920.everit.customer.activities.orders.tab.OrderTabPagerAdapter;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.InfoFragment;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.MenuFragment;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.ResultTabPagerAdapter;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.ReviewListFragment;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

/**
 * An activity representing a single Order detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link OrderListActivity}.
 */
public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_default);

        toolbar.setTitle(R.string.order_detail);

        long orderId = getIntent().getLongExtra(OrderDetailFragment.ARG_ITEM_ID, 0);

        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putLong(OrderDetailFragment.ARG_ITEM_ID, orderId);
            OrderTabManagerFragment orderTabManagerFragment = new OrderTabManagerFragment();
            orderTabManagerFragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().add(R.id.order_activity_detail_container, orderTabManagerFragment);
            transaction.addToBackStack(null).commit();
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


}
