package eu.andlabs.fahrgemeinschaft;


import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import eu.andlabs.fahrgemeinschaft.RideListFragment.ListItemClicker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

public class ResultsActivity extends SherlockFragmentActivity implements ListItemClicker {

    private ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Stuttgart -> München");
        setContentView(R.layout.activity_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                
                @Override
                public int getCount() {
                    return 42;
                }
                
                @Override
                public Fragment getItem(int position) {
                    if (position == 0) {
                        return new RideListFragment();
                    } else {
                        return new RideDetailsFragment();
                    }
                }
            });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "hey", Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(String id) {
        pager.setCurrentItem(7, false);
    }
    
    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() > 0) {
            pager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }
    
}
