package it.uniba.di.sms1920.everit.customer.activities.results;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


import it.uniba.di.sms1920.everit.customer.DeliveryAddress;
import it.uniba.di.sms1920.everit.customer.R;

import it.uniba.di.sms1920.everit.customer.activities.CartActivity;
import it.uniba.di.sms1920.everit.customer.cart.Cart;
import it.uniba.di.sms1920.everit.customer.cart.CartConnector;
import it.uniba.di.sms1920.everit.customer.cart.PartialOrder;
import it.uniba.di.sms1920.everit.utils.models.ProductCategory;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.Review;

import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.ReviewRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;


public class ResultDetailFragment extends Fragment implements CartConnector {
    public static final String ARG_ITEM_ID = "item_id";
    private static final String GONE = "GONE";
    private static final String VISIBLE = "VISIBLE";

    private ReviewCardRecyclerViewAdapter reviewCardRecyclerViewAdapter;
    private RecyclerView reviewRecycleView;
    private List<Review> reviews;
    private List<Review> scrollableReviews = new ArrayList<>();

    private Restaurateur restaurateur;
    private TextView textViewPhoneNumber, textViewAddress, textViewOpenClosed, textViewDeliveryCost, textViewMinPurchase, textViewReviewNumber;
    private LinearLayout layoutMenuContainer, layoutMenuText, layoutReviewCardListContainer, layoutCall;
    private MaterialButton buttonOrder;
    private ConstraintLayout layoutRatingBar;
    private RatingBar ratingBar;

    private ExpandableListView expandableListView ;
    private CustomExpandibleMenuAdapter expandableListAdapter;
    private List<ProductCategory> expandableListDetail = new LinkedList<>();


    public ResultDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getArguments() != null) && (getArguments().containsKey(ARG_ITEM_ID))) {
            long id = getArguments().getLong(ARG_ITEM_ID);

            RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
            restaurateurRequest.read(id, new RequestListener<Restaurateur>() {
                @Override
                public void successResponse(Restaurateur response) {
                    restaurateur = response;
                    expandableListDetail = (List<ProductCategory>) restaurateur.getProductCategories();
                    expandableListAdapter = new CustomExpandibleMenuAdapter(getActivity(), ResultDetailFragment.this, expandableListDetail);
                    expandableListView.setAdapter(expandableListAdapter);

                    ReviewRequest reviewRequest = new ReviewRequest();
                    reviewRequest.readRestaurateurReviewsFromCustomer(restaurateur.getId(), new RequestListener<Collection<Review>>() {
                        @Override
                        public void successResponse(Collection<Review> response) {
                            reviews = new ArrayList<>(response);
                            initComponent();
                            setupRecyclerView(reviewRecycleView);
                            loadMoreData();
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                            reviewRecycleView.setLayoutManager(linearLayoutManager);
                            reviewRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == scrollableReviews.size()-1){
                                        loadMoreData();

                                    }
                                }
                            });

                        }
                        @Override
                        public void errorResponse(RequestException error) {
                            //TODO gestire
                        }
                    });
                }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO gestire
                }
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.result_detail, container, false);
        //TODO Mapping dati ristorante

        reviewRecycleView = rootView.findViewById(R.id.review_complete_card_list);

        layoutMenuContainer = rootView.findViewById(R.id.layoutMenuContainer);
        layoutMenuContainer.setVisibility(View.GONE);
        layoutMenuContainer.setTag(GONE);

        layoutReviewCardListContainer = rootView.findViewById(R.id.layoutReviewCardListContainer);
        layoutReviewCardListContainer.setVisibility(View.GONE);
        layoutReviewCardListContainer.setTag(GONE);

        layoutMenuText = rootView.findViewById(R.id.layoutMenuText);
        layoutMenuText.setOnClickListener(v -> showOrHide(layoutMenuContainer));

        ratingBar = rootView.findViewById(R.id.ratingBarReviewDetail);
        layoutRatingBar = rootView.findViewById(R.id.layoutRatingBar);
        layoutRatingBar.setOnClickListener(v -> showOrHide(layoutReviewCardListContainer));

        layoutCall = rootView.findViewById(R.id.layoutCall);

        textViewAddress = rootView.findViewById(R.id.textViewAddress);
        textViewPhoneNumber = rootView.findViewById(R.id.textViewCall);
        textViewDeliveryCost = rootView.findViewById(R.id.textViewDeliveryCost);
        textViewMinPurchase = rootView.findViewById(R.id.textViewMinPurchase);
        textViewOpenClosed = rootView.findViewById(R.id.textViewOpenClosed);
        textViewReviewNumber = rootView.findViewById(R.id.textViewReviewNumber);

        buttonOrder = rootView.findViewById(R.id.buttonOrderMenu);
        buttonOrder.setOnClickListener(v -> {
            if(getCart().isEmpty()){

            }else{
                Intent goIntent = new Intent(getActivity(), CartActivity.class);
                startActivity(goIntent);
            }
        });

        expandableListView = rootView.findViewById(R.id.expandableMenu);


        return rootView;
    }


    private void initComponent(){
        textViewPhoneNumber.setText(restaurateur.getPhoneNumber());
        textViewAddress.setText(restaurateur.getAddress().getAddress());
        textViewDeliveryCost.setText(String.valueOf(restaurateur.getDeliveryCost()) + " €");
        textViewMinPurchase.setText(String.valueOf((float) restaurateur.getMinPrice()) + " €");

        textViewReviewNumber.setText(String.valueOf(reviews.size()) + " " +getString(R.string.reviews));

        //TODO aggiungere funzione server per media recensioni

        layoutCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + restaurateur.getPhoneNumber()));
            startActivity(intent);
        });
    }

    private void showOrHide(LinearLayout layout){
        String status = (String) layout.getTag();
        if(status.equals(GONE)){
            layout.setVisibility(View.VISIBLE);
            layout.setTag(VISIBLE);
        }else{
            layout.setVisibility(View.GONE);
            layout.setTag(GONE);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        reviewCardRecyclerViewAdapter = new ReviewCardRecyclerViewAdapter(getActivity().getApplicationContext(), scrollableReviews);
        recyclerView.setAdapter(reviewCardRecyclerViewAdapter);
    }

    private void loadMoreData(){
        if(scrollableReviews.size() < reviews.size()){
            scrollableReviews.add(reviews.get(scrollableReviews.size()));
            reviewCardRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public Cart getCart() {
        Cart cart = Cart.getInstance();

        if (cart == null) {
            Cart.init(getContext());
            cart = Cart.getInstance();
        }

        return cart;
    }

    @Override
    public PartialOrder getPartialOrder() {
        PartialOrder partialOrder = this.getCart().getPartialOrderOf(restaurateur);

        if (partialOrder == null) {
            partialOrder = this.getCart().initPartialOrderFor(restaurateur, DeliveryAddress.get());
        }

        return partialOrder;
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
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_complete_card, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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


