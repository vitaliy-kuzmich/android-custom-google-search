package com.example.myapp.views;

import android.os.Bundle;
import com.example.myapp.model.ImageData;

import java.util.Map;

/**
 * Created by v on 16.11.2014.
 */
public class FavouriteFragment extends MainFragment<Map<Long, ImageData>> {

    @Override
    protected void reInit() {
        adapter.getData().clear();
        adapter.setServerListSize(0);
        setListAdapter(adapter);
        getListView().setOnScrollListener(listNer);
        getListView().setFocusable(true);
        getListView().setClickable(true);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if (menuVisible) {
            adapter.getData().clear();
            adapter.setServerListSize(0);
            adapter.notifyDataSetInvalidated();
            Bundle req = new Bundle();
            super.startLoader(req);
        }
    }
}