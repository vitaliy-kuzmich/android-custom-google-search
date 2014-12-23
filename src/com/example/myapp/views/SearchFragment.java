package com.example.myapp.views;


import android.os.Bundle;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.widget.SearchView;
import com.example.myapp.Const;
import com.example.myapp.R;
import com.example.myapp.model.ImageData;

import java.util.Map;

/**
 * Created by v on 16.11.2014.
 */
public class SearchFragment extends MainFragment<Map<Long, ImageData>> {

    @Override
    protected void reInit() {
        setHasOptionsMenu(true);
        setListAdapter(adapter);
        getListView().setOnScrollListener(listNer);
        getListView().setFocusable(true);
        getListView().setClickable(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        adapter.getData().clear();
        adapter.setServerListSize(0);
        adapter.notifyDataSetInvalidated();
        Bundle req = new Bundle();
        req.putString(Const.KEY_QUERY, query);
        super.startLoader(req);
        return true;
    }

}

