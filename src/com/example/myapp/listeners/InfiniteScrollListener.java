package com.example.myapp.listeners;

import android.support.v4.content.Loader;
import android.widget.AbsListView;
import com.example.myapp.Const;

/**
 * Created by v on 16.11.2014.
 */
public class InfiniteScrollListener implements AbsListView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = Const.CACHE_SIZE / 2;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;
    Loader.ForceLoadContentObserver observer;

    public Loader.ForceLoadContentObserver getObserver() {
        return observer;
    }

    public void setObserver(Loader.ForceLoadContentObserver observer) {
        this.observer = observer;
    }

    public InfiniteScrollListener() {
    }

    public InfiniteScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public InfiniteScrollListener(int visibleThreshold, int startPage) {
        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = startPage;
        this.currentPage = startPage;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            onLoadMore(currentPage + 1, totalItemCount);
            loading = true;
        }
    }

    // Defines the processMini for actually loading more data based on page
    public void onLoadMore(int page, int totalItemsCount) {
        if (observer != null)
            observer.onChange(true);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}