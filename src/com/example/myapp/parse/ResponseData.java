package com.example.myapp.parse;

import com.google.api.client.util.Key;

import java.util.List;


public class ResponseData {
    @Key("results")
    private List<Result> responseData;
    @Key("cursor")
    private Cursor cursor;

    public List<Result> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<Result> responseData) {
        this.responseData = responseData;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }
}
