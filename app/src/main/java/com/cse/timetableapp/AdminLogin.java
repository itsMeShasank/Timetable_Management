package com.cse.timetableapp;

import static com.cse.timetableapp.MainActivity.filename;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
                startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                finish();

                /*if(str.equalsIgnoreCase("admin") && str2.equals("cse@123")){
                    startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                    finish();
                }else
                    Toast.makeText(getApplicationContext(),"Check Username and Password",Toast.LENGTH_SHORT).show();*/
            }
        });

    }

    @Override
    public void onBackPressed() {

        SharedPreferences preferences = getApplication().getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember","false");
        editor.apply();
        startActivity(new Intent(getApplicationContext(),StartUpActivity.class));
        finish();
    }
}