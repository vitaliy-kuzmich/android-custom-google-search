package com.example.myapp.views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.example.myapp.Const;
import com.example.myapp.R;
import com.example.myapp.logic.ImageCacheService;

/**
 * Created by v on 18.11.2014.
 */
public class FullFragment extends SherlockFragment {
    String imageId;
    String fullUrl;
    ImageView imgV;
    TextView loading;
    ViewGroup root;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        root = (ViewGroup) inflater.inflate(R.layout.full_screen_fragment, container, false);

        imgV = (ImageView) root.findViewById(R.id.fullScrImage);
        loading = (TextView) root.findViewById(R.id.loadText);

        imageId = getArguments().getString("imageId");
        fullUrl = getArguments().getString("url");
        new AsyncTask<String, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap res = null;
                try {
                    res = ImageCacheService.getInstance().getFullImage(fullUrl, imageId);
                } catch (Exception Ex) {
                    Ex.printStackTrace();
                }
                return res;
            }

            @Override
            protected void onPostExecute(Bitmap imageDatas) {
                super.onPostExecute(imageDatas);
                if (imageDatas != null) {
                    ((ViewGroup) loading.getParent()).removeView(loading);
                    imgV.setImageBitmap(imageDatas);
                }
            }
        }.executeOnExecutor(Const.__POOL);
        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(getClass().getName(), "onDetach");
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getActionBar().show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getActionBar().hide();
    }


}
