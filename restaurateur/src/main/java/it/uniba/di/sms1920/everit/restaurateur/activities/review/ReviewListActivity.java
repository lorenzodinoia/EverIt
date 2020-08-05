package it.uniba.di.sms1920.everit.restaurateur.activities.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import it.uniba.di.sms1920.everit.restaurateur.R;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Review;
import it.uniba.di.sms1920.everit.utils.request.ReviewRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ReviewListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public static final List<Review> reviewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (findViewById(R.id.review_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.review_list);
        assert recyclerView != null;
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.readRestaurateurReviews(new RequestListener<Collection<Review>>() {
            @Override
            public void successResponse(Collection<Review> response) {
                reviewList.clear();
                reviewList.addAll(response);
                setupRecyclerView((RecyclerView) recyclerView);
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO gestire errorResponse
            }
        });

    }

    public static Review getReviewById(long id) {
        Review review = null;

        for (Review r : ReviewListActivity.reviewList) {
            if (r.getId() == id) {
                review = r;
                break;
            }
        }

        return review;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new ReviewwRecyclerViewAdapter(this, reviewList, mTwoPane));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ReviewwRecyclerViewAdapter
            extends RecyclerView.Adapter<ReviewwRecyclerViewAdapter.ViewHolder> {

        private final ReviewListActivity mParentActivity;
        private final List<Review> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Review item = (Review) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putLong(ReviewDetailFragment.ARG_ITEM_ID, item.getId());
                    ReviewDetailFragment fragment = new ReviewDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.review_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ReviewDetailActivity.class);
                    intent.putExtra(ReviewDetailFragment.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        ReviewwRecyclerViewAdapter(ReviewListActivity parent,
                                      List<Review> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Review item = mValues.get(position);
            holder.textViewCustomerName.setText(item.getCustomer().getName());

            //TODO aggiungere data?
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
            LocalDateTime estimatedDeliveryTime = item.getCreatedAt();
            String dateAsString = estimatedDeliveryTime.format(formatter);
            holder.textViewReviewDate.setText(dateAsString);
            holder.ratingBar.setRating(item.getVote());
            holder.textViewRatingIndicator.setText(String.format("%d/5", item.getVote()));

            holder.itemView.setTag(item);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewCustomerName;
            final TextView textViewReviewDate;
            final RatingBar ratingBar;
            final TextView textViewRatingIndicator;

            ViewHolder(View view) {
                super(view);
                textViewCustomerName = (TextView) view.findViewById(R.id.textViewCustomerName);
                textViewReviewDate = (TextView) view.findViewById(R.id.textViewReviewDate);
                ratingBar = (RatingBar) view.findViewById(R.id.ratingBarReview);
                textViewRatingIndicator = (TextView) view.findViewById(R.id.textViewRatingIndicatorReview);
            }
        }
    }
}