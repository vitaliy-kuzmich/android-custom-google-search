package com.example.myapp.model;

import android.graphics.Bitmap;

/**
 * Created by v on 16.11.2014.
 */
public class ImageData {
    private Bitmap image;
    private String imageTitle;
    private String imageId;
    private String url;
    private String tmbUrl;
    private boolean isFavourite;
    private int maxServerResultSize=-1;

    public int getMaxServerResultSize() {
        return maxServerResultSize;
    }

    public void setMaxServerResultSize(int maxServerResultSize) {
        this.maxServerResultSize = maxServerResultSize;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }


    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTmbUrl() {
        return tmbUrl;
    }

    public void setTmbUrl(String tmbUrl) {
        this.tmbUrl = tmbUrl;
    }

}
