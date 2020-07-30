package it.uniba.di.sms1920.everit.customer.activities.results.Tabs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ResultTabPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ResultTabPagerAdapter(@NonNull FragmentManager fragmentManager, int numbTabs) {
        super(fragmentManager, numbTabs);
    }

    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return mFragmentList.get(position);
        /**
        switch (position) {
            case 0:
                return new InfoFragment();

            case 1:
                return new MenuFragment();

            case 2:
                return new ReviewListFragment();

            default:
                return null;
        }
         */

    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
