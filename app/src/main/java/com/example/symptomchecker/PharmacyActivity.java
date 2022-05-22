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

import com.example.symptomchecker.data.PharmacyContract.PharmacyEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PharmacyActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the Pharmacy data loader */
    private static final int PHARMACY_LOADER = 0;

    /** Adapter for the ListView */
    PharmacyCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PharmacyActivity.this, PharmacyEditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the Pharmacy data
        ListView pharmacyListView = (ListView) findViewById(R.id.list_pharmacy);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view_pharmacy);
        pharmacyListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of Pharmacy data in the Cursor.
        // There is no Pharmacy data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new PharmacyCursorAdapter(this, null);
        pharmacyListView.setAdapter(mCursorAdapter);

        pharmacyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(PharmacyActivity.this, PharmacyEditorActivity.class);

                Uri currentPharmacylUri = ContentUris.withAppendedId(PharmacyEntry.CONTENT_URI, id);
                // Set the URI on the data field of the intent
                intent.setData(currentPharmacylUri);

                // Launch the {@link EditorActivity} to display the data for the current doctor.
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(PHARMACY_LOADER, null, this);

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
                insertPharmacy();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPharmacys();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                PharmacyEntry.CONTENT_URI,   // Provider content URI to query
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

    private void insertPharmacy() {
        ContentValues values = new ContentValues();
        values.put(PharmacyEntry.COLUMN_PHARMACYS_NAME, "Дешёвая аптека");
        values.put(PharmacyEntry.COLUMN_PHARMACYS_WORKTIME, "08:00 - 20:00");
        values.put(PharmacyEntry.COLUMN_PHARMACYS_PHONE, "88612900722");
        values.put(PharmacyEntry.COLUMN_PHARMACYS_ADDRESS, "ул. 40-летия Победы, 33/1");
        Uri newUri = getContentResolver().insert(PharmacyEntry.CONTENT_URI, values);

        //list pharmacy 02
        ContentValues values1 = new ContentValues();
        values1.put(PharmacyEntry.COLUMN_PHARMACYS_NAME, "Аптека 112");
        values1.put(PharmacyEntry.COLUMN_PHARMACYS_WORKTIME, "08:00 - 20:00");
        values1.put(PharmacyEntry.COLUMN_PHARMACYS_PHONE, "88612526555");
        values1.put(PharmacyEntry.COLUMN_PHARMACYS_ADDRESS, "ул. 40-летия Победы, 108");
        Uri newUri1 = getContentResolver().insert(PharmacyEntry.CONTENT_URI, values1);

        //list pharmacy 03
        ContentValues values3 = new ContentValues();
        values3.put(PharmacyEntry.COLUMN_PHARMACYS_NAME, "Аптеки Кубани, № 6");
        values3.put(PharmacyEntry.COLUMN_PHARMACYS_WORKTIME, "08:00 - 20:00");
        values3.put(PharmacyEntry.COLUMN_PHARMACYS_PHONE, "+7987555413");
        values3.put(PharmacyEntry.COLUMN_PHARMACYS_ADDRESS, "ул. Рашпилевская, 183");
        Uri newUri3 = getContentResolver().insert(PharmacyEntry.CONTENT_URI, values3);

        //list pharmacy 04
        ContentValues values4 = new ContentValues();
        values4.put(PharmacyEntry.COLUMN_PHARMACYS_NAME, "Аптека ВИТА ЦЕНТРАЛЬНАЯ");
        values4.put(PharmacyEntry.COLUMN_PHARMACYS_WORKTIME, "08:00 - 20:00");
        values4.put(PharmacyEntry.COLUMN_PHARMACYS_PHONE, "+7987555413");
        values4.put(PharmacyEntry.COLUMN_PHARMACYS_ADDRESS, "Краснодар улица имени, ул. 40-летия Победы, 144/4");
        Uri newUri4 = getContentResolver().insert(PharmacyEntry.CONTENT_URI, values4);

        //list pharmacy 05
        ContentValues values5 = new ContentValues();
        values5.put(PharmacyEntry.COLUMN_PHARMACYS_NAME, "Моя Аптека, Сеть Аптек");
        values5.put(PharmacyEntry.COLUMN_PHARMACYS_WORKTIME, "08:00 - 20:00");
        values5.put(PharmacyEntry.COLUMN_PHARMACYS_PHONE, "88612527697");
        values5.put(PharmacyEntry.COLUMN_PHARMACYS_ADDRESS, "ул. Ипподромная, 53");
        Uri newUri5 = getContentResolver().insert(PharmacyEntry.CONTENT_URI, values5);

        //list pharmacy 06
        ContentValues values6 = new ContentValues();
        values6.put(PharmacyEntry.COLUMN_PHARMACYS_NAME, "Аптеки Кубани, № 13");
        values6.put(PharmacyEntry.COLUMN_PHARMACYS_WORKTIME, "08:00 - 20:00");
        values6.put(PharmacyEntry.COLUMN_PHARMACYS_PHONE, "88612040352");
        values6.put(PharmacyEntry.COLUMN_PHARMACYS_ADDRESS, "ул. Офицерская, 43");
        Uri newUri6 = getContentResolver().insert(PharmacyEntry.CONTENT_URI, values6);

        //list pharmacy 07
        ContentValues values7 = new ContentValues();
        values7.put(PharmacyEntry.COLUMN_PHARMACYS_NAME, "Аптеки Кубани, №505");
        values7.put(PharmacyEntry.COLUMN_PHARMACYS_WORKTIME, "08:00 - 20:00");
        values7.put(PharmacyEntry.COLUMN_PHARMACYS_PHONE, "88612330980");
        values7.put(PharmacyEntry.COLUMN_PHARMACYS_ADDRESS, "ул. Селезнева, 76");
        Uri newUri7 = getContentResolver().insert(PharmacyEntry.CONTENT_URI, values7);

    }

    private void deleteAllPharmacys() {
        int rowsDeleted = getContentResolver().delete(PharmacyEntry.CONTENT_URI, null, null);
        Log.v("PharmacyActivity1", rowsDeleted + " rows deleted from table database");
    }
}