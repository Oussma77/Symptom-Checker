package com.example.symptomchecker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.symptomchecker.data.DoctorContract.DoctorEntry;

import androidx.annotation.Nullable;

public class DoctorDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DoctorDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "myhealth.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public DoctorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_DOCTORS_TABLE =  "CREATE TABLE " + DoctorEntry.TABLE_NAME_DOCTOR + " ("
                + DoctorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DoctorEntry.COLUMN_DOCTOR_NAME + " TEXT NOT NULL, "
                + DoctorEntry.COLUMN_DOCTOR_AGE + " INTEGER NOT NULL DEFAULT 30, "
                + DoctorEntry.COLUMN_DOCTOR_PHONE + " TEXT NOT NULL, "
                + DoctorEntry.COLUMN_DOCTOR_ADDRESS + " TEXT NOT NULL, "
                + DoctorEntry.COLUMN_DOCTOR_EMAIL + " TEXT NOT NULL );";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_DOCTORS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
