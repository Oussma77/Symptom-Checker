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
        ListView doctorListView = (ListView) findViewById(R.id.list_doctor);

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
        values.put(DoctorEntry.COLUMN_DOCTOR_NAME, "Кудряшева Анна Михайловна");
        values.put(DoctorEntry.COLUMN_DOCTOR_AGE, 38);
        values.put(DoctorEntry.COLUMN_DOCTOR_PHONE, "+7 961 201-84-91");
        values.put(DoctorEntry.COLUMN_DOCTOR_ADDRESS, "ул. им. 70 лет Октября, д. 1");
        values.put(DoctorEntry.COLUMN_DOCTOR_EMAIL, "anna_mukaulov@mail.ru");

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link doctorEntry#CONTENT_URI} to indicate that we want to insert
        // into the doctors database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(DoctorEntry.CONTENT_URI, values);

        //New doctors 2
        ContentValues values2 = new ContentValues();
        values2.put(DoctorEntry.COLUMN_DOCTOR_NAME, "Устинова Татьяна Александровна");
        values2.put(DoctorEntry.COLUMN_DOCTOR_AGE, 38);
        values2.put(DoctorEntry.COLUMN_DOCTOR_PHONE, "+7 961 20-50-830");
        values2.put(DoctorEntry.COLUMN_DOCTOR_ADDRESS, "ул. Выгонная, д. 20");
        values2.put(DoctorEntry.COLUMN_DOCTOR_EMAIL, "tatana52@mail.ru");
        Uri newUri2 = getContentResolver().insert(DoctorEntry.CONTENT_URI, values2);

        //New doctors 3
        ContentValues values3 = new ContentValues();
        values3.put(DoctorEntry.COLUMN_DOCTOR_NAME, "Абгарян Гаяне Ильинична");
        values3.put(DoctorEntry.COLUMN_DOCTOR_AGE, 38);
        values3.put(DoctorEntry.COLUMN_DOCTOR_PHONE, "+7 961  201-84-91");
        values3.put(DoctorEntry.COLUMN_DOCTOR_ADDRESS, "ул. Ставропольская, д. 178");
        values3.put(DoctorEntry.COLUMN_DOCTOR_EMAIL, "anna_mukaulov@mail.ru");
        Uri newUri3 = getContentResolver().insert(DoctorEntry.CONTENT_URI, values3);

        //New doctors 4
        ContentValues values4 = new ContentValues();
        values4.put(DoctorEntry.COLUMN_DOCTOR_NAME, "Адамян Наири Камоевич");
        values4.put(DoctorEntry.COLUMN_DOCTOR_AGE, 44);
        values4.put(DoctorEntry.COLUMN_DOCTOR_PHONE, "+7 961 201-84-73");
        values4.put(DoctorEntry.COLUMN_DOCTOR_ADDRESS, "ул. Гагарина, д. 124");
        values4.put(DoctorEntry.COLUMN_DOCTOR_EMAIL, "naurui564@mail.ru");
        Uri newUri4 = getContentResolver().insert(DoctorEntry.CONTENT_URI, values4);

        //New doctors 5
        ContentValues values5 = new ContentValues();
        values5.put(DoctorEntry.COLUMN_DOCTOR_NAME, "Какуша Михаил Леонидович");
        values5.put(DoctorEntry.COLUMN_DOCTOR_AGE, 38);
        values5.put(DoctorEntry.COLUMN_DOCTOR_PHONE, "+7 961 202-58-05");
        values5.put(DoctorEntry.COLUMN_DOCTOR_ADDRESS, "ул. Коммунаров, д. 270");
        values5.put(DoctorEntry.COLUMN_DOCTOR_EMAIL, "miacha4518@mail.ru");
        Uri newUri5 = getContentResolver().insert(DoctorEntry.CONTENT_URI, values5);

        //New doctors 6
        ContentValues values6 = new ContentValues();
        values6.put(DoctorEntry.COLUMN_DOCTOR_NAME, "Шавкун Ирина Викторовна");
        values6.put(DoctorEntry.COLUMN_DOCTOR_AGE, 55);
        values6.put(DoctorEntry.COLUMN_DOCTOR_PHONE, "+7 961 20-50-830");
        values6.put(DoctorEntry.COLUMN_DOCTOR_ADDRESS, "ул. Выгонная, д. 20");
        values6.put(DoctorEntry.COLUMN_DOCTOR_EMAIL, "anna_mukaulov@mail.ru");
        Uri newUri6 = getContentResolver().insert(DoctorEntry.CONTENT_URI, values6);

        //New doctors 7
        ContentValues values7 = new ContentValues();
        values7.put(DoctorEntry.COLUMN_DOCTOR_NAME, "Миценко Инна Владимировна");
        values7.put(DoctorEntry.COLUMN_DOCTOR_AGE, 38);
        values7.put(DoctorEntry.COLUMN_DOCTOR_PHONE, "+7 961 206-26-08");
        values7.put(DoctorEntry.COLUMN_DOCTOR_ADDRESS, "ул. Тюляева, д. 4/1");
        values7.put(DoctorEntry.COLUMN_DOCTOR_EMAIL, "inna548@mail.ru");
        Uri newUri7 = getContentResolver().insert(DoctorEntry.CONTENT_URI, values7);

        //New doctors 8
        ContentValues values8 = new ContentValues();
        values8.put(DoctorEntry.COLUMN_DOCTOR_NAME, "Липатова Анна Геннадьевна");
        values8.put(DoctorEntry.COLUMN_DOCTOR_AGE, 38);
        values8.put(DoctorEntry.COLUMN_DOCTOR_PHONE, "+7 961 201-87-55");
        values8.put(DoctorEntry.COLUMN_DOCTOR_ADDRESS, "ул. Гагарина, д. 124");
        values8.put(DoctorEntry.COLUMN_DOCTOR_EMAIL, "anna_mukaulov@mail.ru");
        Uri newUri8 = getContentResolver().insert(DoctorEntry.CONTENT_URI, values8);

        //New doctors 9
        ContentValues values9 = new ContentValues();
        values9.put(DoctorEntry.COLUMN_DOCTOR_NAME, "Косьяненко Светлана Сергеевна");
        values9.put(DoctorEntry.COLUMN_DOCTOR_AGE, 38);
        values9.put(DoctorEntry.COLUMN_DOCTOR_PHONE, "+7 961 585-22-91");
        values9.put(DoctorEntry.COLUMN_DOCTOR_ADDRESS, "ул. Гагарина, д. 124");
        values9.put(DoctorEntry.COLUMN_DOCTOR_EMAIL, "cvitlana4@mail.ru");
        Uri newUri9 = getContentResolver().insert(DoctorEntry.CONTENT_URI, values9);

        //New doctors 10
        ContentValues values10 = new ContentValues();
        values10.put(DoctorEntry.COLUMN_DOCTOR_NAME, "Лобач Ольга Игоревна");
        values10.put(DoctorEntry.COLUMN_DOCTOR_AGE, 38);
        values10.put(DoctorEntry.COLUMN_DOCTOR_PHONE, "+7 961 201-77-91");
        values10.put(DoctorEntry.COLUMN_DOCTOR_ADDRESS, "пр-кт К. Образцова, д. 27/1");
        values10.put(DoctorEntry.COLUMN_DOCTOR_EMAIL, "olgakaa45@mail.ru");
        Uri newUri10 = getContentResolver().insert(DoctorEntry.CONTENT_URI, values10);


    }

    private void deleteAllDoctors() {
        int rowsDeleted = getContentResolver().delete(DoctorEntry.CONTENT_URI, null, null);
        Log.v("DoctorActivity", rowsDeleted + " rows deleted from doctor database");
    }

}