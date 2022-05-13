package com.example.symptomchecker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.symptomchecker.data.IllnessContracr.IllnessEntry;

public class IllnessDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = IllnessProvider.class.getSimpleName();
    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "myhealth_illness.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public IllnessDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_TABLE = "CREATE TABLE " + IllnessEntry.TABLE_NAME_ILLNESS + " ("
                + IllnessEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IllnessEntry.COLUMN_ILLNESS_NAME + " TEXT NOT NULL, "
                + IllnessEntry.COLUMN_ILLNESS_TYPE + " TEXT NOT NULL, "
                + IllnessEntry.COLUMN_ILLNESS_DESC + " TEXT NOT NULL, "
                + IllnessEntry.COLUMN_ILLNESS_CAUSES + " TEXT NOT NULL, "
                + IllnessEntry.COLUMN_ILLNESS_RISK + " TEXT NOT NULL, "
                + IllnessEntry.COLUMN_ILLNESS_SYMPTOMS + " TEXT NOT NULL, "
                + IllnessEntry.COLUMN_ILLNESS_MEDICINES + " TEXT NOT NULL, "
                + IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR + " TEXT NOT NULL );";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}