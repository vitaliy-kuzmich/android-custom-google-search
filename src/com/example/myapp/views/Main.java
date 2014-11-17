package com.example.myapp.views;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.myapp.R;
import com.example.myapp.adapters.ViewPagerAdapter;

/**
 * Created by v on 16.11.2014.
 */
public class Main extends SherlockFragmentActivity implements ActionBar.TabListener {
    private ActionBar actionBar;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                actionBar.setSelectedNavigationItem(position);
            }
        });
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        addActionBarTabs();

    }


    private void addActionBarTabs() {
        actionBar = getSupportActionBar();
        String[] tabs = {getResources().getString(R.string.main_fragment_title), getResources().getString(R.string.favourites_fragment_title)};
        for (String tabTitle : tabs) {
            ActionBar.Tab tab = actionBar.newTab().setText(tabTitle)
                    .setTabListener(this);
            actionBar.addTab(tab);
        }
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }


}
