package com.example.myapp.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapp.R;
import com.example.myapp.model.ImageData;

import java.util.Map;

/**
 * Created by v on 22.12.2014.
 */
public  class MainAdapter extends BaseAdapter {
    public Map<Long, ImageData> getData() {
        return data;
    }

    protected Map<Long, ImageData> data;
    LayoutInflater mInflater;
    // the serverListSize is the total number of items on the server side,
// which should be returned from the web request results
    protected int serverListSize = -1;

    public int getServerListSize() {
        return serverListSize;
    }

    Context context;
    // Two view types which will be used to determine whether a row should be displaying
// data or a Progressbar
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_ACTIVITY = 1;
    View.OnClickListener chbxListener;
    View.OnClickListener imageListener;

    public View.OnClickListener getChbxListener() {
        return chbxListener;
    }

    public void setChbxListener(View.OnClickListener chbxListener) {
        this.chbxListener = chbxListener;
    }

    public View.OnClickListener getImageListener() {
        return imageListener;
    }

    public void setImageListener(View.OnClickListener imageListener) {
        this.imageListener = imageListener;
    }

    public MainAdapter(Context context, Map<Long, ImageData> c, View.OnClickListener chbxListener, View.OnClickListener imageListener) {
        super();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = c;
        this.context = context;
        this.chbxListener = chbxListener;
        this.imageListener = imageListener;
    }

    public static class ViewHolder {
        public int listPosition;
        public CheckBox box;
        public TextView text;
        public ImageView imageView;
        public ImageData data;
    }


    public void setServerListSize(int serverListSize) {
        this.serverListSize = serverListSize;
    }


    /**
     * disable click events on indicating rows
     */
    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) == VIEW_TYPE_ACTIVITY;
    }

    /**
     * One type is normal data row, the other type is Progressbar
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }


    /**
     * the size of the List plus one, the one is the last row, which displays a Progressbar
     */
    @Override
    public int getCount() {
        return serverListSize < 0 ? data.size() : data.size() + 1;
    }


    /**
     * return the type of the row,
     * the last row indicates the user that the ListView is loading more data
     */
    @Override
    public int getItemViewType(int position) {
        return (position >= data.size()) ? VIEW_TYPE_LOADING
                : VIEW_TYPE_ACTIVITY;
    }

    @Override
    public ImageData getItem(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? data.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? position
                : -1;
    }

    /**
     * returns the correct view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == VIEW_TYPE_LOADING && serverListSize >= 0) {
            return getFooterView(position, convertView, parent);
        }

        View dataRow = convertView;

        if (dataRow == null) {
            dataRow = mInflater.inflate(R.layout.item, parent, false);
        }

        ViewHolder vh = new ViewHolder();
        vh.box = (CheckBox) dataRow.findViewById(R.id.cbBox);
        vh.text = (TextView) dataRow.findViewById(R.id.descr);
        vh.imageView = (ImageView) dataRow.findViewById(R.id.image);
        vh.data = data.get((long) position);
        vh.listPosition = position;
        try {
            vh.imageView.setImageBitmap(vh.data.getImage());
            // vh.text.setText(Html.fromHtml(vh.data.getImageTitle()));
            vh.text.setText(String.valueOf(position));
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        dataRow.setOnClickListener(imageListener);
        dataRow.setBackgroundResource(android.R.drawable.menuitem_background);
        vh.box.setOnClickListener(chbxListener);
        vh.box.setChecked(vh.data.isFavourite());
        dataRow.setTag(vh);
        vh.box.setTag(vh);
        // View dataRow = super.getView(serverListSize < 0 ? position - 1 : position, convertView, parent);
        if (serverListSize < 0)
            return dataRow;
        return dataRow;
    }

    /**
     * returns a View to be displayed in the last row.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getFooterView(int position, View convertView,
                              ViewGroup parent) {
        if (position >= serverListSize && serverListSize > 0) {
            // the ListView has reached the last row
            TextView tvLastRow = new TextView(context);
            tvLastRow.setHint("Reached the last row.");
            tvLastRow.setGravity(Gravity.CENTER);
            return tvLastRow;
        }

        View row = convertView;
        if (row == null) {
            row = mInflater.inflate(
                    R.layout.load, parent, false);
        }

        return row;
    }


    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

}
