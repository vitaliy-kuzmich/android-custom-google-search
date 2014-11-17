package com.example.myapp.async;


import com.example.myapp.Const;
import com.example.myapp.model.ImageData;
import com.example.myapp.parse.ResponseDataWrapper;
import com.example.myapp.parse.Result;
import com.example.myapp.storage.DBFav;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by v on 16.11.2014.
 */
public class ImageProcessor {
    private Navigator navigator;
    private String query;
    private ImageCacheService imageCacheService;
    private DBFav db;

    public DBFav getDb() {
        return db;
    }

    public void setDb(DBFav db) {
        this.db = db;
    }

    LinkedList<Result> parseCache = new LinkedList<>();
    LinkedList<ImageData> fileCache = new LinkedList<>();
    private static ReentrantReadWriteLock parseLock = new ReentrantReadWriteLock();
    private static ReentrantReadWriteLock fileLock = new ReentrantReadWriteLock();
    private static ReentrantReadWriteLock navigatorLock = new ReentrantReadWriteLock();

    public Navigator getNavigator() {
        return navigator;
    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        navigator = new Navigator(query);
        imageCacheService = new ImageCacheService();
        this.query = query;
        parseCache.clear();
        fileCache.clear();
        preLoadParseCache();
        // preLoadFileCache();
    }


    public List<ImageData> getItemsPart(int count) {
        List<ImageData> result = new ArrayList<>(count);
        List<Result> currentResult;
        ResponseDataWrapper data = null;
        ImageData tmp = null;
        int counter = count;
        while (counter > 0) {
            if (fileCache.size() > 0) {
                while (fileCache.size() > 0 && result.size() < count) {
                    tmp = fileCache.removeFirst();
                    tmp.setFavourite(db.exists(tmp.getImageId()));
                    result.add(tmp);
                    counter--;
                }
                if (result.size() == count)
                    return result;
            }

            if (parseCache.size() > 0) {
                int howManyLeft = count - result.size();
                while (howManyLeft > 0 && parseCache.size() > 0) {
                    fileCache.add(imageCacheService.processMini(parseCache.removeFirst()));
                    howManyLeft--;
                }
                continue;
            }

            data = navigator.next();
            if (data == null)
                return result;
            parseCache.addAll(data.getResponseData().getResponseData());
        }
        int parseCacheFactor = count / 4;
        parseCacheFactor = parseCacheFactor == 0 ? 1 : parseCacheFactor;

        for (int i = 0; i < parseCacheFactor; i++) {
            preLoadParseCache();
        }


        return result;

    }

    public void preLoadParseCache() {
        Const.__POOL.execute(new Runnable() {
            @Override
            public void run() {

                ResponseDataWrapper tmp = null;
                navigatorLock.writeLock().lock();
                tmp = navigator.next();
                navigatorLock.writeLock().unlock();

                parseLock.writeLock().lock();
                parseCache.addAll(tmp.getResponseData().getResponseData());
                parseLock.writeLock().unlock();


            }
        });
    }

    public void preLoadFileCache() {
        Const.__POOL.execute(new Runnable() {
            @Override
            public void run() {
                List<ImageData> res = null;

                parseLock.readLock().lock();
                res = imageCacheService.processMini(parseCache);
                parseLock.readLock().unlock();

                fileLock.writeLock().lock();
                fileCache.addAll(res);
                fileLock.writeLock().unlock();


            }
        });
    }

}