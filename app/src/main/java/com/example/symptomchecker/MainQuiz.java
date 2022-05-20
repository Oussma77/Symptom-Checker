package com.example.symptomchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.symptomchecker.data.SymptomContract;

import com.example.symptomchecker.data.SymptomContract.SymptomEntry;
import com.example.symptomchecker.data.PharmacyContract.PharmacyEntry;
import com.example.symptomchecker.data.IllnessContracr.IllnessEntry;
import com.example.symptomchecker.data.HospitalContract.HospitalEntry;
import com.example.symptomchecker.data.DoctorContract.DoctorEntry;

public class MainQuiz extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //--------------------------------------for data base
    /**
     * Identifier for the --- data loader
     */
    private static final int LOADER = 0;
    private static final int LOADER_EXIST = 1;

    private Uri mSelectSymptomUri = SymptomEntry.CONTENT_URI;
    SymptomCursorAdapter mSymptomCursorAdapter;

    private Uri mSelectDoctorUri = DoctorEntry.CONTENT_URI;
    DoctorCursorAdapter mDoctorCursorAdapter;

    private Uri mSelectHospitalUri = HospitalEntry.CONTENT_URI;
    HospitalCursorAdapter mHospitalCursorAdapter;

    private Uri mSelectPharmacyUri = PharmacyEntry.CONTENT_URI;
    PharmacyCursorAdapter mPharmacyCursorAdapter;


    /**
     * Adapter for the ListView
     */
    MainQuizOneSymptomCursorAdapter mOneSymptomCursorAdapter;
    MainQuizOneIllnessCursorAdapter mOneIllnessCursorAdapter;

    //-**********************************************end
    //type
    LinearLayout linearType;
    Button buttonType;
    RadioGroup radioGrouptype;
    RadioButton radioButtonType;
    String Type = "";

    //for who
    LinearLayout linearForWho;
    Button buttonForWho;
    RadioGroup radioGroupForWho;
    RadioButton radioButtonForWho;
    String ForWhow = "";

    //Sex
    LinearLayout linearsex;
    Button buttonSex;
    RadioGroup radioGroupSex;
    RadioButton radioButtonSex;
    String sex = "";

    //Age
    LinearLayout linearAge;
    Button buttonAge;
    EditText editTextAge;
    String Age = "";

    //select symptom
    LinearLayout linearResult;

    //allstatements
    LinearLayout linearAllstatements;


    RadioGroup radioGroupTemperature;
    RadioButton radioButtonTemperature;
    String Temperature = "";

    RadioGroup radioGroupOverweight;
    RadioButton radioButtonOverweight;
    String Overweight = "";


    RadioGroup radioGroupCigarettes;
    RadioButton radioButtonCigarettes;
    String Cigarettes = "";

    RadioGroup radioGroupCholesterol;
    RadioButton radioButtonCholesterol;
    String Cholesterol = "";

    RadioGroup radioGroupHypertension;
    RadioButton radioButtonHypertension;
    String Hypertension = "";

    RadioGroup radioGroupDiabetes;
    RadioButton radioButtonDiabetes;
    String Diabetes = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);

        linearType = (LinearLayout) findViewById(R.id.linearSelect_illness_or_symptoms);
        linearForWho = (LinearLayout) findViewById(R.id.linearForWho);
        linearsex = (LinearLayout) findViewById(R.id.linearsex);
        linearAge = (LinearLayout) findViewById(R.id.linearAge);
        linearResult = (LinearLayout) findViewById(R.id.linearResults);
        linearAllstatements = (LinearLayout) findViewById(R.id.linearAddSymptoms);


        //first select type
        linearType.setVisibility(View.VISIBLE);

        radioGrouptype = (RadioGroup) findViewById(R.id.radioGrouptype);
        buttonType = (Button) findViewById(R.id.buttonType);

        buttonType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGrouptype.getCheckedRadioButtonId();
                radioButtonType = findViewById(radioId);
                Type = radioButtonType.getText().toString().trim();

                if (Type.equalsIgnoreCase("по симптомам")) {
                    linearType.setVisibility(View.GONE);
                    linearForWho.setVisibility(View.VISIBLE);
                } else {
                    Intent numbersIntent = new Intent(MainQuiz.this, MainQuizIllness.class);
                    startActivity(numbersIntent);
                }

            }
        });




        //   for who
        radioGroupForWho = findViewById(R.id.radioGroupForWho);
        buttonForWho = (Button) findViewById(R.id.buttonForWho);

        buttonForWho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int radioId = radioGroupForWho.getCheckedRadioButtonId();
                radioButtonForWho = findViewById(radioId);
                ForWhow = radioButtonForWho.getText().toString().trim();

                linearForWho.setVisibility(View.GONE);
                linearsex.setVisibility(View.VISIBLE);

            }
        });


        //   Sex
        radioGroupSex = findViewById(R.id.radioGroupsex);
        buttonSex = (Button) findViewById(R.id.buttonsex);

        buttonSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int radioId = radioGroupSex.getCheckedRadioButtonId();
                radioButtonSex = findViewById(radioId);
                sex = radioButtonSex.getText().toString().trim();

                linearsex.setVisibility(View.GONE);
                linearAge.setVisibility(View.VISIBLE);

            }
        });

        //   Age
        editTextAge = findViewById(R.id.EditTextAge);
        buttonAge = (Button) findViewById(R.id.buttonAge);

        buttonAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Age = buttonAge.getText().toString().trim();

                linearAge.setVisibility(View.GONE);
                linearAllstatements.setVisibility(View.VISIBLE);

            }
        });


        // for list symptoms
        final wLoadingDialog wLoadingDialog = new wLoadingDialog(MainQuiz.this);
        // Find the ListView which will be populated with the hospital data
        ListView listViewOneSymptoms = (ListView) findViewById(R.id.list_search_Symptoms);

        // Setup an Adapter to create a list item for each row of hospital data in the Cursor.
        // There is no hospital data yet (until the loader finishes) so pass in null for the Cursor.
        mOneSymptomCursorAdapter = new MainQuizOneSymptomCursorAdapter(this, null);
        listViewOneSymptoms.setAdapter(mOneSymptomCursorAdapter);

        listViewOneSymptoms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mSelectSymptomUri = ContentUris.withAppendedId(SymptomEntry.CONTENT_URI, id);

                wLoadingDialog.startLoadingDialog();
                Handler handler= new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wLoadingDialog.dismissDialog();
                        linearAllstatements.setVisibility(View.GONE);
                        linearResult.setVisibility(View.VISIBLE);
                    }
                },6000);

                getLoaderManager().initLoader(LOADER_EXIST, null, MainQuiz.this);


            }
        });


        //result // Find the ListView which will be populated with the Pharmacy data
        ListView resultSymptomListView = (ListView) findViewById(R.id.list_result_Symptoms);

        mSymptomCursorAdapter = new SymptomCursorAdapter(this, null);
        resultSymptomListView.setAdapter(mSymptomCursorAdapter);


//       Список врачей в вашем районе
        ImageButton imageButtonDoctor = findViewById(R.id.img_btn_list_doctors);
        imageButtonDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent numbersIntent = new Intent(MainQuiz.this, DoctorListActivity.class);
                startActivity(numbersIntent);
            }
        });


        ImageButton imageButtonHospitals = findViewById(R.id.img_btn_list_hospitals);
        imageButtonHospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent numbersIntent = new Intent(MainQuiz.this, HospitalListActivity.class);
                startActivity(numbersIntent);
            }
        });

        ImageButton imageButtonPharmacy = findViewById(R.id.img_btn_list_pharmacy);
        imageButtonPharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent numbersIntent = new Intent(MainQuiz.this, PharmacyListActivity.class);
                startActivity(numbersIntent);
            }
        });


        getLoaderManager().initLoader(LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projection that specifies the columns from the table we care about.
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
                mSelectSymptomUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);


    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mOneSymptomCursorAdapter.swapCursor(cursor);
        mSymptomCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mOneSymptomCursorAdapter.swapCursor(null);
        mSymptomCursorAdapter.swapCursor(null);
    }
}