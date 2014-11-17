package com.example.myapp.async;

import com.example.myapp.parse.Parser;
import com.example.myapp.parse.ResponseDataWrapper;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by v on 16.11.2014.
 */
public class Navigator implements Iterator<ResponseDataWrapper> {
    private int currentPage = -1;
    private String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&searchType=image";
    private final int basicUrlLen;
    private Parser parser = Parser.getInstance();
    private String queryString;
    private StringBuilder buf;
    private ResponseDataWrapper lastResult;


    private int maxPageCount = -1;

    public Navigator(String queryString) {
        this.queryString = queryString;
        buf = new StringBuilder(url);

        buf.append("&q=");
        buf.append(queryString);
        basicUrlLen = buf.length();
    }


    @Override
    public boolean hasNext() {
        return currentPage < maxPageCount;
    }

    @Override
    public ResponseDataWrapper next() {

        buf.replace(basicUrlLen , buf.length(), "");
        buf.append("&start=");
        buf.append(++currentPage);


        try {
            lastResult = parser.parse(buf.toString());
        } catch (IOException noPagesLeft) {
            noPagesLeft.printStackTrace();
            lastResult = null;
        }
        if (lastResult != null) {
            maxPageCount = lastResult.getResponseData().getCursor().getPages().get(lastResult.getResponseData().getCursor().getPages().size() - 1).getStart();
        }
        return lastResult;
    }

    @Override
    @Deprecated
    public void remove() {

    }

    public ResponseDataWrapper getLastResult() {
        if (lastResult == null)
            return next();
        return lastResult;
    }


}
