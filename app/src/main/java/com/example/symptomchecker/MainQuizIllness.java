package com.example.symptomchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.example.symptomchecker.data.IllnessContracr.IllnessEntry;

public class MainQuizIllness extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the --- data loader
     */
    private static final int LOADER = 0;
    private static final int LOADER_EXIST = 1;

    private Uri mSelectIllnessUri = IllnessEntry.CONTENT_URI;
    SymptomCursorAdapter mSymptomCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz_illness);
    }
}