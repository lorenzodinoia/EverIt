package it.uniba.di.sms1920.everit.customer.activities.reviews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Review;
import it.uniba.di.sms1920.everit.utils.request.ReviewRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class ReviewListActivity extends AppCompatActivity {

    private ReviewListActivity.ReviewRecyclerViewAdapter recyclerViewAdapter;
    public static final List<Review> resultList = new ArrayList<>();
    private boolean mTwoPane;

    //TODO non funziona la back arrow

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

        View recyclerView = findViewById(R.id.review_list);
        if(recyclerView != null){
            setupRecyclerView((RecyclerView) recyclerView);
        }
                ReviewRequest reviewRequest = new ReviewRequest();
                reviewRequest.readAll(new RequestListener<Collection<Review>>() {
                    @Override
                    public void successResponse(Collection<Review> response) {
                        resultList.clear();
                        resultList.addAll(response);
                        setupRecyclerView((RecyclerView) recyclerView);
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        //TODO implementare gestione errore mostra recensioni customer
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        Log.d("test", error.toString());
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
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

        ReviewRecyclerViewAdapter(ReviewListActivity parent,
                                      List<Review> items,
                                      boolean twoPane) {
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
                    String imageUrl = String.format("%s/%s", Constants.SERVER_HOST, restaurateurLogoPath);
                    Picasso.get()
                            .load(imageUrl)
                            .error(R.mipmap.icon)
                            .placeholder(R.mipmap.icon)
                            .transform(new CropCircleTransformation())
                            .fit()
                            .into(holder.imageViewRestaurantLogo);
                }

                holder.textViewShopName.setText(item.getRestaurateur().getShopName());
                holder.ratingBarReviewPreview.setRating(item.getVote());
                holder.textViewRatingIndicator.setText(String.format("%d/5", item.getVote()));

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

            ViewHolder(View view) {
                super(view);
                imageViewRestaurantLogo = (ImageView) view.findViewById(R.id.imageViewRestaurantLogoReviewListContent);
                textViewShopName = (TextView) view.findViewById(R.id.textViewShopNameReviewListContent);
                ratingBarReviewPreview = (RatingBar) view.findViewById(R.id.ratingBarReviewListContent);
                textViewRatingIndicator = (TextView) view.findViewById(R.id.textViewRatingIndicatorReviewListContent);
            }
        }
    }


}
