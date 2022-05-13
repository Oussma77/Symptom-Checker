package com.example.symptomchecker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.symptomchecker.data.IllnessContracr.IllnessEntry;

public class IllnessProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = IllnessProvider.class.getSimpleName();

    private static final int ILLNESSES = 100;
    private static final int ILLNESS_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * The MIME type of a {@link #-CONTENT_URI} subdirectory of a single
     * person.
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/contact";

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(IllnessContracr.CONTENT_AUTHORITY, IllnessContracr.PATH_ILLNESSES, ILLNESSES);

        sUriMatcher.addURI(IllnessContracr.CONTENT_AUTHORITY, IllnessContracr.PATH_ILLNESSES + "/#", ILLNESS_ID);
    }
    //database helper object
    private IllnessDbHelper mDBHelperIllness;

    @Override
    public boolean onCreate() {
        mDBHelperIllness = new IllnessDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase database=mDBHelperIllness.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match){
            case ILLNESSES:
                cursor = database.query(IllnessEntry.TABLE_NAME_ILLNESS,strings,s,strings1,null,null,s1);
                break;

            case ILLNESS_ID:
                s = IllnessEntry._ID + "=?";
                strings1 = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor =database.query(IllnessEntry.TABLE_NAME_ILLNESS,strings,s,strings1,null,null,s1);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }
        //notify all listeners that the data has changed for pet content URI
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ILLNESSES:
                return IllnessEntry.CONTENT_LIST_TYPE;
            case ILLNESS_ID:
                return IllnessEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ILLNESSES:
                return insertIllness(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertIllness(Uri uri, ContentValues values) {

        // Check that the name is not null
        String name = values.getAsString(IllnessEntry.COLUMN_ILLNESS_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pharmacy requires a name ...");
        }

        // Get writeable database
        SQLiteDatabase database = mDBHelperIllness.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(IllnessEntry.TABLE_NAME_ILLNESS, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        //notify all listeners that the data has changed for pet content URI
        getContext().getContentResolver().notifyChange(uri,null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDBHelperIllness.getWritableDatabase();
        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ILLNESSES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(IllnessEntry.TABLE_NAME_ILLNESS, selection, selectionArgs);
                break;
            case ILLNESS_ID:
                // Delete a single row given by the ID in the URI
                selection = IllnessEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(IllnessEntry.TABLE_NAME_ILLNESS, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ILLNESSES:
                return updateIllness(uri, contentValues, selection, selectionArgs);
            case ILLNESS_ID:
                // For the ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = IllnessEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateIllness(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updateIllness(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(IllnessEntry.COLUMN_ILLNESS_NAME)) {

            if (values.getAsString(IllnessEntry.COLUMN_ILLNESS_NAME) == null) {
                throw new IllegalArgumentException("Illness requires a name");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDBHelperIllness.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(IllnessEntry.TABLE_NAME_ILLNESS, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows updated
        return rowsUpdated;

    }

}
