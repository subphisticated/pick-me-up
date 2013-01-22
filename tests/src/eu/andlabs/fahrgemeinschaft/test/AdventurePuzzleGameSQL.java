package eu.andlabs.fahrgemeinschaft.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import eu.andlabs.fahrgemeinschaft.RideProvider;

public class AdventurePuzzleGameSQL extends ProviderTestCase2<RideProvider> {

    private Cursor result;
    private MockContentResolver android;
    private static final String TAG = "CRASH-TEST";

    public AdventurePuzzleGameSQL() {
        super(RideProvider.class, "eu.andlabs.fahrgemeinschaft");
    }



    /*
     * ### Level 1 ###   Remembering favorite Places  #########################
     */
    Uri PLACES = Uri.parse("content://eu.andlabs.fahrgemeinschaft/places");

    public void testStorePlaces() {

        Uri pausenhof = android.insert(PLACES, getDummyPlace("Pausenhof"));
        assertEquals("QUEST:   Append database primary key to pausenhof uri.",
                pausenhof.getLastPathSegment(), "1");

        result = android.query(PLACES, null, null, null, null);  // 3 Points!
        assertEquals("QUEST:   database finds one place.", result.getCount(), 1);

        result.moveToFirst();  // 5 Points!
        assertEquals("QUEST:   is Pausenhof.", result.getString(1), "Pausenhof");
    }
    
    private ContentValues getDummyPlace(String name) {
        ContentValues place = new ContentValues();
        place.put("name", name);
        place.put("lat", 23);
        place.put("lng", 42);
        return place;
    }



    /*
     * ### Level 2 ###   Remembering Search Queries  #########################
     */
    Uri QUERIES = Uri.parse("content://eu.andlabs.fahrgemeinschaft/queries");
    
    public void testStoreQueries() {
        
        Uri search = android.insert(QUERIES, getDummySearchQuery());
        int id = Integer.parseInt(search.getLastPathSegment());
        assertEquals("QUEST:   first primary key", id, 1);
        
        Uri again = android.insert(QUERIES, getDummySearchQuery());
        assertEquals("QUEST:   still the same first primary key",
                Integer.parseInt(again.getLastPathSegment()), id); // 5 Points!
        
        result = android.query(QUERIES, null, null, null, null);
        assertEquals("QUEST:   database finds only ONE query.", result.getCount(), 1); // 3 Points!
        
        result.moveToFirst();  // 7 Points!
        assertEquals("QUEST:   the single query has id", result.getInt(0), id); // 3 Points!
        assertEquals("QUEST:   it has Pausenhof as origin", result.getInt(1), 1); // 5 Points!
        assertEquals("QUEST:   it has Studios as destination", result.getInt(2), 2); // 5 Points!
        assertEquals("QUEST:   it has been used TWO times to search", result.getInt(3), 2); // 7 Points!
    }
    
    private ContentValues getDummySearchQuery() {
        Uri pausenhof = android.insert(PLACES, getDummyPlace("Pausenhof")); // primary key: 1
        Uri studios = android.insert(PLACES, getDummyPlace("Studios")); // primary key: 2
        ContentValues query = new ContentValues();
        query.put("orig", Integer.parseInt(pausenhof.getLastPathSegment()));
        query.put("dest", Integer.parseInt(studios.getLastPathSegment()));
        query.put("when", System.currentTimeMillis());
        return query;
    }
    
    
    
    /*
     * ### Level 3 ###   Storing Fahrgemeinschaften  #########################
     */
    Uri RIDES = Uri.parse("content://eu.andlabs.fahrgemeinschaft/queries/1/rides");
    
    public void testStoreRides() {
        
        Uri search = android.insert(QUERIES, getDummySearchQuery());
        Uri horst = android.insert(search, getDummyRide("Horst"));
        Uri lisa = android.insert(search, getDummyRide("Lisa"));
        Uri max = android.insert(search, getDummyRide("Max"));
        
        int max_id = Integer.parseInt(max.getLastPathSegment());
        assertEquals("QUEST:   third primary key", max_id, 3); // 5 Points!
        
        result = android.query(RIDES, null, null, null, null);
        assertEquals("QUEST:   finds three rides for query", result.getCount(), 3); // 5 Points!
        
        result.moveToPosition(3);
        assertEquals("QUEST:   the single query has id", result.getInt(0), max_id); // 3 Points!
        assertEquals("QUEST:   max is the driver", result.getString(1), "Max"); // 5 Points!
    }
    
    private ContentValues getDummyRide(String driver_name) {
        ContentValues ride = new ContentValues();
        ride.put("name", driver_name);
        ride.put("tel", "1234567");
        return ride;
    }






    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Gets the resolver for this test.
        android = getMockContentResolver();
    }
}
