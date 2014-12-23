package com.example.myapp.logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.myapp.Const;
import com.example.myapp.model.ImageData;
import com.example.myapp.parse.Result;
import com.example.myapp.persistance.DBFav;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by v on 16.11.2014.
 */
public class ImageCacheService {
    private ImageCacheService() {
    }

    public DBFav db;
    private static final ImageCacheService INSTANCE = new ImageCacheService();

    public static ImageCacheService getInstance() {
        return INSTANCE;
    }


    public List<ImageData> processMini(List<Result> results) {
        final List<ImageData> res = Collections.synchronizedList(new ArrayList<ImageData>());
        final CountDownLatch latch = new CountDownLatch(results.size());

        for (final Result ress : results) {
            Const.__POOL.execute(new Runnable() {
                @Override
                public void run() {
                    processMini(ress, latch, res);

                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(res);
    }

    public void processMini(final Result result, final CountDownLatch latch, final List<ImageData> out) {
        try {
            final ImageData res = new ImageData();
            res.setImageTitle(result.getTitle());
            res.setImageId(result.getImageId());
            res.setUrl(result.getUrl());
            res.setTmbUrl(result.getTbUrl());
            if (db != null)
                res.setFavourite(db.exists(res.getImageId()));
            final String file = Const.CACHE_FOLDER_MINI + "/" + result.getImageId();

            if (existsMini(result)) {
                res.setImage(compress(new File(file), Const.LIST_IMAGE_WIDTH, Const.LIST_IMAGE_HEIGHT));
                out.add(res);
                latch.countDown();
            } else {
                try {
                    Bitmap tmp = download(new URL(result.getTbUrl()));
                    res.setImage(Bitmap.createScaledBitmap(tmp, Const.LIST_IMAGE_WIDTH, Const.LIST_IMAGE_HEIGHT, true));
                    out.add(res);
                    latch.countDown();
                    save(file, tmp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void fillImagesIfNull(Collection<ImageData> results) {
        final CountDownLatch latch = new CountDownLatch(results.size());

        for (final ImageData res : results) {
            Const.__POOL.execute(new Runnable() {
                @Override
                public void run() {
                    processMini(res, latch);
                    //processMini(res);

                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processMini(ImageData res, CountDownLatch latch) {
        final String file = Const.CACHE_FOLDER_MINI + "/" + res.getImageId();
        if (existsMini(res.getImageId())) {
            res.setImage(compress(new File(file), Const.LIST_IMAGE_WIDTH, Const.LIST_IMAGE_HEIGHT));
            latch.countDown();
        }
        if (res.getImage() == null) {
            Bitmap tmp = null;
            try {
                tmp = download(new URL(res.getTmbUrl()));
                res.setImage(Bitmap.createScaledBitmap(tmp, Const.LIST_IMAGE_WIDTH, Const.LIST_IMAGE_HEIGHT, true));
                latch.countDown();
                save(file, tmp);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    /*private void processMini(ImageData res) {
        final String file = Const.CACHE_FOLDER_MINI + "/" + res.getImageId();
        if (existsMini(res.getImageId())) {
            res.setImage(compress(new File(file), Const.LIST_IMAGE_WIDTH, Const.LIST_IMAGE_HEIGHT));
        }
        if (res.getImage() == null) {
            Bitmap tmp = null;
            try {
                tmp = download(new URL(res.getTmbUrl()));
                res.setImage(Bitmap.createScaledBitmap(tmp, Const.LIST_IMAGE_WIDTH, Const.LIST_IMAGE_HEIGHT, true));
                save(file, tmp);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }*/

    public Bitmap getFullImage(String url, String id) {
        Bitmap res = null;
        final File file = new File(Const.CACHE_FOLDER_FULL + "/" + id);
        if (file.exists()) {
            res = compress(file, Const.DISPLAY_WIDTH, Const.DISPLAY_HEIGHT);
        } else {
            try {
                downloadBig(new URL(url), file);
                res = compress(file, Const.DISPLAY_WIDTH, Const.DISPLAY_HEIGHT);

            } catch (Exception e) {
                e.printStackTrace();
                res = null;
            }

        }
        if (res == null) {
            final File file2 = new File(Const.CACHE_FOLDER_MINI + "/" + id);
            if (file2.exists()) {
                res = compress(file2, Const.DISPLAY_WIDTH, Const.DISPLAY_HEIGHT);
            }
        }
        return res;
    }

    public Bitmap download(URL url) throws Exception {
        Bitmap res = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            res = BitmapFactory.decodeStream(input);
        } finally {
            if (connection != null)
                connection.disconnect();
        }


        return res;
    }


    private void downloadBig(URL u, File file) throws IOException {
        FileUtils.copyURLToFile(u, file);
    }

    private void save(String filename, Bitmap clone) throws Exception {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            clone.compress(Bitmap.CompressFormat.WEBP, 100, out);
            clone.recycle();
        } finally {
            if (out != null)
                out.close();
        }

    }


    public boolean existsMini(Result r) {
        return existsMini(r.getImageId());
    }

    public boolean existsMini(String id) {
        return new File(Const.CACHE_FOLDER_MINI + "/" + id).exists();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap compress(File file, int nWidth,
                                  int nHeight) {

        BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

        btmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), btmapOptions);

        btmapOptions.inSampleSize = calculateInSampleSize(btmapOptions,
                nWidth, nHeight);
        btmapOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), btmapOptions);
    }


}
