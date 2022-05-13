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
import android.widget.Toast;

import com.example.symptomchecker.data.SymptomContract.SymptomEntry;

public class SymptomEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    /** Identifier for the doctor data loader */
    private static final int EXISTING_SYMPTOM_LOADER = 0;

    /** Content URI for the existing doctor (null if it's a new doctor) */
    private Uri mCurrentSymptomUri;


    private EditText name_symptomTextView;
    private EditText desc_symptomTextView;
    private EditText need_help_symptomTextView;
    private EditText classification_symptomTextView;
    private EditText isrecommended_symptomTextView;
    private EditText isnotrecommended_symptomTextView;
    private EditText reasons_symptomTextView;



    /** Boolean flag that keeps track of whether the doctor has been edited (true) or not (false) */
    private boolean mSymptomHasChanged = false;


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mSymptomHasChanged = true;
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_editor);

        Intent intent = getIntent();
        mCurrentSymptomUri = intent.getData();

        if (mCurrentSymptomUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_doctor));
             invalidateOptionsMenu();
        } else {
            // Initialize a loader to read the doctor data from the database
            // and display the current values in the editor
            setTitle(getString(R.string.editor_activity_title_edit_doctor));
            getLoaderManager().initLoader(EXISTING_SYMPTOM_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        name_symptomTextView = (EditText) findViewById(R.id.edit_symptom_name);
        desc_symptomTextView = (EditText) findViewById(R.id.edit_symptom_desc);
        need_help_symptomTextView = (EditText) findViewById(R.id.edit_symptom_help);
        classification_symptomTextView = (EditText) findViewById(R.id.edit_symptom_classification);
        isrecommended_symptomTextView = (EditText) findViewById(R.id.edit_symptom_isnrecommended);
        isnotrecommended_symptomTextView = (EditText) findViewById(R.id.edit_symptom_isnotrecommended);
        reasons_symptomTextView = (EditText) findViewById(R.id.edit_symptom_reasons);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        name_symptomTextView.setOnTouchListener(mTouchListener);
        desc_symptomTextView.setOnTouchListener(mTouchListener);
        need_help_symptomTextView.setOnTouchListener(mTouchListener);
        classification_symptomTextView.setOnTouchListener(mTouchListener);
        isrecommended_symptomTextView.setOnTouchListener(mTouchListener);
        isnotrecommended_symptomTextView.setOnTouchListener(mTouchListener);
        reasons_symptomTextView.setOnTouchListener(mTouchListener);


    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mSymptomHasChanged) {
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
        if (mCurrentSymptomUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the hosp table
        String[] projection = {
                SymptomEntry._ID,
                SymptomEntry.COLUMN_SYMPTOM_NAME,
                SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION,
                SymptomEntry.COLUMN_SYMPTOM_NEEDHELP,
                SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION,
                SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED,
                SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED,
                SymptomEntry.COLUMN_SYMPTOM_REASONS,
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentSymptomUri,   // Provider content URI to query
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
            int nameColumnIndex = cursor.getColumnIndex(SymptomEntry.COLUMN_SYMPTOM_NAME);
            int descColumnIndex = cursor.getColumnIndex(SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION);
            int need_helpColumnIndex = cursor.getColumnIndex(SymptomEntry.COLUMN_SYMPTOM_NEEDHELP);
            int classificationColumnIndex = cursor.getColumnIndex(SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION);
            int isrecommendedColumnIndex = cursor.getColumnIndex(SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED);
            int isnotrecommendedColumnIndex = cursor.getColumnIndex(SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED);
            int reasonsColumnIndex = cursor.getColumnIndex(SymptomEntry.COLUMN_SYMPTOM_REASONS);

            // Read the hosp attributes from the Cursor for the current hos
            String a1 = cursor.getString(nameColumnIndex);
            String a2 = cursor.getString(descColumnIndex);
            String a3 = cursor.getString(need_helpColumnIndex);
            String a4 = cursor.getString(classificationColumnIndex);
            String a5 = cursor.getString(isrecommendedColumnIndex);
            String a6 = cursor.getString(isnotrecommendedColumnIndex);
            String a7 = cursor.getString(reasonsColumnIndex);

            name_symptomTextView.setText(a1);
            desc_symptomTextView.setText(a2);
            need_help_symptomTextView.setText(a3);
            classification_symptomTextView.setText(a4);
            isrecommended_symptomTextView.setText(a5);
            isnotrecommended_symptomTextView.setText(a6);
            reasons_symptomTextView.setText(a7);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        name_symptomTextView.setText("");
        desc_symptomTextView.setText("");
        need_help_symptomTextView.setText("");
        classification_symptomTextView.setText("");
        isrecommended_symptomTextView.setText("");
        isnotrecommended_symptomTextView.setText("");
        reasons_symptomTextView.setText("");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                saveSymptom();
                Intent intent = new Intent(SymptomEditorActivity.this, SymptomActivity.class);
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
                if (!mSymptomHasChanged) {
                    NavUtils.navigateUpFromSameTask(SymptomEditorActivity.this);
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
                                NavUtils.navigateUpFromSameTask(SymptomEditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Save and delete
    private void saveSymptom() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = name_symptomTextView.getText().toString().trim();
        String a2 = desc_symptomTextView.getText().toString().trim();
        String a3 = need_help_symptomTextView.getText().toString().trim();
        String a4 = classification_symptomTextView.getText().toString().trim();
        String a5 = isrecommended_symptomTextView.getText().toString().trim();
        String a6 = isnotrecommended_symptomTextView.getText().toString().trim();
        String a7 = reasons_symptomTextView.getText().toString().trim();



        // Check if this is supposed to be a new pet
        // and check if all the fields in the editor are blank
        if (mCurrentSymptomUri == null &&
                TextUtils.isEmpty(nameString) &&
                TextUtils.isEmpty(a2) && TextUtils.isEmpty(a3) &&  TextUtils.isEmpty(a4) && TextUtils.isEmpty(a5) &&
                TextUtils.isEmpty(a6) &&  TextUtils.isEmpty(a7) ) {
            // Since no fields were modified, we can return early without creating a new doctor.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(SymptomEntry.COLUMN_SYMPTOM_NAME, nameString);
        values.put(SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION, a2);
        values.put(SymptomEntry.COLUMN_SYMPTOM_NEEDHELP,a3);
        values.put(SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION, a4);
        values.put(SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED, a5);
        values.put(SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED, a6);
        values.put(SymptomEntry.COLUMN_SYMPTOM_REASONS, a7);


        // Determine if this is a new or existing pet by checking if mCurrentPetUri is null or not
        if (mCurrentSymptomUri == null) {
            // This is a NEW pet, so insert a new pet into the provider,
            // returning the content URI for the new pet.
            Uri newUri = getContentResolver().insert(SymptomEntry.CONTENT_URI, values);

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
            int rowsAffected = getContentResolver().update(mCurrentSymptomUri, values, null, null);

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

    private void deleteSymptom() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentSymptomUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentSymptomUri, null, null);

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

    /* dialog */
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
                deleteSymptom();
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

}