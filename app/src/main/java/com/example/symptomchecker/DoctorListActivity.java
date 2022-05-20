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

import com.example.symptomchecker.data.DoctorContract.DoctorEntry;

public class DoctorListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private Uri mSelectDoctorUri = DoctorEntry.CONTENT_URI;
    DoctorCursorAdapter mDoctorCursorAdapter;

    /**
     * Identifier for the doctor data loader
     */
    private static final int DOCTOR_LOADER = 0;
    private static final int DOCTOR_LOADER_EXIST = 1;

    /**
     * Adapter for the ListView
     */
    DoctorCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        // Find the ListView which will be populated with the doctor data
        ListView doctorListView = (ListView) findViewById(R.id.list_list_doctor);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view_doctor);
        doctorListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of doctor data in the Cursor.
        // There is no doctor data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new DoctorCursorAdapter(this, null);
        doctorListView.setAdapter(mCursorAdapter);

        doctorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                mSelectDoctorUri = ContentUris.withAppendedId(DoctorEntry.CONTENT_URI, id);

                getLoaderManager().initLoader(DOCTOR_LOADER_EXIST, null, DoctorListActivity.this);


            }
        });

        getLoaderManager().initLoader(DOCTOR_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                DoctorEntry._ID,
                DoctorEntry.COLUMN_DOCTOR_NAME,
                DoctorEntry.COLUMN_DOCTOR_AGE,
                DoctorEntry.COLUMN_DOCTOR_PHONE,
                DoctorEntry.COLUMN_DOCTOR_ADDRESS,
                DoctorEntry.COLUMN_DOCTOR_EMAIL};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mSelectDoctorUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

        if (mSelectDoctorUri != DoctorEntry.CONTENT_URI) {
            int phoneColumnIndex = data.getColumnIndex(DoctorEntry.COLUMN_DOCTOR_PHONE);
            String doctorPhone = data.getString(phoneColumnIndex);
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", doctorPhone, null));
            startActivity(intent);

        }

    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}