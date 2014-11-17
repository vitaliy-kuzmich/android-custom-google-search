package com.example.myapp.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.example.myapp.Const;
import com.example.myapp.model.ImageData;
import com.example.myapp.parse.Result;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by v on 16.11.2014.
 */
public class ImageCacheService {
    public List<ImageData> processMini(List<Result> results) {
        final List<ImageData> res = Collections.synchronizedList(new ArrayList<ImageData>());
        final CountDownLatch latch = new CountDownLatch(results.size());

        for (final Result ress : results) {
            Const.__POOL.execute(new Runnable() {
                @Override
                public void run() {
                    res.add(processMini(ress));
                    latch.countDown();
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return res;
    }

    public ImageData processMini(final Result result) {
        ImageData res = new ImageData();
        res.setImageTitle(result.getTitle());
        res.setImageId(result.getImageId());
        res.setUrl(result.getUrl());
        res.setTmbUrl(result.getTbUrl());


        if (existsMini(result)) {
            res.setImage(BitmapFactory.decodeFile(Const.CACHE_FOLDER_MINI + "/" + result.getImageId()));
            return res;
        }
        try {
            URL mini = new URL(result.getTbUrl());
            FileUtils.copyURLToFile(mini, new File(Const.CACHE_FOLDER_MINI + "/" + result.getImageId()));
            res.setImage(BitmapFactory.decodeFile(Const.CACHE_FOLDER_MINI + "/" + result.getImageId()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public Bitmap getFullImage(String url, String id) {
        File file = new File(Const.CACHE_FOLDER_FULL + "/" + id);
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        try {

            URL mini = new URL(url);
            FileUtils.copyURLToFile(mini, file);
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        file = new File(Const.CACHE_FOLDER_MINI + "/" + id);
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }

        return null;
    }

    public boolean existsMini(Result r) {
        return existsMini(r.getImageId());
    }

    public boolean existsMini(String id) {
        return new File(Const.CACHE_FOLDER_MINI + "/" + id).exists() ;
    }



}
