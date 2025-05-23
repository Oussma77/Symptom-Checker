package com.example.symptomchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.symptomchecker.data.HospitalContract;
import com.example.symptomchecker.data.PharmacyContract.PharmacyEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PharmacyListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    /** Identifier for the Pharmacy data loader */
    private static final int PHARMACY_LOADER = 0;

    /** Adapter for the ListView */
    PharmacyCursorAdapter mCursorAdapter;

    private Uri mSelectPharmacyUri = PharmacyEntry.CONTENT_URI;
    PharmacyCursorAdapter mPharmacyCursorAdapter;
    private static final int LOADER_EXIST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_list);

        ListView pharmacyListView = (ListView) findViewById(R.id.list_list_pharmacy);

        View emptyView = findViewById(R.id.empty_view_pharmacy);
        pharmacyListView.setEmptyView(emptyView);

        mCursorAdapter = new PharmacyCursorAdapter(this, null);
        pharmacyListView.setAdapter(mCursorAdapter);

        pharmacyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                mSelectPharmacyUri = ContentUris.withAppendedId(PharmacyEntry.CONTENT_URI, id);

                getLoaderManager().initLoader(LOADER_EXIST, null, PharmacyListActivity.this);

            }
        });

        getLoaderManager().initLoader(PHARMACY_LOADER, null, this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                PharmacyEntry._ID,
                PharmacyEntry.COLUMN_PHARMACYS_NAME,
                PharmacyEntry.COLUMN_PHARMACYS_WORKTIME,
                PharmacyEntry.COLUMN_PHARMACYS_PHONE,
                PharmacyEntry.COLUMN_PHARMACYS_ADDRESS};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mSelectPharmacyUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);

        if (mSelectPharmacyUri != PharmacyEntry.CONTENT_URI) {
            int addressColumnIndex = cursor.getColumnIndex(PharmacyEntry.COLUMN_PHARMACYS_ADDRESS);
            String Address = cursor.getString(addressColumnIndex);

            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+Address+", краснодар");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);


        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }


}