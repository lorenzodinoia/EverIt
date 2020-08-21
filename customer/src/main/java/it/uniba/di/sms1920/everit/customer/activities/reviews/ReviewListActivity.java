package it.uniba.di.sms1920.everit.customer.activities.reviews;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.cartActivity.CartActivity;
import it.uniba.di.sms1920.everit.customer.activities.LoginActivity;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Review;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.ReviewRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class ReviewListActivity extends AppCompatActivity {

    private ReviewRecyclerViewAdapter recyclerViewAdapter;
    public static final List<Review> resultList = new ArrayList<>();
    private boolean mTwoPane;
    private TextView textViewEmptyReview;
    View recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (findViewById(R.id.review_detail_container) != null) {
            /*
             * Se il layout è presente vuol dire che l'app è installata su un dispositivo di grandi dimensioni
             * Pertanto si utilizza la modalità con due pannelli
             */
            mTwoPane = true;
        }

        textViewEmptyReview = findViewById(R.id.textViewEmptyReviewCustomer);
        recyclerView = findViewById(R.id.review_list);
        if(recyclerView != null){
            setupRecyclerView((RecyclerView) recyclerView);

        }
                ReviewRequest reviewRequest = new ReviewRequest();
                reviewRequest.readCustomerReviews(new RequestListener<Collection<Review>>() {
                    @Override
                    public void successResponse(Collection<Review> response) {
                        resultList.clear();
                        if(!response.isEmpty()){
                            textViewEmptyReview.setVisibility(View.INVISIBLE);
                            resultList.addAll(response);
                        }
                        else{
                            textViewEmptyReview.setVisibility(View.VISIBLE);
                            textViewEmptyReview.setText(R.string.no_reviews);
                            textViewEmptyReview.bringToFront();
                        }


                        setupRecyclerView((RecyclerView) recyclerView);
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        promptErrorMessage(error.getMessage());
                    }
                });
    }

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                super.onBackPressed();
                break;
            }

            case R.id.goTo_cart:{
                if(Providers.getAuthProvider().getUser() != null) {
                    Intent cartIntent = new Intent(getApplicationContext(), CartActivity.class);
                    startActivity(cartIntent);
                }else{
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginIntent);
                }
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerViewAdapter = new ReviewRecyclerViewAdapter(this, ReviewListActivity.resultList, mTwoPane);
        recyclerView.setAdapter(this.recyclerViewAdapter);
    }

    public static Review getReviewById(long id) {
        Review review = null;

        for (Review r : ReviewListActivity.resultList) {
            if (r.getId() == id) {
                review = r;
                break;
            }
        }

        return review;
    }

    public static class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {

        private final ReviewListActivity parentActivity;
        private final List<Review> results;
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
                    parentActivity.getSupportFragmentManager().beginTransaction()
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

        ReviewRecyclerViewAdapter(ReviewListActivity parent, List<Review> items, boolean twoPane) {
            results = items;
            parentActivity = parent;
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
            Review item = this.results.get(position);
            if(item != null){
                String restaurateurLogoPath = item.getRestaurateur().getImagePath();
                if(restaurateurLogoPath != null){
                    String imageUrl = String.format("%s/images/%s", Constants.SERVER_HOST, restaurateurLogoPath);
                    Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.mipmap.icon)
                            .transform(new CropCircleTransformation())
                            .fit()
                            .into(holder.imageViewRestaurantLogo);
                }

                holder.textViewShopName.setText(item.getRestaurateur().getShopName());
                holder.ratingBarReviewPreview.setRating(item.getVote());
                holder.textViewRatingIndicator.setText(String.format("%d/5", item.getVote()));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
                LocalDateTime reviewDate = item.getCreatedAt();
                String dateAsString = reviewDate.format(formatter);
                holder.textViewReviewDate.setText(dateAsString);

                holder.itemView.setTag(results.get(position));
                holder.itemView.setOnClickListener(mOnClickListener);
            }
        }



        @Override
        public int getItemCount() {
            return results.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView imageViewRestaurantLogo;
            final TextView textViewShopName;
            final RatingBar ratingBarReviewPreview;
            final TextView textViewRatingIndicator;
            final TextView textViewReviewDate;

            ViewHolder(View view) {
                super(view);
                imageViewRestaurantLogo = view.findViewById(R.id.imageViewRestaurateurLogoReviewListContent);
                textViewShopName = view.findViewById(R.id.textViewShopNameReviewListContent);
                ratingBarReviewPreview = view.findViewById(R.id.ratingBarReviewListContent);
                textViewRatingIndicator = view.findViewById(R.id.textViewRatingIndicatorReviewListContent);
                textViewReviewDate = view.findViewById(R.id.textViewReviewDate);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }


    private void updateData(){
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.readCustomerReviews(new RequestListener<Collection<Review>>() {
            @Override
            public void successResponse(Collection<Review> response) {
                resultList.clear();
                if(!response.isEmpty()){
                    textViewEmptyReview.setVisibility(View.INVISIBLE);
                    resultList.addAll(response);
                }
                else{
                    textViewEmptyReview.setVisibility(View.VISIBLE);
                    textViewEmptyReview.setText(R.string.no_reviews);
                    textViewEmptyReview.bringToFront();
                }

                setupRecyclerView((RecyclerView) recyclerView);
            }

            @Override
            public void errorResponse(RequestException error) {
                promptErrorMessage(error.getMessage());
            }
        });

    }
}
