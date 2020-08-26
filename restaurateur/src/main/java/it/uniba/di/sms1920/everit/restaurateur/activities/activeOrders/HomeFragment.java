package it.uniba.di.sms1920.everit.restaurateur.activities.activeOrders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.BaseActivity;


public class HomeFragment extends Fragment {

    private BaseActivity mParent;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public HomeFragment() {
        // Required empty public constructor
    }

    //TODO non funziona ancora, chiedere a lorenzo

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.view_pager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        OrderTabAdapter adapter = new OrderTabAdapter(mParent.getSupportFragmentManager(), 0);
        OrderListFragment fragment1 = new OrderListFragment();
        fragment1.setIndex(0);
        OrderListFragment fragment2 = new OrderListFragment();
        fragment2.setIndex(1);

        adapter.addFragment(fragment1, getString(R.string.not_confirmed));
        adapter.addFragment(fragment2, getString(R.string.to_do));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                OrderListFragment orderListFragment = (OrderListFragment) adapter.getItem(tab.getPosition());
                orderListFragment.updateData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof BaseActivity){
            mParent = (BaseActivity) context;
        }
    }
}
