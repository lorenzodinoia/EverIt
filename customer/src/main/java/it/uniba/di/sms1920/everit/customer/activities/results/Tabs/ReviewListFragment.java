package it.uniba.di.sms1920.everit.customer.activities.results.Tabs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.results.ResultDetailActivity;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.Review;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.ReviewRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class ReviewListFragment extends Fragment {

    private ResultDetailActivity resultDetailActivity;
    private Restaurateur restaurateur;

    private ReviewCardRecyclerViewAdapter reviewCardRecyclerViewAdapter;

    private RecyclerView reviewRecycleView;
    private TextView textViewReviewNumber;
    private TextView textViewRatingNumber;
    private TextView textViewPlaceholder;
    private TextView textViewLableNumberReview;
    private List<Review> reviews;
    private Review review;
    private RatingBar ratingBar;
    private View divider;
    private ConstraintLayout constraintLayout;

    private MaterialButton buttonReview;
    private MaterialButton buttonDelete;

    
    public ReviewListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review_list, container, false);

        textViewPlaceholder = rootView.findViewById(R.id.textViewPlaceholder);
        constraintLayout = rootView.findViewById(R.id.constraintLayoutReviewListResult);
        textViewLableNumberReview = rootView.findViewById(R.id.textViewLableNumberReview);
        textViewReviewNumber = rootView.findViewById(R.id.textViewReviewNumber);
        reviewRecycleView = rootView.findViewById(R.id.review_complete_card_list);
        ratingBar = rootView.findViewById(R.id.ratingBarReviewDetail);
        ratingBar.setStepSize(1);
        textViewRatingNumber = rootView.findViewById(R.id.textViewRatingNumber);
        buttonReview = rootView.findViewById(R.id.buttonReview);
        divider = rootView.findViewById(R.id.view);
        buttonDelete = rootView.findViewById(R.id.buttonDeleteReviewFragment);

        setupRecyclerView(reviewRecycleView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(resultDetailActivity);
        reviewRecycleView.setLayoutManager(linearLayoutManager);

        setLayoutElements();

        return rootView;
    }

    private void setLayoutElements(){

        boolean flag = false;

        if(reviews.size() > 0) {
            textViewPlaceholder.setVisibility(View.GONE);
            textViewRatingNumber.setVisibility(View.VISIBLE);
            textViewLableNumberReview.setVisibility(View.VISIBLE);
            textViewReviewNumber.setVisibility(View.VISIBLE);
            textViewReviewNumber.setText(Integer.toString(reviews.size()));
            int avg = calculateAvg();
            ratingBar.setRating(avg);
            textViewRatingNumber.setText(String.format("%d/5", avg));
        }
        else{
            textViewPlaceholder.setVisibility(View.VISIBLE);
            textViewPlaceholder.setText(R.string.no_reviews);
            textViewRatingNumber.setVisibility(View.INVISIBLE);
            textViewLableNumberReview.setVisibility(View.INVISIBLE);
            textViewReviewNumber.setVisibility(View.INVISIBLE);
        }

        for(int i=0; i<reviews.size(); i++){
            //TODO crash se l'utente non è loggato, aggiungi avviso
            if((reviews.get(i).getCustomer().getId() == Providers.getAuthProvider().getUser().getId())) {
                review = reviews.get(i);
                flag = true;
            }
        }

        if(!flag){
            buttonReview.setText(R.string.make_review);
            buttonReview.setOnClickListener(v ->  makeReview() );

            buttonDelete.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.buttonReview, constraintSet.BOTTOM, R.id.constraintLayoutReviewListResult, constraintSet.BOTTOM, 8);
            constraintSet.applyTo(constraintLayout);
        }else {
            buttonDelete.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.buttonReview, constraintSet.BOTTOM, R.id.view, constraintSet.TOP, 8);
            constraintSet.applyTo(constraintLayout);

            buttonReview.setText(R.string.modify_review);
            buttonReview.setOnClickListener(v -> modifyReview());

            buttonDelete.setOnClickListener(v -> {
                deleteReview();
            });
        }
    }

    private void updateData(){
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.readRestaurateurReviewsFromCustomer(restaurateur.getId(), new RequestListener<Collection<Review>>() {
            @Override
            public void successResponse(Collection<Review> response) {
                reviews.clear();

                if(!response.isEmpty()) {
                    reviews.addAll(response);
                    int avg = calculateAvg();
                    ratingBar.setRating(avg);
                }
                else{
                    ratingBar.setRating(0);
                }

                setupRecyclerView((RecyclerView) reviewRecycleView);
                setLayoutElements();
            }

            @Override
            public void errorResponse(RequestException error) {
                promptErrorMessage(error.getMessage());
            }
        });
    }


    private void makeReview(){
        Dialog dialogMakeReview = new Dialog(resultDetailActivity);
        dialogMakeReview.setContentView(R.layout.dialog_make_review);

        TextInputLayout reviewLayout = dialogMakeReview.findViewById(R.id.editTextReviewContainer);
        TextInputEditText editTextReview = dialogMakeReview.findViewById(R.id.editTextReview);

        TextView textViewRatingBarIndicator = dialogMakeReview.findViewById(R.id.textViewRatingBarIndicator);

        RatingBar ratingBarDialog = dialogMakeReview.findViewById(R.id.ratingBarReviewDialog);
        ratingBarDialog.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            textViewRatingBarIndicator.setText(String.format("%d/5", (int) ratingBarDialog.getRating()));
        });

        textViewRatingBarIndicator.setText(String.format("%d/5", (int) ratingBarDialog.getRating()));

        MaterialButton buttonConfirmReview = dialogMakeReview.findViewById(R.id.buttonConfirmReview);
        buttonConfirmReview.setOnClickListener(v1 -> {
            if(Utility.isValidReview(editTextReview.getText().toString(), reviewLayout, resultDetailActivity)){
                Review review = new Review(
                        (int)ratingBarDialog.getRating(),
                        editTextReview.getText().toString(),
                        LocalDateTime.now(),
                        (Customer) Providers.getAuthProvider().getUser(),
                        restaurateur
                );

                ReviewRequest reviewRequest = new ReviewRequest();
                reviewRequest.create(review, new RequestListener<Review>() {
                    @Override
                    public void successResponse(Review response) {
                        dialogMakeReview.dismiss();
                        updateData();
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        promptErrorMessage(error.getMessage());
                    }
                });
            }
        });

        MaterialButton buttonClose = dialogMakeReview.findViewById(R.id.BtnCancel);
        buttonClose.setOnClickListener(v1 -> dialogMakeReview.dismiss());

        dialogMakeReview.show();
    }

    private void modifyReview(){
        Dialog dialogModifyReview = new Dialog(resultDetailActivity);
        dialogModifyReview.setContentView(R.layout.dialog_make_review);

        TextInputLayout reviewLayout = dialogModifyReview.findViewById(R.id.editTextReviewContainer);
        TextInputEditText editTextReview = dialogModifyReview.findViewById(R.id.editTextReview);
        editTextReview.setText(review.getText());

        TextView textViewRatingBarIndicator = dialogModifyReview.findViewById(R.id.textViewRatingBarIndicator);

        RatingBar ratingBarDialog = dialogModifyReview.findViewById(R.id.ratingBarReviewDialog);
        ratingBarDialog.setRating((float) review.getVote());
        ratingBarDialog.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            textViewRatingBarIndicator.setText(String.format("%d/5", (int) ratingBarDialog.getRating()));
        });

        textViewRatingBarIndicator.setText(String.format("%d/5", (int) ratingBarDialog.getRating()));

        MaterialButton buttonConfirmReview = dialogModifyReview.findViewById(R.id.buttonConfirmReview);
        buttonConfirmReview.setOnClickListener(v1 -> {
            if(Utility.isValidReview(editTextReview.getText().toString(), reviewLayout, resultDetailActivity)) {
                Review modReview = new Review(
                        review.getId(),
                        (int) ratingBarDialog.getRating(),
                        editTextReview.getText().toString(),
                        LocalDateTime.now(),
                        (Customer) Providers.getAuthProvider().getUser(),
                        restaurateur);

                ReviewRequest reviewRequest = new ReviewRequest();
                reviewRequest.update(modReview, new RequestListener<Review>() {
                    @Override
                    public void successResponse(Review response) {
                        dialogModifyReview.dismiss();
                        updateData();
                    }

                    @Override
                    public void errorResponse(RequestException error) {
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
        Dialog dialog = new Dialog(resultDetailActivity);
        dialog.setContentView(R.layout.dialog_message_y_n);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(R.string.delete_review);
        TextView message = dialog.findViewById(R.id.textViewMessage);
        message.setText(R.string.message_confirm_delete_review);

        MaterialButton btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> {
            ReviewRequest reviewRequest = new ReviewRequest();
            reviewRequest.delete(review.getId(), new RequestListener<Boolean>() {
                @Override
                public void successResponse(Boolean response) {
                    dialog.dismiss();
                    updateData();
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

    private int calculateAvg(){

        float avg;
        float sumReviewVote = 0;

        for(Review item : reviews){
            sumReviewVote += item.getVote();
        }

        avg = sumReviewVote/reviews.size();
        return Math.round(avg);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof ResultDetailActivity){
            resultDetailActivity = (ResultDetailActivity) context;
            restaurateur = resultDetailActivity.passRestaurateur();
            reviews = resultDetailActivity.passRestaurateurReviews();
        }
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        this.reviewCardRecyclerViewAdapter = new ReviewCardRecyclerViewAdapter(resultDetailActivity, reviews);
        recyclerView.setAdapter(this.reviewCardRecyclerViewAdapter);
    }


    public static class ReviewCardRecyclerViewAdapter extends RecyclerView.Adapter<ReviewCardRecyclerViewAdapter.ViewHolder> {

        private final List<Review> resultsListReview;
        private Context context;

        public ReviewCardRecyclerViewAdapter(Context context, List<Review> list) {
            this.context = context;
            resultsListReview = list;
        }



        @NonNull
        @Override
        public ReviewCardRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_complete_card, parent, false);

            return new ReviewCardRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewCardRecyclerViewAdapter.ViewHolder holder, int position) {
            Review item = this.resultsListReview.get(position);

            if(item != null) {
                holder.textViewCustomerName.setText(item.getCustomer().getName());
                holder.textViewDescription.setText(item.getText());
                holder.ratingBarReview.setRating(item.getVote());
                holder.textViewRatingIndicator.setText(String.format("%d/5", item.getVote()));

                holder.itemView.setTag(resultsListReview.get(position));
            }


        }

        @Override
        public int getItemCount() {
            return this.resultsListReview.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewCustomerName;
            final RatingBar ratingBarReview;
            final TextView textViewRatingIndicator;
            final TextView textViewDescription;

            ViewHolder(View view) {
                super(view);
                textViewCustomerName = view.findViewById(R.id.textViewCustomerName);
                ratingBarReview = view.findViewById(R.id.ratingBarReview);
                textViewRatingIndicator = view.findViewById(R.id.textViewRatingIndicator);
                textViewDescription = view.findViewById(R.id.textViewReviewDescription);
            }
        }

    }

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(resultDetailActivity);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
        });

        dialog.show();
    }

}