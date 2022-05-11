package com.example.symptomchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void LoginUser(View view) {
        Intent familyIntent = new Intent(Login.this, MainAdmin.class);
        startActivity(familyIntent);
    }
}