package com.example.symptomchecker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.symptomchecker.data.PharmacyContract.PharmacyEntry;

public class PharmacyDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PharmacyDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "myhealth3.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public PharmacyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_TABLE =  "CREATE TABLE " + PharmacyEntry.TABLE_NAME_PHARMACYS + " ("
                + PharmacyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PharmacyEntry.COLUMN_PHARMACYS_NAME + " TEXT NOT NULL, "
                + PharmacyEntry.COLUMN_PHARMACYS_WORKTIME + " TEXT NOT NULL , "
                + PharmacyEntry.COLUMN_PHARMACYS_PHONE + " TEXT NOT NULL, "
                + PharmacyEntry.COLUMN_PHARMACYS_ADDRESS + " TEXT NOT NULL );";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
