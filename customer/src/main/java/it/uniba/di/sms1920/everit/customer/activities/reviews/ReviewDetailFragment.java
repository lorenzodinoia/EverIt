package it.uniba.di.sms1920.everit.customer.activities.reviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
    public static final String ARG_ITEM = "item";
    private static final String SAVED_REVIEW = "saved.review";

    private Review review;
    private AppCompatActivity parentActivity;
    private TextView textViewShopNameReviewDetail;
    private RatingBar ratingBarDF;
    private TextView textViewIndicatorRate;
    private TextView textViewRateDescription;
    private TextView textViewReviewDate;
    private ImageView imageViewRestaurateur;

    public ReviewDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            if ((arguments != null) && (arguments.containsKey(ARG_ITEM))) {
                this.review = arguments.getParcelable(ARG_ITEM);
            }
        }
        else if (savedInstanceState.containsKey(SAVED_REVIEW)) {
            this.review = savedInstanceState.getParcelable(SAVED_REVIEW);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof  AppCompatActivity) {
            parentActivity = (AppCompatActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_detail, container, false);
        this.initUi(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.review != null) {
            this.initData();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_REVIEW, this.review);
    }

    private void initUi(View view) {
        this.imageViewRestaurateur = view.findViewById(R.id.imageViewRestaurateur);
        this.textViewShopNameReviewDetail = view.findViewById(R.id.textViewShopNameReviewDetail);
        this.ratingBarDF = view.findViewById(R.id.ratingBarReviewDetail);
        this.textViewIndicatorRate = view.findViewById(R.id.textViewRatingIndicatorReviewDetail);
        this.textViewRateDescription = view.findViewById(R.id.textViewReviewDescription);
        this.textViewReviewDate = view.findViewById(R.id.textViewReviewDate);
        MaterialButton btnEditReview = view.findViewById(R.id.buttonEditReview);
        btnEditReview.setOnClickListener(v -> updateReview());
        MaterialButton btnDeleteReview = view.findViewById(R.id.buttonDeleteReview);
        btnDeleteReview.setOnClickListener(v -> deleteReview());
    }

    private void initData() {
        if (this.review.getRestaurateur().getImagePath() != null) {
            String imageUrl = String.format("%s/images/%s", Constants.SERVER_HOST, this.review.getRestaurateur().getImagePath());
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.mipmap.icon)
                    .fit()
                    .transform(new CropCircleTransformation())
                    .into(this.imageViewRestaurateur);
        }
        this.textViewShopNameReviewDetail.setText(this.review.getRestaurateur().getShopName());
        this.ratingBarDF.setRating(this.review.getVote());
        this.textViewIndicatorRate.setText(String.format("%d/5", review.getVote()));
        this.textViewRateDescription.setText(this.review.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
        LocalDateTime estimatedDeliveryTime = this.review.getCreatedAt();
        String dateAsString = estimatedDeliveryTime.format(formatter);
        this.textViewReviewDate.setText(dateAsString);
    }

    private void updateReview() {
        Dialog dialogModifyReview = new Dialog(parentActivity);
        dialogModifyReview.setContentView(R.layout.dialog_make_review);

        TextInputLayout reviewLayout = dialogModifyReview.findViewById(R.id.editTextReviewContainer);
        TextInputEditText editTextReview = dialogModifyReview.findViewById(R.id.editTextReview);
        editTextReview.setText(review.getText());

        RatingBar ratingBarDialog = dialogModifyReview.findViewById(R.id.ratingBarReviewDialog);
        ratingBarDialog.setRating((float) review.getVote());
        ratingBarDialog.setStepSize(1);

        MaterialButton buttonConfirmReview = dialogModifyReview.findViewById(R.id.buttonConfirmReview);
        buttonConfirmReview.setOnClickListener(v1 -> {
            if(Utility.isValidReview(editTextReview.getText().toString(), reviewLayout, parentActivity)) {
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
                        review = new Review(response.getId(),
                                response.getVote(),
                                response.getText(),
                                response.getCreatedAt(),
                                review.getCustomer(),
                                review.getRestaurateur());
                        initData();
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

    private void deleteReview() {
        Dialog dialog = new Dialog(parentActivity);
        dialog.setContentView(R.layout.dialog_message_y_n);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(getString(R.string.delete_review));

        TextView message = dialog.findViewById(R.id.textViewMessage);
        message.setText(getString(R.string.message_confirm_delete_review));

        MaterialButton btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            ReviewRequest request = new ReviewRequest();
            request.delete(review.getId(), new RequestListener<Boolean>() {
                @Override
                public void successResponse(Boolean response) {
                    dialog.dismiss();
                    parentActivity.finish();
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

    private void promptErrorMessage(String message){
        Dialog errorDialog = new Dialog(parentActivity);
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
