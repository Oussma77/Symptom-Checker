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

import com.example.symptomchecker.data.PharmacyContract.PharmacyEntry;

public class PharmacyProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = PharmacyProvider.class.getSimpleName();

    private static final int PHARMACYS = 100;
    private static final int PHARMACY_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * The MIME type of a {@link #-CONTENT_URI} subdirectory of a single
     * person.
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/contact";

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(PharmacyContract.CONTENT_AUTHORITY, PharmacyContract.PATH_PHARMACYS, PHARMACYS);

        sUriMatcher.addURI(PharmacyContract.CONTENT_AUTHORITY, PharmacyContract.PATH_PHARMACYS + "/#", PHARMACY_ID);
    }
    //database helper object
    private PharmacyDbHelper mDBHelperPharmacy;

    @Override
    public boolean onCreate() {
        mDBHelperPharmacy = new PharmacyDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase database=mDBHelperPharmacy.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match){
            case PHARMACYS:
                cursor = database.query(PharmacyEntry.TABLE_NAME_PHARMACYS,strings,s,strings1,null,null,s1);
                break;

            case PHARMACY_ID:
                s = PharmacyEntry._ID + "=?";
                strings1 = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor =database.query(PharmacyEntry.TABLE_NAME_PHARMACYS,strings,s,strings1,null,null,s1);
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
            case PHARMACYS:
                return PharmacyEntry.CONTENT_LIST_TYPE;
            case PHARMACY_ID:
                return PharmacyEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PHARMACYS:
                return insertPharmacy(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPharmacy(Uri uri, ContentValues values) {

        // Check that the name is not null
        String name = values.getAsString(PharmacyEntry.COLUMN_PHARMACYS_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pharmacy requires a name ...");
        }

        // Get writeable database
        SQLiteDatabase database = mDBHelperPharmacy.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(PharmacyEntry.TABLE_NAME_PHARMACYS, null, values);
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
        SQLiteDatabase database = mDBHelperPharmacy.getWritableDatabase();
        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PHARMACYS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(PharmacyEntry.TABLE_NAME_PHARMACYS, selection, selectionArgs);
                break;
            case PHARMACY_ID:
                // Delete a single row given by the ID in the URI
                selection = PharmacyEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(PharmacyEntry.TABLE_NAME_PHARMACYS, selection, selectionArgs);
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
            case PHARMACYS:
                return updatePharmacy(uri, contentValues, selection, selectionArgs);
            case PHARMACY_ID:
                // For the ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PharmacyEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePharmacy(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updatePharmacy(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(PharmacyEntry.COLUMN_PHARMACYS_NAME)) {

            if (values.getAsString(PharmacyEntry.COLUMN_PHARMACYS_NAME) == null) {
                throw new IllegalArgumentException("Pharmacy requires a name");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDBHelperPharmacy.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(PharmacyEntry.TABLE_NAME_PHARMACYS, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows updated
        return rowsUpdated;

    }
}
