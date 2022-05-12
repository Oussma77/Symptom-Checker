package com.example.symptomchecker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.symptomchecker.data.HospitalContract.HospitalEntry;

public class HospitalDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = HospitalDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "myhealthh.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public HospitalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_HOSPITALS_TABLE =  "CREATE TABLE " + HospitalEntry.TABLE_NAME_HOSPITAL + " ("
                + HospitalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HospitalEntry.COLUMN_HOSPITAL_NAME + " TEXT NOT NULL, "
                + HospitalEntry.COLUMN_HOSPITAL_WORKTIME + " TEXT NOT NULL , "
                + HospitalEntry.COLUMN_HOSPITAL_ADDRESS + " TEXT NOT NULL, "
                + HospitalEntry.COLUMN_HOSPITAL_PHONE + " TEXT NOT NULL );";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_HOSPITALS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
