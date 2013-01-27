/*
 * Copyright (C) 2012 Contributors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.andlabs.fahrgemeinschaft;

import com.actionbarsherlock.app.SherlockListFragment;

import eu.andlabs.fahrgemeinschaft.RideListFragment.ListItemClicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RideListFragment extends SherlockListFragment implements LoaderCallbacks<Cursor> {
    
    private static final String TAG = "Fahrgemeinschaft";
    private ListItemClicker callback;

    @Override
    public View onCreateView(final LayoutInflater lI, ViewGroup p, Bundle b) {
        return lI.inflate(R.layout.fragment_ride_list, p, false);
    }

    @Override
    public void onViewCreated(View layout, Bundle savedInstanceState) {
        super.onViewCreated(layout, savedInstanceState);
        
        setListAdapter(new BaseAdapter() {
            
            @Override
            public int getCount() {
                return 42;
            }
            
            @Override
            public View getView(int position, View v, ViewGroup parent) {
                v = getLayoutInflater(null).inflate(R.layout.view_ride_list_entry, parent, false);
                if (position % 2 == 0) {
                    v.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                } else {
                    v.setBackgroundColor(getResources().getColor(R.color.mediumGreen));
                }
                return v; 
            }
            
            @Override
            public long getItemId(int position) { return 0; }
            
            @Override
            public Object getItem(int position) { return null; }
        });
        
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
        Uri uri = Uri.parse("content://...");
        return new CursorLoader(getActivity(), uri, null, null, null, null);
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor msges) {
        ((CursorAdapter)getListAdapter()).swapCursor(msges);
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        Log.d(TAG, "onLoaderReset");
    }
    

    public interface ListItemClicker {
        public void onListItemClick(String id);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (ListItemClicker) activity;
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        callback.onListItemClick("foo");
    }

    static class RideView extends RelativeLayout {

        private TextView from;

        public RideView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        
        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
//            text = (TextView) findViewById(R.id.msg_text);
        }
    }
}
