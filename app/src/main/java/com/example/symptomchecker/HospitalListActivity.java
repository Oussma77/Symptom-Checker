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
import android.widget.EditText;
import android.widget.ListView;

import com.example.symptomchecker.data.DoctorContract;
import com.example.symptomchecker.data.HospitalContract.HospitalEntry;


public class HospitalListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the hospital data loader
     */
    private static final int HOSPITAL_LOADER = 0;
    private static final int HOSPITAL_LOADER_EXIST = 1;

    /**
     * Adapter for the ListView
     */
    HospitalCursorAdapter mCursorAdapter;

    private Uri mSelectHospitalUri = HospitalEntry.CONTENT_URI;
    HospitalCursorAdapter mHospitalCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);


        ListView hospitalListView = (ListView) findViewById(R.id.list_list_hospital);

        View emptyView = findViewById(R.id.empty_view_hospital);
        hospitalListView.setEmptyView(emptyView);

        mCursorAdapter = new HospitalCursorAdapter(this, null);
        hospitalListView.setAdapter(mCursorAdapter);

        hospitalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mSelectHospitalUri = ContentUris.withAppendedId(HospitalEntry.CONTENT_URI, id);

                getLoaderManager().initLoader(HOSPITAL_LOADER_EXIST, null, HospitalListActivity.this);
            }
        });

        getLoaderManager().initLoader(HOSPITAL_LOADER, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                HospitalEntry._ID,
                HospitalEntry.COLUMN_HOSPITAL_NAME,
                HospitalEntry.COLUMN_HOSPITAL_WORKTIME,
                HospitalEntry.COLUMN_HOSPITAL_PHONE,
                HospitalEntry.COLUMN_HOSPITAL_ADDRESS};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mSelectHospitalUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

        if (mSelectHospitalUri != HospitalEntry.CONTENT_URI) {
            int addressColumnIndex = data.getColumnIndex(HospitalEntry.COLUMN_HOSPITAL_ADDRESS);
            String hospitalAddress = data.getString(addressColumnIndex);

            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+hospitalAddress+", краснодар");
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