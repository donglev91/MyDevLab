package com.example.lvd.myapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;

/**
 * Created by lvd on 29/07/2016.
 */
public class RatingMemoryProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.lvd.provider.rating";
    static final String URL = "content://" + PROVIDER_NAME + "/memories";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String ITEM = "item";
    static final String RATING = "rating";

    private static HashMap<String, String> MEMORIES_ITEM_RATING_MAP;

    static final int MEMORIES = 1;
    static final int MEMORY_ID = 2;
    static final int MEMORY_ITEM = 3;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "memories", MEMORIES);
        uriMatcher.addURI(PROVIDER_NAME, "memories/#", MEMORY_ID);
        uriMatcher.addURI(PROVIDER_NAME, "memories/#", MEMORY_ITEM);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Memory";
    static final String RATING_TABLE_NAME = "memories";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE IF NOT EXISTS " + RATING_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " item INTEGER NOT NULL, " +
                    " rating INTEGER NOT NULL);";

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(RATING_TABLE_NAME);

        switch (uriMatcher.match(uri)){
            case MEMORIES:
                qb.setProjectionMap(MEMORIES_ITEM_RATING_MAP);
                break;
            case MEMORY_ID:
                qb.appendWhere(selection);
                break;
            case MEMORY_ITEM:
                qb.appendWhere(selection);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == ""){
            sortOrder = ITEM;
        }

        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId = db.insert(RATING_TABLE_NAME, "", values);

        if (rowId > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI,rowId);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Fail to add a record into" + uri);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] contentValues){
        long rowId = 0;
        int numberInserted = 0;
        db.beginTransaction();
        try {
            for (ContentValues cv: contentValues){
                rowId = db.insertOrThrow(RATING_TABLE_NAME, null, cv);
                if (rowId <= 0){
                    throw new SQLException("Fail to insert row into " + uri);
                }
            }

            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
            numberInserted = contentValues.length;
        } finally {
            db.endTransaction();
        }
        return numberInserted;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case MEMORIES:
                count = db.update(RATING_TABLE_NAME, values, selection, selectionArgs);
                break;
            case MEMORY_ID:
                count = db.update(RATING_TABLE_NAME, values, selection, selectionArgs);
                break;
            case MEMORY_ITEM:
                count = db.update(RATING_TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        return count;
    }

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //Last point internal file
            final String LASTPOINTFILE = "last_point_file";
            db.execSQL(CREATE_DB_TABLE);
            if (isFileExisted(LASTPOINTFILE)){
                db.execSQL("insert into " + CREATE_DB_TABLE + "(" + _ID + ","
                        + ITEM + "," + RATING + ") values(1,1,0)");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + RATING_TABLE_NAME);
            onCreate(db);
        }

        public boolean isFileExisted(String fname){
            File file = getContext().getFileStreamPath(fname);
            return file.exists();
        }
    }
    private class RatingMemory{
        public int id;
        public int item;
        public int rating;

        public RatingMemory(int id, int item, int rating) {
            this.id = id;
            this.item = item;
            this.rating = rating;
        }

        public RatingMemory(int item, int rating) {
            this.item = item;
            this.rating = rating;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getItem() {
            return item;
        }

        public void setItem(int item) {
            this.item = item;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }
    }
}
