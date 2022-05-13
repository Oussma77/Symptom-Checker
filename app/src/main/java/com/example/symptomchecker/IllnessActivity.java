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

import com.example.symptomchecker.data.IllnessContracr.IllnessEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class IllnessActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the Pharmacy data loader */
    private static final int ILLNESS_LOADER = 0;

    /** Adapter for the ListView */
    IllnessCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illness);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IllnessActivity.this, IllnessEditorActivity.class);
                startActivity(intent);
            }
        });


        // Find the ListView which will be populated with the Pharmacy data
        ListView illnessListView = (ListView) findViewById(R.id.list_illness);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view_illness);
        illnessListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of Pharmacy data in the Cursor.
        // There is no Pharmacy data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new IllnessCursorAdapter(this, null);
        illnessListView.setAdapter(mCursorAdapter);

        illnessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(IllnessActivity.this, IllnessEditorActivity.class);

                Uri currentlUri = ContentUris.withAppendedId(IllnessEntry.CONTENT_URI, id);
                // Set the URI on the data field of the intent
                intent.setData(currentlUri);

                // Launch the {@link EditorActivity} to display the data for the current doctor.
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(ILLNESS_LOADER, null, this);

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
                insertIllness();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllIllnesses();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                IllnessEntry._ID,
                IllnessEntry.COLUMN_ILLNESS_NAME,
                IllnessEntry.COLUMN_ILLNESS_TYPE,
                IllnessEntry.COLUMN_ILLNESS_DESC,
                IllnessEntry.COLUMN_ILLNESS_CAUSES,
                IllnessEntry.COLUMN_ILLNESS_RISK,
                IllnessEntry.COLUMN_ILLNESS_SYMPTOMS,
                IllnessEntry.COLUMN_ILLNESS_MEDICINES,
                IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR,
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                IllnessEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    private void insertIllness() {
        ContentValues values = new ContentValues();
        values.put(IllnessEntry.COLUMN_ILLNESS_NAME, "-");
        values.put(IllnessEntry.COLUMN_ILLNESS_TYPE, "-");
        values.put(IllnessEntry.COLUMN_ILLNESS_DESC, "-");
        values.put(IllnessEntry.COLUMN_ILLNESS_CAUSES, "-");
        values.put(IllnessEntry.COLUMN_ILLNESS_RISK, "-");
        values.put(IllnessEntry.COLUMN_ILLNESS_SYMPTOMS, "-");
        values.put(IllnessEntry.COLUMN_ILLNESS_MEDICINES, "-");
        values.put(IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR, "-");


        Uri newUri = getContentResolver().insert(IllnessEntry.CONTENT_URI, values);
    }

    private void deleteAllIllnesses() {
        int rowsDeleted = getContentResolver().delete(IllnessEntry.CONTENT_URI, null, null);
        Log.v("IllnessActivity", rowsDeleted + " rows deleted from table database");
    }

}