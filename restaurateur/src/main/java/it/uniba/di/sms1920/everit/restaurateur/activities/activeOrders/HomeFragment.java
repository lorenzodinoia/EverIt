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
    private BaseActivity parentBaseActivity;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof BaseActivity) {
            this.parentBaseActivity = (BaseActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.initUi(view);
        return view;
    }

    private void initUi(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        ViewPager viewPager = view.findViewById(R.id.view_pager);

        OrderListFragment notConfirmedFragment = new OrderListFragment();
        Bundle notConfirmedFragmentArguments = new Bundle();
        notConfirmedFragmentArguments.putInt(OrderListFragment.ARG_ORDER_TYPE, OrderListFragment.ORDER_TYPE_NOT_CONFIRMED);
        notConfirmedFragment.setArguments(notConfirmedFragmentArguments);

        OrderListFragment toDoFragment = new OrderListFragment();
        Bundle toDoFragmentArguments = new Bundle();
        toDoFragmentArguments.putInt(OrderListFragment.ARG_ORDER_TYPE, OrderListFragment.ORDER_TYPE_TO_DO);
        toDoFragment.setArguments(toDoFragmentArguments);

        OrderTabAdapter adapter = new OrderTabAdapter(parentBaseActivity.getSupportFragmentManager(), 0);
        adapter.addFragment(notConfirmedFragment, getString(R.string.not_confirmed));
        adapter.addFragment(toDoFragment, getString(R.string.to_do));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
