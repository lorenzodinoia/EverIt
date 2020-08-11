package it.uniba.di.sms1920.everit.customer.activities.reviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.threeten.bp.LocalDateTime;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.models.Review;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.ReviewRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class ReviewDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private Review review;
    private ReviewDetailActivity mParent;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.review_detail, container, false);

        if (review != null) {
            RatingBar ratingBarDF = rootView.findViewById(R.id.ratingBarReviewDetail);
            TextView textViewIndicatorRate = rootView.findViewById(R.id.textViewRatingIndicatorReviewDetail);
            TextView textViewRateDescription = rootView.findViewById(R.id.textViewReviewDescription);
            MaterialButton btnEditReview = rootView.findViewById(R.id.buttonEditReview);

            ratingBarDF.setRating(review.getVote());
            textViewIndicatorRate.setText(String.format("%d/5", review.getVote()));
            textViewRateDescription.setText(review.getText());
            btnEditReview.setOnClickListener(v -> {
                modifyReview();
            });
        }

        return rootView;
    }

    private void modifyReview(){
        Dialog dialogModifyReview = new Dialog(getActivity());
        dialogModifyReview.setContentView(R.layout.dialog_make_review);

        TextInputLayout reviewLayout = dialogModifyReview.findViewById(R.id.editTextReviewContainer);
        TextInputEditText editTextReview = dialogModifyReview.findViewById(R.id.editTextReview);
        editTextReview.setText(review.getText());

        RatingBar ratingBarDialog = dialogModifyReview.findViewById(R.id.ratingBarReviewDialog);
        ratingBarDialog.setRating((float) review.getVote());

        MaterialButton buttonConfirmReview = dialogModifyReview.findViewById(R.id.buttonConfirmReview);
        buttonConfirmReview.setOnClickListener(v1 -> {
            Review modReview = new Review(
                    review.getId(),
                    (int) ratingBarDialog.getRating(),
                    editTextReview.getText().toString(),
                    LocalDateTime.now(),
                    (Customer) Providers.getAuthProvider().getUser(),
                    review.getRestaurateur());

            ReviewRequest reviewRequest = new ReviewRequest();
            reviewRequest.update(modReview, new RequestListener<Review>() {
                @Override
                public void successResponse(Review response) {
                    dialogModifyReview.dismiss();
                    //TODO Manca aggiornamento automatico
                }

                @Override
                public void errorResponse(RequestException error) {
                    dialogModifyReview.dismiss();
                    promptErrorMessage(error.getMessage());
                }
            });
        });

        MaterialButton buttonClose = dialogModifyReview.findViewById(R.id.BtnCancel);
        buttonClose.setOnClickListener(v1 -> dialogModifyReview.dismiss());

        dialogModifyReview.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof  ReviewDetailActivity){
            mParent = (ReviewDetailActivity) context;
        }
    }

    private void promptErrorMessage(String message){
        Dialog errorDialog = new Dialog(mParent);
        errorDialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = errorDialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = errorDialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = errorDialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            errorDialog.dismiss();
        });

        errorDialog.show();
    }
}
