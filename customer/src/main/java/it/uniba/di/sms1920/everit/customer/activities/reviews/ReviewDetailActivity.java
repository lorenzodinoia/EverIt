package it.uniba.di.sms1920.everit.customer.activities.reviews;

import android.content.Intent;
import android.os.Bundle;

import com.squareup.picasso.Picasso;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;
import android.widget.ImageView;

import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Review;

/**
 * An activity representing a single Review detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ReviewListActivity}.
 */
public class ReviewDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        long reviewId = getIntent().getLongExtra(ReviewDetailFragment.ARG_ITEM_ID, 0);
        Review review = ReviewListActivity.getReviewById(reviewId);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        if(review != null){
            toolbar.setTitle(review.getRestaurateur().getShopName());
        }

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(ReviewDetailFragment.ARG_ITEM_ID,
                    getIntent().getLongExtra(ReviewDetailFragment.ARG_ITEM_ID, 0));
            /*arguments.putString(ReviewDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ReviewDetailFragment.ARG_ITEM_ID));*/
            Log.d("test", Boolean.toString(arguments.isEmpty()));
            ReviewDetailFragment fragment = new ReviewDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.review_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, ReviewListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
