package com.example.myapp.parse;

import com.google.api.client.util.Key;

import java.util.List;

public class Cursor {
    @Key("resultCount")
    private String resultCount;
    @Key("pages")
    private List<Page> pages;
    @Key("estimatedResultCount")
    private int estimatedResultCount;
    @Key("currentPageIndex")
    private int currentPageIndex;
    @Key("moreResultsUrl")
    private String moreResultsUrl;
    @Key("searchResultTime")
    private float searchResultTime;

    public String getResultCount() {
        return resultCount;
    }

    public void setResultCount(String resultCount) {
        this.resultCount = resultCount;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public int getEstimatedResultCount() {
        return estimatedResultCount;
    }

    public void setEstimatedResultCount(int estimatedResultCount) {
        this.estimatedResultCount = estimatedResultCount;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public String getMoreResultsUrl() {
        return moreResultsUrl;
    }

    public void setMoreResultsUrl(String moreResultsUrl) {
        this.moreResultsUrl = moreResultsUrl;
    }

    public float getSearchResultTime() {
        return searchResultTime;
    }

    public void setSearchResultTime(float searchResultTime) {
        this.searchResultTime = searchResultTime;
    }
}
