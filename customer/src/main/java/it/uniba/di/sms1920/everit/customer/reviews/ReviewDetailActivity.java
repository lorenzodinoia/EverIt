package it.uniba.di.sms1920.everit.customer.reviews;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Review;
import it.uniba.di.sms1920.everit.utils.request.ReviewRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

/**
 * An activity representing a single Review detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ReviewListActivity}.
 */
public class ReviewDetailActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "item_id";
    private static final String SAVED_REVIEW = "saved.review";
    private long reviewId;
    private Review review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if ((extras != null) && (extras.containsKey(ARG_ITEM_ID))) {
                this.reviewId = extras.getLong(ARG_ITEM_ID);
            }
        }
        else if (savedInstanceState.containsKey(SAVED_REVIEW)) {
            this.review = savedInstanceState.getParcelable(SAVED_REVIEW);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.review == null) { //The review needs to be loaded
            this.loadData();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_REVIEW, this.review);
    }

    private void setUpFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ReviewDetailFragment.ARG_ITEM, this.review);
        ReviewDetailFragment fragment = new ReviewDetailFragment();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().add(R.id.review_detail_container, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    private void loadData() {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.read(this.reviewId, new RequestListener<Review>() {
            @Override
            public void successResponse(Review response) {
                review = response;
                setUpFragment();
            }

            @Override
            public void errorResponse(RequestException error) {
                Utility.showGenericMessage(ReviewDetailActivity.this, error.getMessage());
            }
        });
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
