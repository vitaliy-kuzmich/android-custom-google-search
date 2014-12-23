package com.example.myapp.async;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import com.example.myapp.Const;
import com.example.myapp.logic.ImageCacheService;
import com.example.myapp.model.ImageData;
import com.example.myapp.persistance.DBFav;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by v on 25.11.2014.
 */
public class DBImageLoader extends AsyncTaskLoader<Map<Long, ImageData>> {
    DBFav db;

    public DBImageLoader(Context context, Bundle args, DBFav db) {
        super(context);
        this.db = db;
    }

    ImageData tmpImage;
    Cursor lastDbCursor;
    ImageCacheService cacheService = ImageCacheService.getInstance();
    AtomicLong idCounter = new AtomicLong(-1l);

    @Override
    public Map<Long, ImageData> loadInBackground() {
        int counter = Const.CACHE_SIZE;
        if (lastDbCursor == null) {
            lastDbCursor = db.getAllData();
            lastDbCursor.moveToFirst();
        }


        Map<Long, ImageData> result = new HashMap<>();
        if (lastDbCursor.isClosed())
            return result;
        boolean hasNext = true;
        while (counter > 0 && lastDbCursor.getCount() > 0 && hasNext) {
            if (lastDbCursor.getPosition() == lastDbCursor.getCount()) {
                break;
            }
            tmpImage = new ImageData();

            tmpImage.setFavourite(true);
            tmpImage.setImageId(lastDbCursor.getString(lastDbCursor.getColumnIndex(DBFav.COLUMN_IMG_ID)));
            tmpImage.setMaxServerResultSize(lastDbCursor.getCount());
            tmpImage.setTmbUrl(lastDbCursor.getString(lastDbCursor.getColumnIndex(DBFav.COLUMN_URL_TMB)));
            tmpImage.setUrl(lastDbCursor.getString(lastDbCursor.getColumnIndex(DBFav.COLUMN_URL_FULL)));
            tmpImage.setImageTitle(lastDbCursor.getString(lastDbCursor.getColumnIndex(DBFav.COLUMN_TITLE)));
            tmpImage.setMaxServerResultSize(lastDbCursor.getCount() - 1);
            result.put(idCounter.incrementAndGet(), tmpImage);

            hasNext = lastDbCursor.moveToNext();
            counter--;


        }
        if (!hasNext && result.size() < Const.CACHE_SIZE || !hasNext) {
            lastDbCursor.close();
        }
        cacheService.fillImagesIfNull(result.values());
        return result;
    }
}
