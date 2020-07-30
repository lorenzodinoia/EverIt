package it.uniba.di.sms1920.everit.customer.activities.results.Tabs;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.results.ResultDetailActivity;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.Review;

public class ReviewListFragment extends Fragment {

    private ResultDetailActivity resultDetailActivity;
    private Restaurateur restaurateur;

    private ReviewCardRecyclerViewAdapter reviewCardRecyclerViewAdapter;

    private RecyclerView reviewRecycleView;
    private TextView textViewReviewNumber;
    private List<Review> reviews;
    private RatingBar ratingBar;


    public ReviewListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review_list, container, false);

        textViewReviewNumber = rootView.findViewById(R.id.textViewReviewNumber);
        reviewRecycleView = rootView.findViewById(R.id.review_complete_card_list);
        ratingBar = rootView.findViewById(R.id.ratingBarReviewDetail);

        setupRecyclerView(reviewRecycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        reviewRecycleView.setLayoutManager(linearLayoutManager);

        return rootView;
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