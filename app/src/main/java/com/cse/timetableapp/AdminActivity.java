package com.cse.timetableapp;

import static com.cse.timetableapp.MainActivity.filename;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.cse.timetableapp.Workloads.FacultyWorkload;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    Spinner periods,days;
    String selected_period,selected_day;
    Button free_periods,free_faculty,faculty_workload,subject_faculty,changetimetable,change_faculty_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);



        change_faculty_details = findViewById(R.id.change_faculty_details);
        change_faculty_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),ChangeFacultyDetails.class));
            }
        });

        subject_faculty = findViewById(R.id.subject_faculty);
        subject_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SubjectFacultyDealing.class));
                finish();
            }
        });

        selected_period = "";
        periods = findViewById(R.id.spinner_period);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.Periods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        periods.setAdapter(adapter);
        periods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_period = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        selected_day = "";
        days = findViewById(R.id.spinner_day);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        days.setAdapter(adapter1);
        days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_day = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        free_periods = findViewById(R.id.free_classroom_button);
        free_faculty = findViewById(R.id.free_faculty_button);
        free_periods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selected_period.equals("")||selected_period.equals("Period")||selected_day.equals("")||selected_day.equals("Day"))
                    Toast.makeText(getApplicationContext(), "Select a Valid Option", Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(getApplicationContext(),FreeClasses.class);
                    intent.putExtra("period",selected_period);
                    intent.putExtra("day",selected_day);
                    startActivity(intent);
                    finish();
                }
            }
        });


        free_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_period.equals("")||selected_period.equals("Period")||selected_day.equals("")||selected_day.equals("Day")) {
                    Toast.makeText(getApplicationContext(), "Select a Valid Option", Toast.LENGTH_SHORT).show();


                    HashMap<String, ArrayList<String>> namesmap = MyApplication.namesmap;

                    for (String name : namesmap.keySet()) {
                        Log.e(name, namesmap.get(name) + " ");
                    }


                }

                else{
                    Toast.makeText(getApplicationContext(), selected_period+" "+selected_day, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),FreeFaculty.class);
                    intent.putExtra("period",selected_period);
                    intent.putExtra("day",selected_day);
                    startActivity(intent);
                    finish();
                }
            }
        });

        faculty_workload=findViewById(R.id.faculty_workload);
        faculty_workload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FacultyWorkload.class));
            }
        });
        changetimetable = findViewById(R.id.change_timetable);
        changetimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*SharedPreferences preferences = getApplication().getSharedPreferences(filename, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember","false");
                editor.apply();
                Toast.makeText(getApplicationContext(), "Logging Out...", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(),StartUpActivity.class));
                finish();*/



                startActivity(new Intent(getApplicationContext(),ModidyCurrentTimetable.class));
                finish();


            }
        });

    }

    @Override
    public void onBackPressed() {

        SharedPreferences preferences = getApplication().getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember","false");
        editor.apply();
        Toast.makeText(getApplicationContext(), "Logging Out...", Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(getApplicationContext(),StartUpActivity.class));

        finish();
    }
}