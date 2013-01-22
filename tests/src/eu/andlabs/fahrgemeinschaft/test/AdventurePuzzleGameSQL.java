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






    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Gets the resolver for this test.
        android = getMockContentResolver();
    }
}
