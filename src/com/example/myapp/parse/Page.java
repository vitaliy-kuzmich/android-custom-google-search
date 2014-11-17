package com.example.myapp.parse;

import com.google.api.client.util.Key;

public class Page {
    @Key
    private int start;
    @Key
    private int label;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
