package com.example.myapp;


import android.os.Environment;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by v on 16.11.2014.
 */
public class Const {
    public static final AtomicBoolean isEnabled = new AtomicBoolean(true);
    public static final String KEY_FULL_URL = "url";
    public static final String KEY_QUERY = "q";
    public static final String KEY_IMAGE_ID = "imageId";
    public static final int CACHE_SIZE = 10;
    public static final String CACHE_FOLDER_MINI = new File(Environment.getExternalStorageDirectory().getPath()).getAbsolutePath() + "/app-images-cgs/mini";
    public static final String CACHE_FOLDER_FULL = new File(Environment.getExternalStorageDirectory().getPath()).getAbsolutePath() + "/app-images-cgs/full";
    public static final ExecutorService __POOL = Executors.newFixedThreadPool(CACHE_SIZE);
    public static final int LOADER_SEARCH_ID = 0;
    public static final int LOADER_DB_ = 1;
    public static final int LIST_IMAGE_WIDTH = 80;
    public static final int LIST_IMAGE_HEIGHT = 60;
    public static final int RESULT_COUNT = 8;
    public static final int ERROR_IN_LOADER = Integer.MIN_VALUE;
    public static int DISPLAY_WIDTH;
    public static int DISPLAY_HEIGHT;

    static {
        File f = new File(CACHE_FOLDER_MINI);
        f.mkdirs();
        f = new File(CACHE_FOLDER_FULL);
        f.mkdirs();

    }

}
