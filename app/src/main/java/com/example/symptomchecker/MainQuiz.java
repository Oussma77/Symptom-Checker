package com.example.symptomchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.symptomchecker.data.SymptomContract;

import com.example.symptomchecker.data.SymptomContract.SymptomEntry;

public class MainQuiz extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //--------------------------------------for data base
    /**
     * Identifier for the hospital data loader
     */
    private static final int LOADER = 0;
    private static final int LOADER_EXIST = 1;

    private Uri mSelectSymptomUri = SymptomEntry.CONTENT_URI;
    SymptomCursorAdapter mSymptomCursorAdapter;

    /**
     * Adapter for the ListView
     */
    MainQuizOneSymptomCursorAdapter mOneSymptomCursorAdapter;

    //-**********************************************end

    RadioGroup radioGroupForWho;
    RadioButton radioButtonForWho;
    RadioButton radioButtonsemoene;

    String ForWhow = "";

    LinearLayout linearLayoutForWho;
    LinearLayout linearLayout2;
    LinearLayout linearSelectSymptomp;
    LinearLayout linearResult;

    Button buttonForWho;
    Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);

        linearResult = (LinearLayout) findViewById(R.id.linearResults);
        linearSelectSymptomp = (LinearLayout) findViewById(R.id.linearAddSymptoms);


        linearLayoutForWho = (LinearLayout) findViewById(R.id.linearForWho);
//        linearLayoutForWho.setVisibility(View.VISIBLE);
        radioGroupForWho = findViewById(R.id.radioGroupForWho);
        buttonForWho = (Button) findViewById(R.id.buttonForWho);

        buttonForWho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutForWho.setVisibility(View.GONE);
                //linearLayout2.setVisibility(View.VISIBLE);

                int radioId = radioGroupForWho.getCheckedRadioButtonId();
                radioButtonForWho = findViewById(radioId);
                ForWhow = radioButtonForWho.getText().toString().trim();
                Log.v("MainQuiz1", "Radio = " + ForWhow);

            }
        });


        // for list symptoms
        // Find the ListView which will be populated with the hospital data
        ListView listViewOneSymptoms = (ListView) findViewById(R.id.list_search_Symptoms);

        // Setup an Adapter to create a list item for each row of hospital data in the Cursor.
        // There is no hospital data yet (until the loader finishes) so pass in null for the Cursor.
        mOneSymptomCursorAdapter = new MainQuizOneSymptomCursorAdapter(this, null);
        listViewOneSymptoms.setAdapter(mOneSymptomCursorAdapter);

        listViewOneSymptoms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mSelectSymptomUri = ContentUris.withAppendedId(SymptomEntry.CONTENT_URI, id);

                getLoaderManager().initLoader(LOADER_EXIST, null, MainQuiz.this);
                linearSelectSymptomp.setVisibility(View.GONE);
                linearResult.setVisibility(View.VISIBLE);
            }
        });

        if (mSelectSymptomUri != null) {
            getLoaderManager().initLoader(LOADER, null, this);
        }

        //result

        // Find the ListView which will be populated with the Pharmacy data
        ListView resultSymptomListView = (ListView) findViewById(R.id.list_result_Symptoms);

        mSymptomCursorAdapter = new SymptomCursorAdapter(this, null);
        resultSymptomListView.setAdapter(mSymptomCursorAdapter);


        getLoaderManager().initLoader(LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                SymptomEntry._ID,
                SymptomEntry.COLUMN_SYMPTOM_NAME,
                SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION,
                SymptomEntry.COLUMN_SYMPTOM_NEEDHELP,
                SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION,
                SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED,
                SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED,
                SymptomEntry.COLUMN_SYMPTOM_REASONS,
        };
        // This loader will execute the ContentProvider's query method on a background thread
            return new CursorLoader(this,   // Parent activity context
                    mSelectSymptomUri,   // Provider content URI to query
                    projection,             // Columns to include in the resulting Cursor
                    null,                   // No selection clause
                    null,                   // No selection arguments
                    null);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mOneSymptomCursorAdapter.swapCursor(cursor);
        mSymptomCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mOneSymptomCursorAdapter.swapCursor(null);
        mSymptomCursorAdapter.swapCursor(null);
    }
}