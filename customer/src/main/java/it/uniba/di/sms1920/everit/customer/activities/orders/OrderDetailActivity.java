package it.uniba.di.sms1920.everit.customer.activities.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Order;


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
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        long idObj = getIntent().getLongExtra(OrderDetailFragment.ARG_ITEM_ID, 0);
        Order item = OrderListActivity.getOrderById(idObj);

        ImageView imageView = findViewById(R.id.imageViewDetailOrder);
        if(item.getRestaurateur().getImagePath() != null){
            String imageUrl = String.format("%s/%s", Constants.SERVER_HOST, item.getRestaurateur().getImagePath());
            Picasso.get()
                    .load(imageUrl)
                    .error(R.mipmap.icon)
                    .placeholder(R.mipmap.icon)
                    .fit()
                    .into(imageView);
        }

        if (item != null) {
            toolbar.setTitle(item.getRestaurateur().getShopName());
        }

        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(OrderDetailFragment.ARG_ITEM_ID,
                    getIntent().getLongExtra(OrderDetailFragment.ARG_ITEM_ID, 0));
            OrderDetailFragment fragment = new OrderDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.order_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, OrderListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
