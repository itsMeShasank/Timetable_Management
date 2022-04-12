package com.cse.timetableapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cse.timetableapp.R;

public class AdminLogin extends AppCompatActivity {
    com.google.android.material.textfield.TextInputEditText username,password;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        username = findViewById(R.id.admin_username);
        password = findViewById(R.id.admin_password);
        button =   findViewById(R.id.admin_submit_button);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = username.getText().toString();
                String str2 = password.getText().toString();

                if(str.equalsIgnoreCase("admin") && str2.equals("cse@123")){
                    startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                    finish();
                }else
                    Toast.makeText(getApplicationContext(),"Check Username and Password",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(),StartUpActivity.class);
        startActivity(intent);
        finish();
    }
}