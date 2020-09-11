package it.uniba.di.sms1920.everit.customer.results;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Address;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResultListActivity extends AppCompatActivity {
    public static final String PARAMETER_ADDRESS = "address";
    private static final String SAVED_SEARCH_ADDRESS = "saved.search_address";

    private boolean twoPaneMode;
    public static final List<Restaurateur> resultList = new ArrayList<>();
    private RestaurateurRecyclerViewAdapter recyclerViewAdapter;
    private Address searchAddress;
    private TextView textViewEmptyResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        resultList.clear();
        this.initUi();

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Address searchAddress;
            if ((intent != null) && ((searchAddress = intent.getParcelableExtra(PARAMETER_ADDRESS)) != null)) {
                this.searchAddress = searchAddress;
            }
        }
        else if (savedInstanceState.containsKey(SAVED_SEARCH_ADDRESS)) {
            this.searchAddress = savedInstanceState.getParcelable(SAVED_SEARCH_ADDRESS);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.searchAddress != null) {
            this.search();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_SEARCH_ADDRESS, this.searchAddress);
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_result_list);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.result_detail_container) != null) {
            twoPaneMode = true;
        }

        textViewEmptyResult = findViewById(R.id.textViewEmptyResultCustomer);

        View recyclerView = findViewById(R.id.result_list);
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void search() {
        RestaurateurRequest searchRequest = new RestaurateurRequest();
        searchRequest.search(this.searchAddress.getLatitude(), this.searchAddress.getLongitude(), new RequestListener<Collection<Restaurateur>>() {
            @Override
            public void successResponse(Collection<Restaurateur> response) {
                resultList.clear();
                if(!response.isEmpty()){
                    textViewEmptyResult.setVisibility(View.INVISIBLE);
                    resultList.addAll(response);
                }
                else{
                    textViewEmptyResult.setVisibility(View.VISIBLE);
                    textViewEmptyResult.setText(R.string.no_results);
                    textViewEmptyResult.bringToFront();
                }
                if (recyclerViewAdapter != null) {
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResponse(RequestException error) {
                promptErrorMessage(error.getMessage());
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerViewAdapter = new RestaurateurRecyclerViewAdapter(this, ResultListActivity.resultList, twoPaneMode);
        recyclerView.setAdapter(this.recyclerViewAdapter);
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

    public static class RestaurateurRecyclerViewAdapter extends RecyclerView.Adapter<RestaurateurRecyclerViewAdapter.ViewHolder> {
        private final ResultListActivity parentActivity;
        private final List<Restaurateur> results;
        private final boolean towPaneMode;

        private final View.OnClickListener onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Restaurateur item = (Restaurateur) view.getTag();

                Context context = view.getContext();
                Intent intent = new Intent(context, ResultDetailActivity.class);
                intent.putExtra(ResultDetailActivity.ARG_ITEM_ID, item.getId());

                context.startActivity(intent);
                /**
                Restaurateur item = (Restaurateur) view.getTag();
                if (towPaneMode) {
                    Bundle arguments = new Bundle();
                    arguments.putLong(ResultDetailActivity.ARG_ITEM_ID, item.getId());
                    ResultDetailFragment fragment = new ResultDetailFragment();
                    fragment.setArguments(arguments);
                    parentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.result_detail_container, fragment).commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ResultDetailActivity.class);
                    intent.putExtra(ResultDetailActivity.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
                 */
            }
        };

        RestaurateurRecyclerViewAdapter(ResultListActivity parentActivity, List<Restaurateur> results, boolean twoPaneMode) {
            this.results = results;
            this.parentActivity = parentActivity;
            towPaneMode = twoPaneMode;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Restaurateur item = this.results.get(position);
            if (item != null) {
                String restaurantLogoPath = item.getImagePath();
                if (restaurantLogoPath != null) {
                    //Carica immagine dal server
                    String imageUrl = String.format("%s/images/%s", Constants.SERVER_HOST, restaurantLogoPath);
                    Picasso.get()
                            .load(imageUrl)
                            .error(R.mipmap.icon)
                            .placeholder(R.mipmap.icon)
                            .fit()
                            .transform(new CropCircleTransformation())
                            .into(holder.imageViewRestaurantLogo);
                }
                holder.textViewRestaurantName.setText(item.getShopName());
                holder.ratingBarAvgReview.setRating(item.getAvg());
                holder.textViewIndicatorAvgReview.setText(String.format("%d/5", item.getAvg()));

                holder.textViewMinPrice.setText(item.getMinPrice() + " " + parentActivity.getString(R.string.currency_type));
                holder.textViewDeliveryCost.setText(item.getDeliveryCost() + " " + parentActivity.getString(R.string.currency_type));
                holder.textViewAddress.setText(item.getAddress().getFullAddress());
                if(item.isOpen()){
                    holder.textViewShopStatus.setText(R.string.open_shop);
                    holder.textViewShopStatus.setTextColor(ContextCompat.getColor(parentActivity, R.color.colorPrimary));
                }
                else{
                    holder.textViewShopStatus.setText(R.string.closed_shop);
                    holder.textViewShopStatus.setTextColor(ContextCompat.getColor(parentActivity, R.color.colorWarning));
                }


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
            final TextView textViewRestaurantName;
            final TextView textViewMinPrice;
            final TextView textViewDeliveryCost;
            final TextView textViewAddress;
            final RatingBar ratingBarAvgReview;
            final TextView textViewIndicatorAvgReview;
            final TextView textViewShopStatus;

            ViewHolder(View view) {
                super(view);
                imageViewRestaurantLogo = view.findViewById(R.id.imageViewRestaurantLogo);
                textViewRestaurantName = view.findViewById(R.id.textViewRestaurantName);
                textViewMinPrice = view.findViewById(R.id.textViewMinPrice);
                textViewDeliveryCost = view.findViewById(R.id.textViewDeliveryCost);
                textViewAddress = view.findViewById(R.id.textViewAddress);
                ratingBarAvgReview = view.findViewById(R.id.ratingBarAvgReview);
                textViewIndicatorAvgReview = view.findViewById(R.id.textViewIndicatorAvgReview);
                textViewShopStatus = view.findViewById(R.id.textViewShopStatus);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
