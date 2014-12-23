package com.example.myapp.logic;

import com.example.myapp.Const;
import com.example.myapp.parse.Parser;
import com.example.myapp.parse.ResponseDataWrapper;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by v on 16.11.2014.
 */
public class Navigator implements Iterator<ResponseDataWrapper>  {
    private int currentPage = -1;
    private String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=" + Const.RESULT_COUNT + "&searchType=image";
    private int basicUrlLen;
    private Parser parser = Parser.getInstance();
    private String queryString;

    public String getQueryString() {
        return queryString;
    }

    private StringBuilder buf;
    private ResponseDataWrapper lastResult;


    private int maxPageCount = -1;


    public void setQueryString(String queryString) {
        this.queryString = queryString;
        reset();
    }


    @Override
    public boolean hasNext() {
        return currentPage < maxPageCount;
    }

    @Override
    public ResponseDataWrapper next() {

        buf.replace(basicUrlLen, buf.length(), "");
        buf.append("&start=");
        buf.append(++currentPage);


        try {
            lastResult = parser.parse(buf.toString());
        } catch (IOException noPagesLeft) {
            noPagesLeft.printStackTrace();
            lastResult = null;
        }
        if (lastResult != null) {
            maxPageCount = lastResult.getResponseData().getCursor().getPages().get(lastResult.getResponseData().getCursor().getPages().size() - 1).getStart()*Const.RESULT_COUNT;
        }
        return lastResult;
    }

    public int getServerMaxCount() {
        return maxPageCount;
    }

    @Override
    @Deprecated
    public void remove() {

    }

    public void reset() {
        buf = new StringBuilder(url);
        buf.append("&q=");
        buf.append(queryString);
        basicUrlLen = buf.length();
        lastResult = null;
        currentPage = -1;
        maxPageCount = -1;
    }




}
