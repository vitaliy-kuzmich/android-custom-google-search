package com.example.myapp.views;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Display;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.example.myapp.Const;
import com.example.myapp.R;
import com.example.myapp.adapters.ViewPagerAdapter;

/**
 * Created by v on 16.11.2014.
 */
public class Main extends SherlockFragmentActivity implements ActionBar.TabListener {
    private ActionBar actionBar;
    private ViewPager viewPager;

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long time = System.currentTimeMillis();
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
        setScreenSize();
        time = System.currentTimeMillis() - time;
        if (time > 0)
            time = time;
    }

    private void setScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Const.DISPLAY_WIDTH = size.x;
        Const.DISPLAY_HEIGHT = size.y;
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Const.isEnabled.set(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }


}
