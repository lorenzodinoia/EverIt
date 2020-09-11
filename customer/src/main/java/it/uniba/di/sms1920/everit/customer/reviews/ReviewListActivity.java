package it.uniba.di.sms1920.everit.customer.reviews;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.cartActivity.CartActivity;
import it.uniba.di.sms1920.everit.customer.LoginActivity;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Review;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.ReviewRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ReviewListActivity extends AppCompatActivity {
    private boolean twoPaneMode;
    private ReviewRecyclerViewAdapter recyclerViewAdapter;
    public final ArrayList<Review> reviewList = new ArrayList<>();
    private TextView textViewEmptyReview;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        this.initUi();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.loadData();
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
            case R.id.goTo_cart: {
                if (Providers.getAuthProvider().getUser() != null) {
                    Intent cartIntent = new Intent(getApplicationContext(), CartActivity.class);
                    startActivity(cartIntent);
                }
                else{
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    loginIntent.putExtra(LoginActivity.INTENT_FLAG, 0);
                    startActivity(loginIntent);
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        this.recyclerViewAdapter = new ReviewRecyclerViewAdapter(this, reviewList, twoPaneMode);
        this.recyclerView.setAdapter(this.recyclerViewAdapter);
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar_default);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.review_detail_container) != null) {
            /*
             * Se il layout è presente vuol dire che l'app è installata su un dispositivo di grandi dimensioni
             * Pertanto si utilizza la modalità con due pannelli
             */
            twoPaneMode = true;
        }

        this.swipeRefreshLayout = findViewById(R.id.swipeContainer);
        this.swipeRefreshLayout.setOnRefreshListener(this::loadData);
        this.textViewEmptyReview = findViewById(R.id.textViewEmptyReviewCustomer);
        this.recyclerView = findViewById(R.id.review_list);
        this.setupRecyclerView();
    }

    private void loadData() {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.readCustomerReviews(new RequestListener<Collection<Review>>() {
            @Override
            public void successResponse(Collection<Review> response) {
                stopRefreshLayout();
                reviewList.clear();
                if(!response.isEmpty()) {
                    textViewEmptyReview.setVisibility(View.INVISIBLE);

                    reviewList.addAll(response);
                }
                else {
                    textViewEmptyReview.setVisibility(View.VISIBLE);
                    textViewEmptyReview.setText(R.string.no_reviews);
                    textViewEmptyReview.bringToFront();
                }
                if (recyclerViewAdapter != null) {
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResponse(RequestException error) {
                stopRefreshLayout();
                promptErrorMessage(error.getMessage());
            }
        });
    }

    private void stopRefreshLayout() {
        if (this.swipeRefreshLayout != null) {
            this.swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void promptErrorMessage(String message) {
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

    public static class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {
        private final ReviewListActivity parentActivity;
        private final List<Review> results;
        private final boolean twoPaneMode;
        private final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Review item = (Review) view.getTag();
                if (twoPaneMode) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(ReviewDetailFragment.ARG_ITEM, item);
                    ReviewDetailFragment fragment = new ReviewDetailFragment();
                    fragment.setArguments(arguments);
                    parentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.review_detail_container, fragment)
                            .commit();
                }
                else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ReviewDetailActivity.class);
                    intent.putExtra(ReviewDetailActivity.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        ReviewRecyclerViewAdapter(ReviewListActivity parent, List<Review> items, boolean twoPane) {
            results = items;
            parentActivity = parent;
            twoPaneMode = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_content, parent, false);
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
                holder.itemView.setOnClickListener(onClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return results.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
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
}
