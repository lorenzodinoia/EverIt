package it.uniba.di.sms1920.everit.customer.activities.results;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.InfoFragment;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.MenuFragment;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.ResultTabPagerAdapter;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.ReviewListFragment;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.Review;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.ReviewRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class ResultDetailActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "item_id";
    private static final String SAVED_RESTAURATEUR = "saved.restaurateur";
    private static final String SAVED_REVIEWS = "saved.reviews";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ResultTabPagerAdapter pagerAdapter;

    private long restaurateurId;
    private Restaurateur restaurateur;
    private ArrayList<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        /*
        if (restaurateur != null) {
            toolbar.setTitle(restaurateur.getShopName());
        }

         */
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if ((extras != null) && (extras.containsKey(ARG_ITEM_ID))) {
                this.restaurateurId = extras.getLong(ARG_ITEM_ID);
            }
            /*
            RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
            restaurateurRequest.read(resultId, new RequestListener<Restaurateur>() {
                @Override
                public void successResponse(Restaurateur response) {
                    restaurateur = response;

                    ReviewRequest reviewRequest = new ReviewRequest();
                    reviewRequest.readRestaurateurReviewsFromCustomer(restaurateur.getId(), new RequestListener<Collection<Review>>() {
                        @Override
                        public void successResponse(Collection<Review> response) {
                            reviews = new ArrayList<>(response);

                            tabLayout = findViewById(R.id.tabs);
                            viewPager = findViewById(R.id.viewPager);

                            pagerAdapter = new ResultTabPagerAdapter(getSupportFragmentManager(), 0);
                            InfoFragment infoFragment = new InfoFragment();
                            MenuFragment menuFragment = new MenuFragment();
                            ReviewListFragment reviewListFragment = new ReviewListFragment();

                            pagerAdapter.addFragment(infoFragment, getString(R.string.info));
                            pagerAdapter.addFragment(menuFragment, getString(R.string.menu));
                            pagerAdapter.addFragment(reviewListFragment, getString(R.string.reviews));

                            viewPager.setAdapter(pagerAdapter);
                            tabLayout.setupWithViewPager(viewPager);
                            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {
                                    viewPager.setCurrentItem(tab.getPosition());
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {

                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {

                                }
                            });
                            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


                        }

                        @Override
                        public void errorResponse(RequestException error) {
                            promptErrorMessage(error.getMessage());
                        }
                    });
                }

                @Override
                public void errorResponse(RequestException error) {
                    promptErrorMessage(error.getMessage());
                }

                 });
            */
        }
        else if ((savedInstanceState.containsKey(SAVED_RESTAURATEUR)) && savedInstanceState.containsKey(SAVED_REVIEWS)) {
            this.restaurateur = savedInstanceState.getParcelable(SAVED_RESTAURATEUR);
            this.reviews = savedInstanceState.getParcelableArrayList(SAVED_REVIEWS);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ((this.restaurateurId != 0) && ((this.restaurateur == null) || (this.reviews == null))) { //Needs server request
            this.loadData();
        }
        else {
            this.setUpTabLayout();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_RESTAURATEUR, this.restaurateur);
        outState.putParcelableArrayList(SAVED_REVIEWS, this.reviews);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
        restaurateurRequest.read(this.restaurateurId, new RequestListener<Restaurateur>() {
            @Override
            public void successResponse(Restaurateur response) {
                restaurateur = response;

                ReviewRequest reviewRequest = new ReviewRequest();
                reviewRequest.readRestaurateurReviewsFromCustomer(restaurateur.getId(), new RequestListener<Collection<Review>>() {
                    @Override
                    public void successResponse(Collection<Review> response) {
                        reviews = new ArrayList<>(response);
                        setUpTabLayout(); //All data are loaded then set up the tab layout
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        promptErrorMessage(error.getMessage());
                    }
                });
            }

            @Override
            public void errorResponse(RequestException error) {
                promptErrorMessage(error.getMessage());
            }
        });
    }

    private void setUpTabLayout() {
        this.tabLayout = findViewById(R.id.tabs);
        this.viewPager = findViewById(R.id.viewPager);

        this.pagerAdapter = new ResultTabPagerAdapter(getSupportFragmentManager(), 0);

        InfoFragment infoFragment = new InfoFragment();
        Bundle infoFragmentArguments = new Bundle();
        infoFragmentArguments.putParcelable(InfoFragment.ARG_ITEM, this.restaurateur);
        infoFragment.setArguments(infoFragmentArguments);

        MenuFragment menuFragment = new MenuFragment();
        Bundle menuFragmentArguments = new Bundle();
        menuFragmentArguments.putParcelable(MenuFragment.ARG_ITEM, this.restaurateur);
        menuFragment.setArguments(menuFragmentArguments);

        ReviewListFragment reviewListFragment = new ReviewListFragment();
        Bundle reviewListFragmentArguments = new Bundle();
        reviewListFragmentArguments.putParcelable(ReviewListFragment.ARG_ITEM_RESTAURATEUR, this.restaurateur);
        reviewListFragmentArguments.putParcelableArrayList(ReviewListFragment.ARG_ITEM_REVIEWS, this.reviews);
        reviewListFragment.setArguments(reviewListFragmentArguments);

        this.pagerAdapter.addFragment(infoFragment, getString(R.string.info));
        this.pagerAdapter.addFragment(menuFragment, getString(R.string.menu));
        this.pagerAdapter.addFragment(reviewListFragment, getString(R.string.reviews));

        this.viewPager.setAdapter(pagerAdapter);
        this.tabLayout.setupWithViewPager(viewPager);
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public Restaurateur passRestaurateur(){
        return  this.restaurateur;
    }

    public ArrayList<Review> passRestaurateurReviews() {
        return this.reviews;
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
}
