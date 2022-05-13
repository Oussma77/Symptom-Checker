package com.example.symptomchecker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.symptomchecker.data.SymptomContract.SymptomEntry;

public class SymptomDbHelper  extends SQLiteOpenHelper {

    public static final String LOG_TAG = SymptomDbHelper.class.getSimpleName();
    /** Name of the database file */
    private static final String DATABASE_NAME = "myhealth_symptom.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public SymptomDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_TABLE =  "CREATE TABLE " + SymptomEntry.TABLE_NAME_SYMPTOMS + " ("
                + PharmacyContract.PharmacyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SymptomEntry.COLUMN_SYMPTOM_NAME + " TEXT NOT NULL, "
                + SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION + " TEXT NOT NULL, "
                + SymptomEntry.COLUMN_SYMPTOM_NEEDHELP + " TEXT NOT NULL, "
                + SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION + " TEXT NOT NULL, "
                + SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED + " TEXT NOT NULL, "
                + SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED + " TEXT NOT NULL, "
                + SymptomEntry.COLUMN_SYMPTOM_REASONS + " TEXT NOT NULL );";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
