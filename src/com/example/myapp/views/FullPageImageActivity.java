package com.example.myapp.views;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.myapp.Const;
import com.example.myapp.R;
import com.example.myapp.async.ImageCacheService;

/**
 * Created by v on 16.11.2014.
 */
public class FullPageImageActivity extends SherlockFragmentActivity {
    String imageId;
    String fullUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().hide();
        setContentView(R.layout.full_screen_fragment);

        imageId = getIntent().getExtras().getString("imageId");
        fullUrl = getIntent().getExtras().getString("fullUrl");
        final ImageView imgV = (ImageView) findViewById(R.id.fullScrImage);
        final TextView loading = (TextView) findViewById(R.id.loadText);


        new AsyncTask<String, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(String... strings) {
                return new ImageCacheService().getFullImage(fullUrl, imageId);
            }

            @Override
            protected void onPostExecute(Bitmap imageDatas) {
                super.onPostExecute(imageDatas);
                ((LinearLayout) loading.getParent()).removeView(loading);
                imgV.setImageBitmap(imageDatas);
            }
        }.executeOnExecutor(Const.__POOL);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getActionBar().show();
    }
}
