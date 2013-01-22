package eu.andlabs.fahrgemeinschaft.test;

import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import eu.andlabs.fahrgemeinschaft.RideProvider;

public class AdventurePuzzleGameSQL extends ProviderTestCase2<RideProvider> {

    private MockContentResolver android;
    private static final String TAG = "CRASH-TEST";

    public AdventurePuzzleGameSQL() {
        super(RideProvider.class, "eu.andlabs.fahrgemeinschaft");
    }



    /*
     * Here comes the LEVEL DESIGN:
     * Not much going on so far...
     */





    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Gets the resolver for this test.
        android = getMockContentResolver();
    }
}
