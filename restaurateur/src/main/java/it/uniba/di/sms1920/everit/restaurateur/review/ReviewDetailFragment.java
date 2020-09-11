package it.uniba.di.sms1920.everit.restaurateur.review;

import android.os.Bundle;

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

    private TextView textViewOrderNumberDetail;
    private TextView textViewCustomerNameDetail;
    private TextView textViewReviewDate;
    private RatingBar ratingBarReviewDetail;
    private TextView textViewRatingIndicatorReviewDetail;
    private TextView textViewReviewDescription;


    public ReviewDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            if (getArguments().containsKey(ARG_ITEM_ID)) {
                long id = getArguments().getLong(ARG_ITEM_ID);
                mItem = ReviewListActivity.getReviewById(id);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.review_detail, container, false);

        if (mItem != null) {
            textViewOrderNumberDetail = rootView.findViewById(R.id.textViewOrderNumberDetail);
            textViewCustomerNameDetail = rootView.findViewById(R.id.textViewCustomerNameDetail);
            textViewReviewDate = rootView.findViewById(R.id.textViewReviewDate);
            ratingBarReviewDetail = rootView.findViewById(R.id.ratingBarReviewDetail);
            textViewRatingIndicatorReviewDetail = rootView.findViewById(R.id.textViewRatingIndicatorReviewDetail);
            textViewReviewDescription = rootView.findViewById(R.id.textViewReviewDescription);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mItem != null){
            textViewOrderNumberDetail.setText("#"+mItem.getId());
            textViewCustomerNameDetail.setText(mItem.getCustomer().getName());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
            LocalDateTime estimatedDeliveryTime = mItem.getCreatedAt();
            String dateAsString = estimatedDeliveryTime.format(formatter);
            textViewReviewDate.setText(dateAsString);
            ratingBarReviewDetail.setRating(mItem.getVote());
            textViewRatingIndicatorReviewDetail.setText(String.format("%d/5", mItem.getVote()));
            textViewReviewDescription.setText(mItem.getText());
        }
    }
}