package com.example.symptomchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;

import com.example.symptomchecker.data.IllnessContracr.IllnessEntry;

public class MainQuizIllness extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the --- data loader
     */
    private static final int LOADER = 0;
    private static final int LOADER_EXIST = 1;

    private Uri mSelectIllnessUri = IllnessEntry.CONTENT_URI;

    /**
     * Adapter for the ListView
     */

    MainQuizOneIllnessCursorAdapter mOneIllnessCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz_illness);

        // for list illness
        final wLoadingDialog wLoadingDialog = new wLoadingDialog(MainQuizIllness.this);
        // Find the ListView which will be populated with the hospital data
        ListView listViewOneIllness = (ListView) findViewById(R.id.list_search_illnesses);

        mOneIllnessCursorAdapter = new MainQuizOneIllnessCursorAdapter(this, null);
        listViewOneIllness.setAdapter(mOneIllnessCursorAdapter);

        getLoaderManager().initLoader(LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
                mSelectIllnessUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mOneIllnessCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mOneIllnessCursorAdapter.swapCursor(null);
    }
}