package eu.andlabs.fahrgemeinschaft;

import java.util.Date;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class RideProvider extends ContentProvider {

	public static final Uri URI_CONTENT_RIDES = Uri.parse("content://eu.andlabs.fahrgemeinschaft/rides");
	public static final Uri URI_CONTENT_PLACES = Uri.parse("content://eu.andlabs.fahrgemeinschaft/places");
	public static final Uri URI_CONTENT_QUERIES = Uri.parse("content://eu.andlabs.fahrgemeinschaft/queries");
	
	protected static final String TAG = "FAHRGEMEINSCHAFT";

	public static final String RIDES = "rides";

	public static final String PLACES = "places";

	private static final int URI_MATCH_RIDES = 1;

	private static final int URI_MATCH_PLACES = 2;

	private static UriMatcher mUriMatcher;
	
	private SQLiteOpenHelper mOpenHelper;

	static class DatabaseHelper extends SQLiteOpenHelper {
		


	DatabaseHelper(Context context) {
           super(context, "rides.db", null, 1);
       }

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + RIDES + " ("
	                   + BaseColumns._ID+ " INTEGER PRIMARY KEY, "
	                   + "orig INTEGER, "
	                   + "dest INTEGER, "
	                   + "departure" + " LONG, "
	                   + "name TEXT"
	                   + ");");

			db.execSQL("CREATE TABLE " + PLACES + " ("
	                   + BaseColumns._ID+ " INTEGER PRIMARY KEY, "
	                   + "lat INTEGER, "
	                   + "lng INTEGER, "
	                   + "name TEXT"
	                   + ");");
	
			Log.d(TAG, "DB CREATED");
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
		
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		// Assumes that any failures will be reported by a thrown exception.
	    return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,	String[] selectionArgs, String sortOrder) {
		Cursor result = null;
		
		switch (mUriMatcher.match(uri)) {
		case URI_MATCH_RIDES:
			result = mOpenHelper.getReadableDatabase().query(RIDES, projection, selection, selectionArgs, null, null, sortOrder);
			break;
		case URI_MATCH_PLACES:
			result = mOpenHelper.getReadableDatabase().query(PLACES, projection, selection, selectionArgs, null, null, sortOrder);
			break;
		default:
			break;
		}
		
		return result;
	}

	@Override
	public String getType(Uri uri) {
		return "mime";
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
    /**
     * A test package can call this to get a handle to the database underlying NotePadProvider,
     * so it can insert test data into the database. The test case class is responsible for
     * instantiating the provider in a test context; {@link android.test.ProviderTestCase2} does
     * this during the call to setUp()
     *
     * @return a handle to the database helper object for the provider's data.
     */
    public SQLiteOpenHelper getOpenHelperForTest() {
        return mOpenHelper;
    }

    static {
    	mUriMatcher = new UriMatcher(0);
    	mUriMatcher.addURI("eu.andlabs.fahrgemeinschaft", "rides", URI_MATCH_RIDES);
    	mUriMatcher.addURI("eu.andlabs.fahrgemeinschaft", "places", URI_MATCH_PLACES);
    }
    
}
