package com.example.myapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import com.example.myapp.Const;
import com.example.myapp.persistance.DBFav;
import com.example.myapp.views.FavouriteFragment;
import com.example.myapp.views.MainFragment;
import com.example.myapp.views.SearchFragment;

/**
 * Created by v on 16.11.2014.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    // private final int PAGES = 2;
    Fragment[] frags = new Fragment[2];

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        MainFragment tmp = null;


        tmp = new SearchFragment();
        tmp.setLoaderId(Const.LOADER_SEARCH_ID);
        frags[0] = tmp;


        tmp = new FavouriteFragment();
        tmp.setLoaderId(Const.LOADER_DB_);
        frags[1] = tmp;


        for (Fragment f : frags) {
            f.setRetainInstance(true);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return frags[position];
    }

    @Override
    public int getCount() {
        return frags.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        Fragment fragment = frags[position];

        if ((obj != null && fragment != null) && !(obj.getClass().getSimpleName().equals(fragment.getClass().getSimpleName()))) {
            destroyItem(container, position, obj);
            return obj;
        } else {
            return obj;
        }
    }
}