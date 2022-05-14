package com.example.symptomchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class MainQuiz extends AppCompatActivity {

    LinearLayout linearLayout1;
    LinearLayout linearLayout2;

    Button b1;
    Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);

        linearLayout1 = (LinearLayout) findViewById(R.id.id1);
        linearLayout1 = (LinearLayout) findViewById(R.id.id2);

        b1 = (Button) findViewById(R.id.next1);
        b2 = (Button) findViewById(R.id.next2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout2.setVisibility(View.GONE);
                linearLayout1.setVisibility(View.VISIBLE);
            }
        });


    }
}