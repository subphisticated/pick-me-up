package eu.andlabs.fahrgemeinschaft.test;

import java.net.URI;
import java.util.Date;

import eu.andlabs.fahrgemeinschaft.RideProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

public class FahrgemeinschaftTestProvider extends ProviderTestCase2<RideProvider> {

	private static final String TAG = "TTEST";
	
    // Contains a reference to the mocked content resolver for the provider under test.
    private MockContentResolver mMockResolver;
    // Contains an SQLite database, used as test data
    private SQLiteDatabase mDb;

	public FahrgemeinschaftTestProvider() {
		super(RideProvider.class, "eu.andlabs.fahrgemeinschaft");
		Log.d(TAG, "HI");
	}

	public void testMimeType() {
		String mime = mMockResolver.getType(RideProvider.URI_CONTENT_PLACES);
		assertEquals("mime", mime);
	}
	
	/*
	 * ####################### Q U E R I E S ######################################
	 */
	static final Uri QUERIES_URI = Uri.parse("content://eu.andlabs.fahrgemeinschaft/queries");
	
	public void testInsertSearchParams() {
		mMockResolver.insert(QUERIES_URI, getPlaceDummy(System.currentTimeMillis()));
		Cursor c = mDb.query("queries", null, null, null, null, null, null);
		assertEquals(c.getCount(), 1);
		c.moveToFirst();
		assertEquals(c.getString(2), "Garmisch");
		// TODO assert query has orig_id/dest_id from places table   .. insert or get
	}
	
	public void testQueryJobsToDo() {
		mDb.insert("queries", null, getPlaceDummy(System.currentTimeMillis() - 3000)); // abgelaufen
		Cursor c = mMockResolver.query(QUERIES_URI, null, "date > "+System.currentTimeMillis(), null, null);
		assertEquals(c.getCount(), 0); // no jobs to do

		mDb.insert("queries", null, getPlaceDummy(System.currentTimeMillis() + 3000)); // back to future
		c = mMockResolver.query(QUERIES_URI, null, "date > "+System.currentTimeMillis(), null, null);
		assertEquals(c.getCount(), 1); // one job in queue
		c.moveToFirst();
		assertEquals(c.getString(2), "Garmisch");	
	}
	
	private ContentValues getPlaceDummy(long when) {
		ContentValues cv = new ContentValues();
		cv.put("orig", "Pausenhof");
		cv.put("dest", "Garmisch");
		cv.put("date", when);
		cv.put("mode", "driver");
		return cv;
	}


	
	
	/*
	 * #######################   R I D E S   ######################################
	 */

	static final Uri RIDES_URI = Uri.parse("content://eu.andlabs.fahrgemeinschaft/rides");
	
	public void testInsertRides() {	
		mMockResolver.insert(RIDES_URI, getRideDummy());
		Cursor c = mDb.query("rides", null, null, null, null, null, null);
		assertEquals(c.getCount(), 1);
		c.moveToFirst();
		assertEquals(c.getString(4), "Max Mustermann");			
	}

	public void testQueryRides() {
        mDb.insertOrThrow("places", null, getPlaceDummy("Pausenhof"));
        mDb.insertOrThrow("places", null, getPlaceDummy("Garmisch"));
        long id = mDb.insert("queries", null, getPlaceDummy(System.currentTimeMillis() + 3000)); // future
        mDb.insertOrThrow("rides", null, getRideDummy());
        mDb.insertOrThrow("rides", null, getRideDummy());
        mDb.insertOrThrow("rides", null, getRideDummy());
		Cursor r = mMockResolver.query( Uri.parse("content://eu.andlabs.fahrgemeinschaft/queries/"+id+"/rides"), null, "orig = 23 AND dest = 42", null, null);
		assertEquals(3, r.getCount());
		r.moveToFirst();
		assertEquals(r.getInt(1), "Pausenhof");
	}

	private ContentValues getPlaceDummy(String name) {
		ContentValues place = new ContentValues();
		place.put("lat", 23);
		place.put("lng", 42);
		place.put("name", name);
		return place;
	}

	
	
	/*
	 * #######################   P L A C E S   ######################################
	 */
	static final Uri PLACES_URI = Uri.parse("content://eu.andlabs.fahrgemeinschaft/places");
	
	public void testOrigAutoCompletion() {
        mDb.insertOrThrow("places", null, getPlaceDummy("Pausenhof"));
        mDb.insertOrThrow("places", null, getPlaceDummy("Garmisch"));
        mDb.insertOrThrow("places", null, getPlaceDummy("Passau"));
		Cursor r = mMockResolver.query(Uri.parse("content://eu.andlabs.fahrgemeinschaft/places?q=Pa"), null, null, null, null); 
		assertEquals(2, r.getCount());
		assertEquals(r.getInt(1), "Passau");
	}
	
	public void testDestAutoCompletion() {
		// long orig_id = mDb.insertOrThrow("places", null, getPlaceDummy("Pausenhof"));
        long orig_id = mDb.insertOrThrow("places", null, getPlaceDummy("Pausenhof"));
        mDb.insertOrThrow("places", null, getPlaceDummy("Garmisch"));
        mDb.insertOrThrow("places", null, getPlaceDummy("Passau"));
        mDb.insert("queries", null, getPlaceDummy(System.currentTimeMillis() + 3000)); // from pausenhof to garmisch
		Cursor r = mMockResolver.query(
				Uri.parse("content://eu.andlabs.fahrgemeinschaft/places/"+orig_id+"/destinations"), null, null, null, null); 
		assertEquals(2, r.getCount()); // check sorting
		r.moveToFirst();
		assertEquals(r.getInt(1), "Passau");
	}
	
	
	private ContentValues getRideDummy() {
		ContentValues ride = new ContentValues();
		ride.put("orig", 23);
		ride.put("dest", 42);
		ride.put("date", System.currentTimeMillis());
		ride.put("name", "Max Mustermann");
		return ride;

	}
		
	private void insertTestPlaces() {
		ContentValues place = new ContentValues();
		place.put("lat", 23);
		place.put("lng", 42);
		place.put("name", "Pausenhof");
        mDb.insertOrThrow(RideProvider.PLACES, null, place);
        place.put("name", "ANDLABS Buero");
        mDb.insertOrThrow(RideProvider.PLACES, null, place);
        place.put("name", "Cafe Kosmos");
        mDb.insertOrThrow(RideProvider.PLACES, null, place);
	}
	
    /*
     * Sets up the test environment before each test method. Creates a mock content resolver,
     * gets the provider under test, and creates a new database for the provider.
     */
    @Override
    protected void setUp() throws Exception {
        // Calls the base class implementation of this method.
        super.setUp();

        // Gets the resolver for this test.
        mMockResolver = getMockContentResolver();

        /*
         * Gets a handle to the database underlying the provider. Gets the provider instance
         * created in super.setUp(), gets the DatabaseOpenHelper for the provider, and gets
         * a database object from the helper.
         */
        mDb = getProvider().getOpenHelperForTest().getWritableDatabase();
    }

    /*
     *  This method is called after each test method, to clean up the current fixture. Since
     *  this sample test case runs in an isolated context, no cleanup is necessary.
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
