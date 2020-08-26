package it.uniba.di.sms1920.everit.customer.activities.orders.tab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.WithinAppServiceBinder;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.orders.OrderDetailFragment;
import it.uniba.di.sms1920.everit.utils.models.Order;

public class OrderTabManagerFragment extends Fragment {
    public static final String ARG_ITEM = "item";
    private static final String SAVED_ORDER = "saved.order";
    public static String ARG_ORDER_KEY = "order_item";

    private long orderId;
    private Order order;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private OrderTabPagerAdapter pagerAdapter;

    public OrderTabManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle extras = getArguments();
            if((extras != null) && (extras.containsKey(ARG_ITEM))){
                this.order = extras.getParcelable(ARG_ITEM);
            }
        }
        else if (savedInstanceState.containsKey(SAVED_ORDER)) {
            this.order = savedInstanceState.getParcelable(SAVED_ORDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_tab_manager, container, false);
        this.initUi(view);
        this.setUpTabLayout();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_ORDER, this.order);
    }

    private void initUi(View view) {
        this.tabLayout = view.findViewById(R.id.tabs);
        this.viewPager = view.findViewById(R.id.viewPager);
    }

    private void setUpTabLayout() {
        FragmentActivity parentActivity = getActivity();
        if (parentActivity != null) {
            this.pagerAdapter = new OrderTabPagerAdapter(parentActivity.getSupportFragmentManager(), 0);

            OrderDetailFragment detailFragment = new OrderDetailFragment();
            Bundle detailFragmentArguments = new Bundle();
            detailFragmentArguments.putParcelable(OrderDetailFragment.ARG_ITEM, this.order);
            detailFragment.setArguments(detailFragmentArguments);

            NotesFragment notesFragment = new NotesFragment();
            Bundle notesFragmentArguments = new Bundle();
            notesFragmentArguments.putParcelable(NotesFragment.ARG_ITEM, this.order);
            notesFragment.setArguments(notesFragmentArguments);

            this.pagerAdapter.addFragment(detailFragment, getString(R.string.order_detail));
            this.pagerAdapter.addFragment(notesFragment, getString(R.string.notes));

            this.viewPager.setAdapter(this.pagerAdapter);
            this.tabLayout.setupWithViewPager(this.viewPager);
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
            this.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        }
    }

    public Order getOrder(){
        return order;
    }
}