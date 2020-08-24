package it.uniba.di.sms1920.everit.rider.activities.works;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class DeliveriesPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTileList = new ArrayList<>();

    public DeliveriesPagerAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTileList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTileList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}