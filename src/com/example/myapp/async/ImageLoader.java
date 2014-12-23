package com.example.myapp.async;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import com.example.myapp.Const;
import com.example.myapp.logic.ImageCacheService;
import com.example.myapp.logic.Navigator;
import com.example.myapp.model.ErrorImageData;
import com.example.myapp.model.ImageData;
import com.example.myapp.parse.ResponseDataWrapper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by v on 25.11.2014.
 */
public class ImageLoader extends AsyncTaskLoader<Map<Long, ImageData>> {
    Navigator navigator;
    ImageCacheService cacheService;
    ResponseDataWrapper data;
    LinkedList<ImageData> tmpDataList = new LinkedList<>();
    AtomicLong idCounter = new AtomicLong(-1l);
    ImageData tmpImage;
    String query;

    public ImageLoader(Context context, Bundle args) {
        super(context);

        query = args.getString(Const.KEY_QUERY);
        if (query == null)
            return;
        navigator = new Navigator();
        cacheService = ImageCacheService.getInstance();
        navigator.setQueryString(query);
    }


    @Override
    public Map<Long, ImageData> loadInBackground() {
        int counter = Const.CACHE_SIZE;
        if (query == null)
            return null;
        Map<Long, ImageData> result = new HashMap<>();

        while (counter > 0) {

            while (counter > 0 && tmpDataList.size() > 0) {
                tmpImage = tmpDataList.removeFirst();
                tmpImage.setMaxServerResultSize(navigator.getServerMaxCount());
                result.put(idCounter.incrementAndGet(), tmpImage);

                counter--;
            }
            if (counter > 0) {
                data = navigator.next();

                try {
                    tmpDataList.addAll(cacheService.processMini(data.getResponseData().getResponseData()));
                } catch (Exception ex) {
                    ErrorImageData dat = new ErrorImageData();
                    dat.setErrorText(ex.getLocalizedMessage());
                    result.put((long) Const.ERROR_IN_LOADER, dat);
                    return result;
                }
            }
        }

        return result;
    }


}
