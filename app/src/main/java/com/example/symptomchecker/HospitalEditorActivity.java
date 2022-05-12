package com.example.symptomchecker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.symptomchecker.data.HospitalContract.HospitalEntry;

public class HospitalEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the doctor data loader */
    private static final int EXISTING_HOSPITAL_LOADER = 0;

    /** Content URI for the existing doctor (null if it's a new doctor) */
    private Uri mCurrentHospitalUri;


    private EditText mNameEditText;
    private EditText mTimeEditText;
    private EditText mPhoneEditText;
    private EditText mAddressEditText;


    /** Boolean flag that keeps track of whether the doctor has been edited (true) or not (false) */
    private boolean mHospitalHasChanged = false;


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mHospitalHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_editor);

        Intent intent = getIntent();
        mCurrentHospitalUri = intent.getData();

        if (mCurrentHospitalUri == null) {
            // This is a new doctor, so change the app bar to say "Add a doctor"
            setTitle(getString(R.string.editor_activity_title_new_doctor));
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing doctor, so change app bar to say "Edit doctor"
            setTitle(getString(R.string.editor_activity_title_edit_doctor));
            // Initialize a loader to read the doctor data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_HOSPITAL_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_hospital_name);
        mTimeEditText = (EditText) findViewById(R.id.edit_hospital_workTime);
        mPhoneEditText = (EditText) findViewById(R.id.edit_hospital_phone);
        mAddressEditText = (EditText) findViewById(R.id.edit_hospital_address);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mTimeEditText.setOnTouchListener(mTouchListener);
        mPhoneEditText.setOnTouchListener(mTouchListener);
        mAddressEditText.setOnTouchListener(mTouchListener);


    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mHospitalHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_doctor_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentHospitalUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                saveHospital();
                Intent intent = new Intent(HospitalEditorActivity.this, HospitalActivity.class);
                startActivity(intent);
                 return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mHospitalHasChanged) {
                    NavUtils.navigateUpFromSameTask(HospitalEditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(HospitalEditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the hosp table
        String[] projection = {
                HospitalEntry._ID,
                HospitalEntry.COLUMN_HOSPITAL_NAME,
                HospitalEntry.COLUMN_HOSPITAL_WORKTIME,
                HospitalEntry.COLUMN_HOSPITAL_PHONE,
                HospitalEntry.COLUMN_HOSPITAL_ADDRESS};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentHospitalUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {

            // Find the columns of hosp attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(HospitalEntry.COLUMN_HOSPITAL_NAME);
            int timeColumnIndex = cursor.getColumnIndex(HospitalEntry.COLUMN_HOSPITAL_WORKTIME);
            int phoneColumnIndex = cursor.getColumnIndex(HospitalEntry.COLUMN_HOSPITAL_PHONE);
            int addressColumnIndex = cursor.getColumnIndex(HospitalEntry.COLUMN_HOSPITAL_ADDRESS);

            // Read the hosp attributes from the Cursor for the current hos
            String hospitalName = cursor.getString(nameColumnIndex);
            String hospitalTime = cursor.getString(timeColumnIndex);
            String hospitalPhone = cursor.getString(phoneColumnIndex);
            String hospitalAddress = cursor.getString(addressColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(hospitalName);
            mTimeEditText.setText(hospitalTime);
            mPhoneEditText.setText(hospitalPhone);
            mAddressEditText.setText(hospitalAddress);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mTimeEditText.setText("");
        mPhoneEditText.setText("");
        mAddressEditText.setText("");
    }


    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteHospital();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteHospital() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentHospitalUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentHospitalUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_doctor_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_doctor_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }

    private void saveHospital() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String timeString = mTimeEditText.getText().toString().trim();
        String phoneString = mPhoneEditText.getText().toString().trim();
        String addressString = mAddressEditText.getText().toString().trim();



        // Check if this is supposed to be a new pet
        // and check if all the fields in the editor are blank
        if (mCurrentHospitalUri == null &&
                TextUtils.isEmpty(nameString) &&
                TextUtils.isEmpty(phoneString) && TextUtils.isEmpty(addressString) &&
                TextUtils.isEmpty(timeString) ) {
            // Since no fields were modified, we can return early without creating a new doctor.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(HospitalEntry.COLUMN_HOSPITAL_NAME, nameString);
        values.put(HospitalEntry.COLUMN_HOSPITAL_WORKTIME, timeString);
        values.put(HospitalEntry.COLUMN_HOSPITAL_PHONE, phoneString);
        values.put(HospitalEntry.COLUMN_HOSPITAL_ADDRESS, addressString);


        // Determine if this is a new or existing pet by checking if mCurrentPetUri is null or not
        if (mCurrentHospitalUri == null) {
            // This is a NEW pet, so insert a new pet into the provider,
            // returning the content URI for the new pet.
            Uri newUri = getContentResolver().insert(HospitalEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_doctor_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_doctor_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentPetUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentPetUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentHospitalUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_doctor_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_doctor_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


}