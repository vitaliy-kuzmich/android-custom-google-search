package com.example.myapp.parse;


import com.google.api.client.util.Key;

import java.net.URL;

public class Result {
    @Key
    private String GsearchResultClass;
    @Key
    private int width;
    @Key
    private int height;
    @Key
    private String imageId;
    @Key
    private int tbWidth;
    @Key
    private int tbHeight;
    @Key
    private String unescapedUrl;
    @Key
    private String url;
    @Key
    private String visibleUrl;
    @Key
    private String title;
    @Key
    private String titleNoFormatting;
    @Key
    private String originalContextUrl;
    @Key
    private String content;
    @Key
    private String contentNoFormatting;
    @Key
    private String tbUrl;


    public String getGsearchResultClass() {
        return GsearchResultClass;
    }

    public void setGsearchResultClass(String gsearchResultClass) {
        GsearchResultClass = gsearchResultClass;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public int getTbWidth() {
        return tbWidth;
    }

    public void setTbWidth(int tbWidth) {
        this.tbWidth = tbWidth;
    }

    public int getTbHeight() {
        return tbHeight;
    }

    public void setTbHeight(int tbHeight) {
        this.tbHeight = tbHeight;
    }

    public String getUnescapedUrl() {
        return unescapedUrl;
    }

    public void setUnescapedUrl(String unescapedUrl) {
        this.unescapedUrl = unescapedUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVisibleUrl() {
        return visibleUrl;
    }

    public void setVisibleUrl(String visibleUrl) {
        this.visibleUrl = visibleUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleNoFormatting() {
        return titleNoFormatting;
    }

    public void setTitleNoFormatting(String titleNoFormatting) {
        this.titleNoFormatting = titleNoFormatting;
    }

    public String getOriginalContextUrl() {
        return originalContextUrl;
    }

    public void setOriginalContextUrl(String originalContextUrl) {
        this.originalContextUrl = originalContextUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentNoFormatting() {
        return contentNoFormatting;
    }

    public void setContentNoFormatting(String contentNoFormatting) {
        this.contentNoFormatting = contentNoFormatting;
    }

    public String getTbUrl() {
        return tbUrl;
    }

    public void setTbUrl(String tbUrl) {
        this.tbUrl = tbUrl;
    }
}
