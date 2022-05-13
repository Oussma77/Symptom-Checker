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

import com.example.symptomchecker.data.IllnessContracr.IllnessEntry;

public class IllnessEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    /** Identifier for the doctor data loader */
    private static final int EXISTING_ILLNESS_LOADER = 0;

    /** Content URI for the existing doctor (null if it's a new doctor) */
    private Uri mCurrentIllnessUri;


    private EditText nameTextView;
    private EditText typeTextView;
    private EditText descTextView;
    private EditText causesTextView;
    private EditText riskTextView;
    private EditText symptomsTextView;
    private EditText medicinesTextView;
    private EditText askTextView;



    /** Boolean flag that keeps track of whether the doctor has been edited (true) or not (false) */
    private boolean mIllnesHasChanged = false;


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mIllnesHasChanged = true;
            return false;
        }


    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illness_editor);

        Intent intent = getIntent();
        mCurrentIllnessUri = intent.getData();

        if (mCurrentIllnessUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_doctor));
            invalidateOptionsMenu();
        } else {
            // Initialize a loader to read the doctor data from the database
            // and display the current values in the editor
            setTitle(getString(R.string.editor_activity_title_edit_doctor));
            getLoaderManager().initLoader(EXISTING_ILLNESS_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        nameTextView = (EditText) findViewById(R.id.edit_illness_name);
        typeTextView = (EditText) findViewById(R.id.edit_illness_type);
        descTextView = (EditText) findViewById(R.id.edit_illness_desc);
        causesTextView = (EditText) findViewById(R.id.edit_illness_causes);
        riskTextView = (EditText) findViewById(R.id.edit_illness_risk);
        symptomsTextView = (EditText) findViewById(R.id.edit_illness_symptoms);
        medicinesTextView = (EditText) findViewById(R.id.edit_illness_medicines);
        askTextView = (EditText) findViewById(R.id.edit_illness_ask);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        nameTextView.setOnTouchListener(mTouchListener);
        typeTextView.setOnTouchListener(mTouchListener);
        descTextView.setOnTouchListener(mTouchListener);
        causesTextView.setOnTouchListener(mTouchListener);
        riskTextView.setOnTouchListener(mTouchListener);
        symptomsTextView.setOnTouchListener(mTouchListener);
        medicinesTextView.setOnTouchListener(mTouchListener);
        askTextView.setOnTouchListener(mTouchListener);

    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mIllnesHasChanged) {
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
        if (mCurrentIllnessUri == null) {
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
                IllnessEntry._ID,
                IllnessEntry.COLUMN_ILLNESS_NAME,
                IllnessEntry.COLUMN_ILLNESS_TYPE,
                IllnessEntry.COLUMN_ILLNESS_DESC,
                IllnessEntry.COLUMN_ILLNESS_CAUSES,
                IllnessEntry.COLUMN_ILLNESS_RISK,
                IllnessEntry.COLUMN_ILLNESS_SYMPTOMS,
                IllnessEntry.COLUMN_ILLNESS_MEDICINES,
                IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR,
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentIllnessUri,   // Provider content URI to query
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
            int ColumnIndex1 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_NAME);
            int ColumnIndex2 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_TYPE);
            int ColumnIndex3 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_DESC);
            int ColumnIndex4 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_CAUSES);
            int ColumnIndex5 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_RISK);
            int ColumnIndex6 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_SYMPTOMS);
            int ColumnIndex7 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_MEDICINES);
            int ColumnIndex8 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR);

            // Read the hosp attributes from the Cursor for the current hos
            String a1 = cursor.getString(ColumnIndex1);
            String a2 = cursor.getString(ColumnIndex2);
            String a3 = cursor.getString(ColumnIndex3);
            String a4 = cursor.getString(ColumnIndex4);
            String a5 = cursor.getString(ColumnIndex5);
            String a6 = cursor.getString(ColumnIndex6);
            String a7 = cursor.getString(ColumnIndex7);
            String a8 = cursor.getString(ColumnIndex8);

            nameTextView.setText(a1);
            typeTextView.setText(a2);
            descTextView.setText(a3);
            causesTextView.setText(a4);
            riskTextView.setText(a5);
            symptomsTextView.setText(a6);
            medicinesTextView.setText(a7);
            askTextView.setText(a7);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameTextView.setText("");
        typeTextView.setText("");
        descTextView.setText("");
        causesTextView.setText("");
        riskTextView.setText("");
        symptomsTextView.setText("");
        medicinesTextView.setText("");
        askTextView.setText("");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                saveIllness();
                Intent intent = new Intent(IllnessEditorActivity.this, IllnessActivity.class);
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
                if (!mIllnesHasChanged) {
                    NavUtils.navigateUpFromSameTask(IllnessEditorActivity.this);
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
                                NavUtils.navigateUpFromSameTask(IllnessEditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Save and delete
    private void saveIllness() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = nameTextView.getText().toString().trim();
        String a2 = typeTextView.getText().toString().trim();
        String a3 = descTextView.getText().toString().trim();
        String a4 = causesTextView.getText().toString().trim();
        String a5 = riskTextView.getText().toString().trim();
        String a6 = symptomsTextView.getText().toString().trim();
        String a7 = medicinesTextView.getText().toString().trim();
        String a8 = askTextView.getText().toString().trim();



        // Check if this is supposed to be a new pet
        // and check if all the fields in the editor are blank
        if (mCurrentIllnessUri == null &&
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
        values.put(IllnessEntry.COLUMN_ILLNESS_NAME, nameString);
        values.put(IllnessEntry.COLUMN_ILLNESS_TYPE, a2);
        values.put(IllnessEntry.COLUMN_ILLNESS_DESC, a3);
        values.put(IllnessEntry.COLUMN_ILLNESS_CAUSES, a4);
        values.put(IllnessEntry.COLUMN_ILLNESS_RISK, a5);
        values.put(IllnessEntry.COLUMN_ILLNESS_SYMPTOMS, a6);
        values.put(IllnessEntry.COLUMN_ILLNESS_MEDICINES, a7);
        values.put(IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR, a8);


        // Determine if this is a new or existing pet by checking if mCurrentPetUri is null or not
        if (mCurrentIllnessUri == null) {
            // This is a NEW pet, so insert a new pet into the provider,
            // returning the content URI for the new pet.
            Uri newUri = getContentResolver().insert(IllnessEntry.CONTENT_URI, values);

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
            int rowsAffected = getContentResolver().update(mCurrentIllnessUri, values, null, null);

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

    private void deleteIllnes() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentIllnessUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentIllnessUri, null, null);

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
                deleteIllnes();
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