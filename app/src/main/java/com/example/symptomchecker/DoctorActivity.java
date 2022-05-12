package com.example.symptomchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.symptomchecker.data.DoctorContract.DoctorEntry;

public class DoctorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    /** Identifier for the doctor data loader */
    private static final int DOCTOR_LOADER = 0;

    /** Adapter for the ListView */
    DoctorCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorActivity.this, DoctorEditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the doctor data
        ListView doctorListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        doctorListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of doctor data in the Cursor.
        // There is no doctor data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new DoctorCursorAdapter(this, null);
        doctorListView.setAdapter(mCursorAdapter);

        doctorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(DoctorActivity.this, DoctorEditorActivity.class);

                // Form the content URI that represents the specific doctor that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link doctorEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.doctors/doctors/2"
                // if the doctor with ID 2 was clicked on.
                Uri currentDoctorUri = ContentUris.withAppendedId(DoctorEntry.CONTENT_URI, id);
                // Set the URI on the data field of the intent
                intent.setData(currentDoctorUri);

                // Launch the {@link EditorActivity} to display the data for the current doctor.
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(DOCTOR_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_doctor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertDoctor();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllDoctors();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                DoctorEntry._ID,
                DoctorEntry.COLUMN_DOCTOR_NAME,
                DoctorEntry.COLUMN_DOCTOR_AGE,
                DoctorEntry.COLUMN_DOCTOR_PHONE,
                DoctorEntry.COLUMN_DOCTOR_ADDRESS,
                DoctorEntry.COLUMN_DOCTOR_EMAIL };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                DoctorEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link doctorCursorAdapter} with this new cursor containing updated doctor data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    private void insertDoctor() {
        // Create a ContentValues object where column names are the keys,
        // and name's doctor attributes are the values.
        ContentValues values = new ContentValues();
        values.put(DoctorEntry.COLUMN_DOCTOR_NAME, "D.IVAN ZEKOVITCHE");
        values.put(DoctorEntry.COLUMN_DOCTOR_AGE, 34);
        values.put(DoctorEntry.COLUMN_DOCTOR_PHONE, "+79875554132");
        values.put(DoctorEntry.COLUMN_DOCTOR_ADDRESS, "ул Красная, 14");
        values.put(DoctorEntry.COLUMN_DOCTOR_EMAIL, "oussama@gmail.com");

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link doctorEntry#CONTENT_URI} to indicate that we want to insert
        // into the doctors database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(DoctorEntry.CONTENT_URI, values);
    }

    private void deleteAllDoctors() {
        int rowsDeleted = getContentResolver().delete(DoctorEntry.CONTENT_URI, null, null);
        Log.v("DoctorActivity", rowsDeleted + " rows deleted from doctor database");
    }

}