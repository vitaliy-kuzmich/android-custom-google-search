package com.example.myapp.views;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragment;
import com.example.myapp.Const;
import com.example.myapp.R;
import com.example.myapp.adapters.InfiniteListAdapter;
import com.example.myapp.async.ImageProcessor;
import com.example.myapp.listeners.InfiniteScrollListener;
import com.example.myapp.model.ImageData;
import com.example.myapp.storage.DBFav;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by v on 16.11.2014.
 */
public class SearchFragment extends SherlockFragment implements View.OnClickListener {
    private ListView listView;
    private Button btnSearch;
    private EditText txtViewSearch;
    private String txtSearch;
    ImageProcessor imageProcessor;
    DBFav db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.search_fragment, container, false);
        listView = (ListView) root.findViewById(R.id.lviewImages);
        btnSearch = (Button) root.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSearchClick(view);
            }
        });
        txtViewSearch = (EditText) root.findViewById(R.id.txtViewSearch);
        imageProcessor = new ImageProcessor();
        db = new DBFav(this.getActivity());
        db.open();
        imageProcessor.setDb(db);

        return root;
    }

    public void btnSearchClick(View v) {
        final SearchFragment listener = this;
        txtSearch = Uri.encode(txtViewSearch.getText().toString());
        imageProcessor.setQuery(txtSearch);
        listView.setAdapter(new InfiniteListAdapter<ImageData>(getActivity(), new LinkedList<ImageData>()) {

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

        listView.setOnScrollListener(new InfiniteScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                AsyncTask t = new AsyncTask<String, Void, List<ImageData>>() {

                    @Override
                    protected List<ImageData> doInBackground(String... strings) {
                        List<ImageData> res = null;

                        res = imageProcessor.getItemsPart(Const.CACHE_SIZE);

                        return res;
                    }


                    @Override
                    protected void onPostExecute(final List<ImageData> imageDat) {
                        super.onPostExecute(imageDat);
                        ((InfiniteListAdapter) listView.getAdapter()).getDataList().addAll(imageDat);
                        ((InfiniteListAdapter) listView.getAdapter()).notifyDataSetChanged();

                    }
                };
                t.executeOnExecutor(Const.__POOL, null);


            }
        });


    }


    @Override
    public void onClick(View view) {
        if (view instanceof CheckBox) {
            CheckBox cb = (CheckBox) view;
            ImageData dat = (ImageData) view.getTag();
            if (cb.isChecked()) {
                db.add(dat);
            } else {
                db.delRec(dat.getImageId());
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
