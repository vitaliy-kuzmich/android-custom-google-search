package com.example.myapp;


import android.os.Environment;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by v on 16.11.2014.
 */
public class Const {
    public static final String ACTIVE_TAB = "activeTab";
    public static final int CACHE_SIZE = 10;
    public static final String CACHE_FOLDER_MINI = new File(Environment.getExternalStorageDirectory().getPath()).getAbsolutePath() + "/app-images-cgs/mini";
    public static final String CACHE_FOLDER_FULL = new File(Environment.getExternalStorageDirectory().getPath()).getAbsolutePath() + "/app-images-cgs/full";
    public static final ExecutorService __POOL = Executors.newCachedThreadPool();

    static {
        File f = new File(CACHE_FOLDER_MINI);
        f.mkdirs();
        f = new File(CACHE_FOLDER_FULL);
        f.mkdirs();

    }
}
