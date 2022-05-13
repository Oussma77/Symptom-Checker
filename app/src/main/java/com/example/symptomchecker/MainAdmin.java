package com.example.symptomchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainAdmin extends AppCompatActivity {

    Button addDoctor;
    Button addHospital;
    Button addPharmacy;
    Button addSymptom;
    Button addIllness;
    Button addMesdicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        //Add Doctor
        addDoctor = (Button) findViewById(R.id.add_doctor);
        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent numbersIntent = new Intent(MainAdmin.this, DoctorActivity.class);
                startActivity(numbersIntent);
            }
        });

        //Add hospital
        addHospital = (Button) findViewById(R.id.add_hospital);
        addHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent numbersIntent1 = new Intent(MainAdmin.this, HospitalActivity.class);
                startActivity(numbersIntent1);
            }
        });

        addPharmacy = (Button) findViewById(R.id.add_pharmacy);
        addPharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent numbersIntent1 = new Intent(MainAdmin.this, PharmacyActivity.class);
                startActivity(numbersIntent1);
            }
        });

        addSymptom = (Button) findViewById(R.id.add_symptom);
        addSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent numbersIntent1 = new Intent(MainAdmin.this, SymptomActivity.class);
                startActivity(numbersIntent1);
            }
        });


    }
}