package it.uniba.di.sms1920.everit.customer.activities.results.Tabs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.results.ResultDetailActivity;
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
    private List<Review> reviews;
    private Review review;
    private RatingBar ratingBar;

    private MaterialButton buttonReview;

    
    public ReviewListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review_list, container, false);

        boolean flag = false;

        textViewReviewNumber = rootView.findViewById(R.id.textViewReviewNumber);
        reviewRecycleView = rootView.findViewById(R.id.review_complete_card_list);
        ratingBar = rootView.findViewById(R.id.ratingBarReviewDetail);
        ratingBar.setStepSize(1);
        buttonReview = rootView.findViewById(R.id.buttonReview);

        setupRecyclerView(reviewRecycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        reviewRecycleView.setLayoutManager(linearLayoutManager);


        for(int i=0; i<reviews.size(); i++){
            if((reviews.get(i).getCustomer().getId() == Providers.getAuthProvider().getUser().getId())) {
                review = reviews.get(i);
                flag = true;
            }
        }

        if(!flag){
            buttonReview.setText(R.string.make_review);
            buttonReview.setOnClickListener(v ->  makeReview() );
        }else {
            buttonReview.setText(R.string.modify_review);
            buttonReview.setOnClickListener(v -> modifyReview());
        }


        return rootView;
    }

    private void makeReview(){
        Dialog dialogMakeReview = new Dialog(getActivity());
        dialogMakeReview.setContentView(R.layout.dialog_make_review);

        TextInputLayout reviewLayout = dialogMakeReview.findViewById(R.id.editTextReviewContainer);
        TextInputEditText editTextReview = dialogMakeReview.findViewById(R.id.editTextReview);
        editTextReview.setHint(String.valueOf(R.string.review_hint));

        RatingBar ratingBarDialog = (RatingBar) dialogMakeReview.findViewById(R.id.ratingBarReviewDialog);

        MaterialButton buttonConfirmReview = dialogMakeReview.findViewById(R.id.buttonConfirmReview);
        buttonConfirmReview.setOnClickListener(v1 -> {
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
                    /** Manca aggiornamento automatico */
                }

                @Override
                public void errorResponse(RequestException error) {
                    Log.d("test", error.toString());
                    //TODO gestire
                }
            });
        });

        MaterialButton buttonClose = dialogMakeReview.findViewById(R.id.BtnCancel);
        buttonClose.setOnClickListener(v1 -> dialogMakeReview.dismiss());

        dialogMakeReview.show();
    }

    private void modifyReview(){
        Dialog dialogModifyReview = new Dialog(getActivity());
        dialogModifyReview.setContentView(R.layout.dialog_make_review);

        TextInputLayout reviewLayout = dialogModifyReview.findViewById(R.id.editTextReviewContainer);
        TextInputEditText editTextReview = dialogModifyReview.findViewById(R.id.editTextReview);
        editTextReview.setText(review.getText());

        RatingBar ratingBarDialog = (RatingBar) dialogModifyReview.findViewById(R.id.ratingBarReviewDialog);
        ratingBarDialog.setRating((float) review.getVote());

        MaterialButton buttonConfirmReview = dialogModifyReview.findViewById(R.id.buttonConfirmReview);
        buttonConfirmReview.setOnClickListener(v1 -> {
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
                    /** Manca aggiornamento automatico */
                }

                @Override
                public void errorResponse(RequestException error) {
                    Log.d("test", error.toString());
                    //TODO gestire
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

        if(context instanceof ResultDetailActivity){
            resultDetailActivity = (ResultDetailActivity) context;
            restaurateur = resultDetailActivity.passRestaurateur();
            reviews = resultDetailActivity.passRestaurateurReviews();
        }
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        reviewCardRecyclerViewAdapter = new ReviewCardRecyclerViewAdapter(getActivity(), reviews);
        recyclerView.setAdapter(reviewCardRecyclerViewAdapter);
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
                textViewCustomerName = (TextView) view.findViewById(R.id.textViewCustomerName);
                ratingBarReview = (RatingBar) view.findViewById(R.id.ratingBarReview);
                textViewRatingIndicator = (TextView) view.findViewById(R.id.textViewRatingIndicator);
                textViewDescription = (TextView) view.findViewById(R.id.textViewReviewDescription);
            }
        }

    }



}