package eu.andlabs.fahrgemeinschaft;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class RideProvider extends ContentProvider {

    protected static final String TAG = "CRASH-TEST";

    private static UriMatcher uriMatcher;
    private static final int QUERIES = 3;
    private static final int PLACES = 2;
    private static final int RIDES = 1;
    private SQLiteOpenHelper db;

    @Override
    public boolean onCreate() {
        db = new DatabaseHelper(getContext());
        return true;
    }

    static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, "rides.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE places ( " + 
                        "_id INTEGER PRIMARY KEY," +
                        " lat INTEGER," + 
                        " lng INTEGER," + 
                        " name TEXT" + 
                        ");");
            Log.d(TAG, "DB CREATED");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    }



    /*
     * Here comes the GAME WORLD:
     * Not much going on so far..
     */


    // CREATE
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (uriMatcher.match(uri)) {
        case PLACES:
            // TODO Level 1
        }
        return null;
    }

    // READ
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // TODO Level 1
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }



    static {
        uriMatcher = new UriMatcher(0);
        uriMatcher.addURI("eu.andlabs.fahrgemeinschaft", "rides", RIDES);
        uriMatcher.addURI("eu.andlabs.fahrgemeinschaft", "places", PLACES);
        uriMatcher.addURI("eu.andlabs.fahrgemeinschaft", "queries", QUERIES);
    }
    
    @Override
    public String getType(Uri uri) {
        return "mymimetype_that_we_ignore_for now";
    }

}

