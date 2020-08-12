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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.models.Review;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.ReviewRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

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
            ImageView imageViewRestaurateur = rootView.findViewById(R.id.imageViewRestaurateur);
            TextView textViewShopNameReviewDetail = rootView.findViewById(R.id.textViewShopNameReviewDetail);
            RatingBar ratingBarDF = rootView.findViewById(R.id.ratingBarReviewDetail);
            TextView textViewIndicatorRate = rootView.findViewById(R.id.textViewRatingIndicatorReviewDetail);
            TextView textViewRateDescription = rootView.findViewById(R.id.textViewReviewDescription);
            TextView textViewReviewDate = rootView.findViewById(R.id.textViewReviewDate);
            MaterialButton btnEditReview = rootView.findViewById(R.id.buttonEditReview);
            MaterialButton btnDeleteReview = rootView.findViewById(R.id.buttonDeleteReview);

            if(review.getRestaurateur().getImagePath() != null){
                String imageUrl = String.format("%s/images/%s", Constants.SERVER_HOST, review.getRestaurateur().getImagePath());
                Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.mipmap.icon)
                        .fit()
                        .transform(new CropCircleTransformation())
                        .into(imageViewRestaurateur);
            }
            textViewShopNameReviewDetail.setText(review.getRestaurateur().getShopName());
            ratingBarDF.setRating(review.getVote());
            textViewIndicatorRate.setText(String.format("%d/5", review.getVote()));
            textViewRateDescription.setText(review.getText());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
            LocalDateTime estimatedDeliveryTime = review.getCreatedAt();
            String dateAsString = estimatedDeliveryTime.format(formatter);
            textViewReviewDate.setText(dateAsString);
            btnEditReview.setOnClickListener(v -> {
                modifyReview();
            });
            btnDeleteReview.setOnClickListener(v -> {
                deleteReview();
            });
        }

        return rootView;
    }

    private void modifyReview(){
        Dialog dialogModifyReview = new Dialog(mParent);
        dialogModifyReview.setContentView(R.layout.dialog_make_review);

        TextInputLayout reviewLayout = dialogModifyReview.findViewById(R.id.editTextReviewContainer);
        TextInputEditText editTextReview = dialogModifyReview.findViewById(R.id.editTextReview);
        editTextReview.setText(review.getText());

        RatingBar ratingBarDialog = dialogModifyReview.findViewById(R.id.ratingBarReviewDialog);
        ratingBarDialog.setRating((float) review.getVote());

        MaterialButton buttonConfirmReview = dialogModifyReview.findViewById(R.id.buttonConfirmReview);
        buttonConfirmReview.setOnClickListener(v1 -> {
            if(Utility.isValidReview(editTextReview.getText().toString(), reviewLayout, mParent)) {
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
            }
        });

        MaterialButton buttonClose = dialogModifyReview.findViewById(R.id.BtnCancel);
        buttonClose.setOnClickListener(v1 -> dialogModifyReview.dismiss());

        dialogModifyReview.show();
    }


    private void deleteReview(){
        Dialog dialog = new Dialog(mParent);
        dialog.setContentView(R.layout.dialog_message_y_n);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(getString(R.string.delete_review));

        TextView message = dialog.findViewById(R.id.textViewMessage);
        message.setText(getString(R.string.message_confirm_delete));

        MaterialButton btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            ReviewRequest request = new ReviewRequest();
            request.delete(review.getId(), new RequestListener<Boolean>() {
                @Override
                public void successResponse(Boolean response) {
                    dialog.dismiss();
                    mParent.finish();
                }

                @Override
                public void errorResponse(RequestException error) {
                    dialog.dismiss();
                    promptErrorMessage(error.getMessage());
                }
            });
        });

        MaterialButton btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
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
