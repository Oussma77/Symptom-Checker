package com.example.symptomchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText name = findViewById(R.id.editTextTextPersonName);
        EditText pass = findViewById(R.id.editTextTextpassword);
        Button login = findViewById(R.id.buttonlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(Login.this, "Entre Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (pass.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(Login.this, "Entre Paasword", Toast.LENGTH_SHORT).show();
                    return;
                } else if (name.getText().toString().equalsIgnoreCase("admin") &&
                        pass.getText().toString().equalsIgnoreCase("admin")) {

                    Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent familyIntent = new Intent(Login.this, MainAdmin.class);
                    startActivity(familyIntent);

                } else {
                    Toast.makeText(Login.this, "Неверный админ или пароль", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });


    }

    public void LoginUser(View view) {


    }
}