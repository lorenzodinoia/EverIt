package it.uniba.di.sms1920.everit.restaurateur.activities.review;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Review;

public class ReviewDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private Review mItem;


    public ReviewDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            long id =  getArguments().getLong(ARG_ITEM_ID);
            mItem = ReviewListActivity.getReviewById(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.review_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.textViewCustomerNameReviewDetail)).setText(mItem.getCustomer().getName());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
            LocalDateTime estimatedDeliveryTime = mItem.getCreatedAt();
            String dateAsString = estimatedDeliveryTime.format(formatter);
            ((TextView) rootView.findViewById(R.id.textViewDateReviewDetail)).setText(dateAsString);
            //TODO add review date
            ((RatingBar) rootView.findViewById(R.id.ratingBarReviewDetail)).setRating(mItem.getVote());
            ((TextView) rootView.findViewById(R.id.textViewratingBarReviewDetailIndicator)).setText(String.format("%d/5", mItem.getVote()));
            ((TextView) rootView.findViewById(R.id.textViewReviewText)).setText(mItem.getText());
        }

        return rootView;
    }
}