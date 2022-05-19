package com.example.symptomchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class MainQuiz extends AppCompatActivity {

    RadioGroup radioGroupForWho;
    RadioButton radioButtonForWho;
    RadioButton radioButtonsemoene;

    String ForWhow = "";

    LinearLayout linearLayoutForWho;
    LinearLayout linearLayout2;

    Button buttonForWho;
    Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);


        linearLayoutForWho = (LinearLayout) findViewById(R.id.linearForWho);
//        linearLayoutForWho.setVisibility(View.VISIBLE);
        radioGroupForWho = findViewById(R.id.radioGroupForWho);
        buttonForWho = (Button) findViewById(R.id.buttonForWho);

        buttonForWho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutForWho.setVisibility(View.GONE);
                //linearLayout2.setVisibility(View.VISIBLE);

                int radioId = radioGroupForWho.getCheckedRadioButtonId();
                radioButtonForWho = findViewById(radioId);
                ForWhow = radioButtonForWho.getText().toString().trim();
                Log.v("MainQuiz1" , "Radio = " + ForWhow);

            }
        });

    }

}