package com.example.myapp.views;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.widget.SearchView;
import com.example.myapp.Const;
import com.example.myapp.R;
import com.example.myapp.adapters.MainAdapter;
import com.example.myapp.async.DBImageLoader;
import com.example.myapp.async.ImageLoader;
import com.example.myapp.listeners.InfiniteScrollListener;
import com.example.myapp.logic.ImageCacheService;
import com.example.myapp.model.ErrorImageData;
import com.example.myapp.model.ImageData;
import com.example.myapp.persistance.DBFav;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by v on 22.12.2014.
 */
public abstract class MainFragment<T> extends SherlockListFragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Map<Long, ImageData>>, SearchView.OnQueryTextListener {
    protected static DBFav db;
    protected MainAdapter adapter;
    protected Loader.ForceLoadContentObserver observer;
    protected int loaderId = -1;

    public int getLoaderId() {
        return loaderId;
    }

    public void setLoaderId(int loaderId) {
        this.loaderId = loaderId;
    }

    InfiniteScrollListener listNer = new InfiniteScrollListener();

    @Override
    public Loader<Map<Long, ImageData>> onCreateLoader(int id, Bundle args) {
        Loader loader = null;
        switch (id) {
            case Const.LOADER_DB_:
                loader = new DBImageLoader(getSherlockActivity(), args, db);
                break;
            default:
                loader = new ImageLoader(getSherlockActivity(), args);
                break;
        }

        observer = loader.new ForceLoadContentObserver();
        listNer.setObserver(observer);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Map<Long, ImageData>> loader, Map<Long, ImageData> data) {
        if (data == null)
            return;
        if (data.get(Const.ERROR_IN_LOADER) != null) {
            Toast.makeText(getSherlockActivity(), ((ErrorImageData) data.get(Const.ERROR_IN_LOADER)).getErrorText(), Toast.LENGTH_LONG);
            data.clear();
        } else if (data.size() > 0 && data.entrySet().iterator().next().getValue().getMaxServerResultSize() <= data.size()) {
            adapter.getData().putAll(data);
            adapter.setServerListSize(data.size());
            adapter.notifyDataSetChanged();
            data.clear();
            observer = null;

        } else if (data.size() > 0) {
            adapter.getData().putAll(data);
            adapter.setServerListSize(data.entrySet().iterator().next().getValue().getMaxServerResultSize());
            adapter.notifyDataSetChanged();
            data.clear();
        } else if (data.size() == 0 && adapter.getData().size() == 0) {
            adapter.setServerListSize(-1);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getLoaderId() == Const.LOADER_DB_)
            return inflater.inflate(R.layout.favourite, container, false);
        else return inflater.inflate(R.layout.search, container, false);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        observer = null;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (adapter != null) {
            reInit();

            return;
        }

        adapter = createNewAdapter();
        reInit();
        if (db == null) {
            db = new DBFav(getSherlockActivity());
            db.open();
            ImageCacheService.getInstance().db = db;
        }
    }

    protected MainAdapter createNewAdapter() {
        return new MainAdapter(getSherlockActivity(), new HashMap<Long, ImageData>(), this, this);
    }

    protected abstract void reInit();


    @Override
    public void onClick(View view) {
        MainAdapter.ViewHolder h = (MainAdapter.ViewHolder) view.getTag();

        if (view.getId() == R.id.cbBox) {
            h.data.setFavourite(!h.data.isFavourite());
            h.box.setChecked(h.data.isFavourite());
            if (h.data.isFavourite()) {
                db.add(h.data);

            } else {
                db.delRec(h.data.getImageId());
            }

        } else {
            showImage(h.data);
        }
    }


    public int getContainerViewId() {
        if (getLoaderId() == Const.LOADER_DB_) {
            return R.id.fav_fragment;
        } else return R.id.search_fragment;
    }

    public void showImage(ImageData dat) {
        FragmentTransaction trans = getSherlockActivity().getSupportFragmentManager()
                .beginTransaction();
        FullFragment f = new FullFragment();
        Bundle b = new Bundle();
        b.putString(Const.KEY_IMAGE_ID, dat.getImageId());
        b.putString(Const.KEY_FULL_URL, dat.getUrl());
        f.setArguments(b);
        trans.replace(getContainerViewId(), f);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        trans.addToBackStack(null);
        trans.commit();
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    protected void startLoader(Bundle req) {
        Loader l = null;

        if (observer == null) {
            l = getSherlockActivity().getSupportLoaderManager().initLoader(loaderId, req, this);
			  if (loaderId == Const.LOADER_DB_)
				l.forceLoad();
        } else {
            l = getSherlockActivity().getSupportLoaderManager().restartLoader(loaderId, req, this);
            if (!l.isStarted() && l.isReset()) {
                getSherlockActivity().getSupportLoaderManager().destroyLoader(loaderId);
                l = getSherlockActivity().getSupportLoaderManager().initLoader(loaderId, req, this);
                l.forceLoad();
            } else {
                int size = adapter.getData().size();
                l.deliverResult(adapter.getData());
                if (adapter.getData().size() - size <= 0) {
                    l.forceLoad();
                }
            }
        }
    }
}
