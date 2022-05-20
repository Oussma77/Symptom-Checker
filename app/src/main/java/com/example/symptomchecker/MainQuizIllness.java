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
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.symptomchecker.data.IllnessContracr.IllnessEntry;
import com.example.symptomchecker.data.SymptomContract;

public class MainQuizIllness extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the --- data loader
     */
    private static final int LOADER = 0;
    private static final int LOADER_EXIST = 1;

    private Uri mSelectIllnessUri = IllnessEntry.CONTENT_URI;


    //all illness
    LinearLayout linearListIllnesses;

    //select illness
    LinearLayout linearResultIllness;


    /**
     * Adapter for the ListView
     */

    MainQuizOneIllnessCursorAdapter mOneIllnessCursorAdapter;
    IllnessCursorAdapter mIllnessCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz_illness);

        linearResultIllness = (LinearLayout) findViewById(R.id.linearResultsIllness);
        linearListIllnesses = (LinearLayout) findViewById(R.id.linearListIllnesses);
        linearListIllnesses.setVisibility(View.VISIBLE);
        // for list illness
        final wLoadingDialog wLoadingDialog = new wLoadingDialog(MainQuizIllness.this);
        // Find the ListView which will be populated with the hospital data
        ListView listViewOneIllness = (ListView) findViewById(R.id.list_search_illnesses);

        mOneIllnessCursorAdapter = new MainQuizOneIllnessCursorAdapter(this, null);
        listViewOneIllness.setAdapter(mOneIllnessCursorAdapter);

        listViewOneIllness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectIllnessUri = ContentUris.withAppendedId(IllnessEntry.CONTENT_URI, id);

                wLoadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wLoadingDialog.dismissDialog();
                        linearListIllnesses.setVisibility(View.GONE);
                        linearResultIllness.setVisibility(View.VISIBLE);
                    }
                }, 4000);
                getLoaderManager().initLoader(LOADER_EXIST, null, MainQuizIllness.this);
            }
        });

        //All list result // Find the ListView which will be populated with the Pharmacy data
        ListView resultIllnessListView = (ListView) findViewById(R.id.list_result_illness);

        mIllnessCursorAdapter = new IllnessCursorAdapter(this, null);
        resultIllnessListView.setAdapter(mIllnessCursorAdapter);


        //       Список врачей в вашем районе
        ImageButton imageButtonDoctor = findViewById(R.id.img_btn_list_doctors1);
        imageButtonDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent numbersIntent = new Intent(MainQuizIllness.this, DoctorListActivity.class);
                startActivity(numbersIntent);
            }
        });


        ImageButton imageButtonHospitals = findViewById(R.id.img_btn_list_hospitals1);
        imageButtonHospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent numbersIntent = new Intent(MainQuizIllness.this, HospitalListActivity.class);
                startActivity(numbersIntent);
            }
        });

        ImageButton imageButtonPharmacy = findViewById(R.id.img_btn_list_pharmacy1);
        imageButtonPharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent numbersIntent = new Intent(MainQuizIllness.this, PharmacyListActivity.class);
                startActivity(numbersIntent);
            }
        });

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
        mIllnessCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mOneIllnessCursorAdapter.swapCursor(null);
        mIllnessCursorAdapter.swapCursor(null);
    }
}