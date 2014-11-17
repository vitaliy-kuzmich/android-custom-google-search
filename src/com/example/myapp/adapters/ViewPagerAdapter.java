package com.example.myapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.myapp.views.FavouriteFragment;
import com.example.myapp.views.SearchFragment;

/**
 * Created by v on 16.11.2014.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final int PAGES = 2;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment res = null;
        if (position == 0) {
            res = new SearchFragment();
        } else if (position == 1) {
            res = new FavouriteFragment();
        } else
            throw new IllegalArgumentException("The item position should be less or equal to:" + PAGES);
        res.setRetainInstance(true);
        return res;
    }

    @Override
    public int getCount() {
        return PAGES;
    }
}