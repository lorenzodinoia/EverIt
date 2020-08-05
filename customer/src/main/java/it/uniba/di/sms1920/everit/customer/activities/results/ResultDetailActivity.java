package it.uniba.di.sms1920.everit.customer.activities.results;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.view.MenuItem;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.InfoFragment;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.MenuFragment;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.ResultTabPagerAdapter;
import it.uniba.di.sms1920.everit.customer.activities.results.Tabs.ReviewListFragment;
import it.uniba.di.sms1920.everit.utils.models.ProductCategory;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.Review;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.ReviewRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class ResultDetailActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "item_id";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ResultTabPagerAdapter pagerAdapter;

    private Restaurateur restaurateur;
    private List<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);

        long resultId = getIntent().getLongExtra(ARG_ITEM_ID, 0);
        this.restaurateur = ResultListActivity.getResultById(resultId);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        if (restaurateur != null) {
            toolbar.setTitle(restaurateur.getShopName());
        }
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (savedInstanceState == null) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, ResultListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public Restaurateur passRestaurateur(){
        return  this.restaurateur;
    }

    public List<Review> passRestaurateurReviews() {
        return this.reviews;
    }



}
