package com.example.myapp.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import com.example.myapp.model.ImageData;

import java.io.ByteArrayOutputStream;

/**
 * Created by v on 16.11.2014.
 */
public class DBFav {
    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "mytab";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IMG_ID = "image_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_URL_FULL = "url";
    public static final String COLUMN_URL_TMB = "url_tmb";
    public static final String COLUMN_IMAGE = "image";

    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_IMG_ID + " text, " +
                    COLUMN_URL_FULL + " text, " +
                    COLUMN_URL_TMB + " text, " +
                    COLUMN_TITLE + " text," +
                    COLUMN_IMAGE + " blob" +

                    ");";

    private final Context mCtx;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DBFav(Context ctx) {
        mCtx = ctx;
    }

    public  void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public  void close() {
        if (mDBHelper != null) mDBHelper.close();
    }

    public  Cursor getAllData() {
       return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

    public  Cursor getByImageId(String imageId) {
        return mDB.rawQuery("SELECT " + COLUMN_ID + " FROM " + DB_TABLE + " WHERE " + COLUMN_IMG_ID + " LIKE '%" + imageId + "%'", null);
    }

    public  boolean exists(String imageId) {
        return getByImageId(imageId).moveToFirst();
    }

    public  void add(ImageData dat) {
        ContentValues cv = new ContentValues();
        if (dat.getImage() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            dat.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            cv.put(COLUMN_IMAGE, byteArray);
        }
        cv.put(COLUMN_IMG_ID, dat.getImageId());
        cv.put(COLUMN_URL_FULL, dat.getUrl());
        cv.put(COLUMN_URL_TMB, dat.getTmbUrl());
        cv.put(COLUMN_TITLE, dat.getImageTitle());

        mDB.insert(DB_TABLE, null, cv);
    }

    public  void delRec(long id) {
        mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }

    public  void delRec(String imageId) {
        mDB.delete(DB_TABLE, COLUMN_IMG_ID + " LIKE '" + imageId + "'", null);
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}