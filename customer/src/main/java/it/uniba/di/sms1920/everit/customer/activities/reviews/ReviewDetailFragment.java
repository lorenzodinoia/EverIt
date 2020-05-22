package it.uniba.di.sms1920.everit.customer.activities.reviews;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Review;

public class ReviewDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private Review review;

    public ReviewDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getArguments() != null) && (getArguments().containsKey(ARG_ITEM_ID))) {
            long id = getArguments().getLong(ARG_ITEM_ID);
            Log.d("test", Long.toString(id));
            review = ReviewListActivity.getReviewById(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.review_detail, container, false);

        if (review != null) {
            RatingBar ratingBarDF = (RatingBar) rootView.findViewById(R.id.ratingBarReviewDetail);
            TextView textViewIndicatorRate = (TextView) rootView.findViewById(R.id.textViewRatingIndicatorReviewDetail);
            TextView textViewRateDescription = (TextView) rootView.findViewById(R.id.textViewReviewDescription);

            Log.d("test", review.getText());
            ratingBarDF.setRating(review.getVote());
            textViewIndicatorRate.setText(String.format("%d/5", review.getVote()));
            textViewRateDescription.setText(review.getText());
        }

        return rootView;
    }
}
