package com.cse.timetableapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.cse.timetableapp.R;

public class Contact extends AppCompatActivity {

    String n,f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Intent intent = getIntent();
        n = intent.getStringExtra("text");
        f = intent.getStringExtra("type");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("text", n);
        intent.putExtra("type", f);
        startActivity(intent);
        finish();
    }
}