package com.example.myapp.views;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.example.myapp.Const;
import com.example.myapp.R;
import com.example.myapp.adapters.InfiniteListAdapter;
import com.example.myapp.async.ImageProcessor;
import com.example.myapp.listeners.InfiniteScrollListener;
import com.example.myapp.model.ImageData;
import com.example.myapp.storage.DBFav;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by v on 16.11.2014.
 */
public class FavouriteFragment extends SherlockFragment implements View.OnClickListener {
    ListView lv;
    ImageProcessor imageProcessor;
    DBFav db;
    volatile Cursor cursor = null;

    class AsyncDBLoader extends AsyncTask<String, Void, List<ImageData>> {
        @Override
        protected List<ImageData> doInBackground(String... strings) {
            //TODO:
            ((InfiniteListAdapter) lv.getAdapter()).getDataList().clear();
            List<ImageData> res = new ArrayList<>(Const.CACHE_SIZE);
            ImageData tmp = null;
            if (cursor == null) {
                cursor = db.getAllData();
            }
            byte[] imgArr = null;
            int counter = Const.CACHE_SIZE;
            boolean hasNext = false;
            synchronized (cursor) {
                if (cursor.moveToFirst()) {
                    do {
                        tmp = new ImageData();
                        tmp.setFavourite(true);
                        tmp.setImageId(cursor.getString(cursor.getColumnIndex(DBFav.COLUMN_IMG_ID)));
                        tmp.setImageTitle(cursor.getString(cursor.getColumnIndex(DBFav.COLUMN_TITLE)));
                        tmp.setUrl(cursor.getString(cursor.getColumnIndex(DBFav.COLUMN_URL_FULL)));
                        tmp.setTmbUrl(cursor.getString(cursor.getColumnIndex(DBFav.COLUMN_URL_TMB)));
                        imgArr = cursor.getBlob(cursor.getColumnIndex(DBFav.COLUMN_IMAGE));
                        tmp.setImage(BitmapFactory.decodeByteArray(imgArr, 0, imgArr.length));
                        res.add(tmp);
                        counter--;
                    } while (hasNext = cursor.moveToNext() && counter > 0);
                }
                if (!hasNext) {
                    cursor.close();
                    cursor = null;
                }
            }

            return res;
        }

        @Override
        protected void onPostExecute(final List<ImageData> imageDat) {
            super.onPostExecute(imageDat);

            List<ImageData> exists = ((InfiniteListAdapter) lv.getAdapter()).getDataList();
            if (exists.size() == 0) {
                exists.addAll(imageDat);
            } else
                for (ImageData newData : imageDat) {
                    for (ImageData existing : exists)
                        if (!existing.getImageId().equals(newData.getImageId())) {
                            exists.add(newData);
                        }
                }

            ((InfiniteListAdapter) lv.getAdapter()).notifyDataSetChanged();

        }

    }

    InfiniteScrollListener infiniteScrollListener = new InfiniteScrollListener() {
        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            new AsyncDBLoader().executeOnExecutor(Const.__POOL, null);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.favorites_fragment, container, false);
        lv = (ListView) root.findViewById(R.id.favimages);
        final View.OnClickListener listener = this;


        lv.setAdapter(new InfiniteListAdapter<ImageData>(getActivity(), new LinkedList<ImageData>()) {

            @Override
            public View getDataRow(int position, View convertView, ViewGroup parent) {
                ImageData data = dataList.get(position);
                //Log.d("position", String.valueOf(position));
                ImageView imgV = (ImageView) convertView.findViewById(R.id.image);
                TextView descr = (TextView) convertView.findViewById(R.id.descr);

                CheckBox chbx = (CheckBox) convertView.findViewById(R.id.cbBox);
                chbx.setOnClickListener(listener);
                chbx.setTag(data);
                if (chbx.isChecked() != data.isFavourite())
                    chbx.setChecked(data.isFavourite());
                convertView.setTag(data);
                imgV.setImageBitmap(data.getImage());
                descr.setText(Html.fromHtml(data.getImageTitle()));
                convertView.setClickable(true);
                convertView.setFocusable(true);
                convertView.setBackgroundResource(android.R.drawable.menuitem_background);
                convertView.setOnClickListener(listener);

                return convertView;
            }
        });

        lv.setOnScrollListener(infiniteScrollListener);


        imageProcessor = new ImageProcessor();
        db = new DBFav(this.getActivity());
        db.open();
        imageProcessor.setDb(db);


        return root;
    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            Log.d(getClass().getName(), "active!!");
            new AsyncDBLoader().executeOnExecutor(Const.__POOL, null);
        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof CheckBox) {
            CheckBox cb = (CheckBox) view;
            ImageData dat = (ImageData) view.getTag();
            if (cb.isChecked()) {
            } else {
                db.delRec(dat.getImageId());
                List<ImageData> lst = ((InfiniteListAdapter) lv.getAdapter()).getDataList();
                Iterator<ImageData> iter = lst.iterator();
                while (iter.hasNext()) {
                    if (iter.next().getImageId().equals(dat.getImageId())) {
                        iter.remove();

                        break;
                    }
                }
                ((InfiniteListAdapter) lv.getAdapter()).notifyDataSetChanged();

            }
            dat.setFavourite(cb.isChecked());

        } else {
            Intent i = new Intent(getActivity(), FullPageImageActivity.class);
            ImageData data = (ImageData) view.getTag();
            i.putExtra("imageId", data.getImageId());
            i.putExtra("fullUrl", data.getUrl());
            getActivity().startActivity(i);

        }
    }
}
