package com.cse.timetableapp;

import static com.cse.timetableapp.MainActivity.filename;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cse.timetableapp.Workloads.FacultyWorkload;

import java.util.Arrays;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    Spinner periods,days;
    String selected_period,selected_day;
    androidx.cardview.widget.CardView free_periods,free_faculty;
    androidx.cardview.widget.CardView faculty_workload,subject_faculty,changetimetable,change_faculty_details,classroomTimeTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);







        /*selected_period = "";
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
                    selected_day = selected_day.substring(0,3);
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
                    selected_day = selected_day.substring(0,3);
                    intent.putExtra("day",selected_day);
                    startActivity(intent);
                    finish();
                }
            }
        });*/








        free_periods = findViewById(R.id.FreeClassesCard);

        free_periods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder;
                AlertDialog alert;
                builder = new AlertDialog.Builder(AdminActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dg = inflater.inflate(R.layout.dailogue_display,null);
                builder.setView(dg);
                Spinner periods;
                periods = dg.findViewById(R.id.spinner_period);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.Periods, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                periods.setAdapter(adapter);

                Spinner days;
                days = dg.findViewById(R.id.spinner_day);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.days, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                days.setAdapter(adapter1);


                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        String selected_day = days.getSelectedItem().toString();
                        String selected_period = periods.getSelectedItem().toString();
                        if(!selected_day.equalsIgnoreCase("Day") && !selected_period.equalsIgnoreCase("Period")){
                            Intent intent = new Intent(AdminActivity.this,FreeClasses.class);
                            intent.putExtra("period",selected_period);
                            selected_day = selected_day.substring(0,3);
                            intent.putExtra("day",selected_day);
                            startActivity(intent);
                            finish();


                        }else{
                            Toast.makeText(AdminActivity.this, "Please Choose Valid Option", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setCancelable(false);

                alert = builder.create();
                alert.show();


            }
        });


        free_faculty = findViewById(R.id.freeFacultyCard);
        free_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder;
                AlertDialog alert;
                builder = new AlertDialog.Builder(AdminActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dg = inflater.inflate(R.layout.dailogue_display,null);
                builder.setView(dg);
                Spinner periods;
                periods = dg.findViewById(R.id.spinner_period);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.Periods, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                periods.setAdapter(adapter);

                Spinner days;
                days = dg.findViewById(R.id.spinner_day);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.days, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                days.setAdapter(adapter1);


                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String selected_day = days.getSelectedItem().toString();
                        String selected_period = periods.getSelectedItem().toString();
                        if(!selected_day.equalsIgnoreCase("Day") && !selected_period.equalsIgnoreCase("Period")){


                            Intent intent = new Intent(AdminActivity.this,FreeFaculty.class);
                            intent.putExtra("period",selected_period);
                            selected_day = selected_day.substring(0,3);
                            intent.putExtra("day",selected_day);
                            startActivity(intent);
                            finish();


                        }else{
                            Toast.makeText(AdminActivity.this, "Please Choose Valid Option", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setCancelable(false);

                alert = builder.create();
                alert.show();


            }
        });

        faculty_workload=findViewById(R.id.faculty_workload);
        faculty_workload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FacultyWorkload.class));
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


        change_faculty_details = findViewById(R.id.change_faculty_details);
        change_faculty_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ChangeFacultyDetails.class));
            }
        });


        classroomTimeTable = findViewById(R.id.ClassRoomTimeTable);
        classroomTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder;
                AlertDialog alert;
                builder = new AlertDialog.Builder(AdminActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dg = inflater.inflate(R.layout.free_classes_dialogue_box,null);
                builder.setView(dg);

                AutoCompleteTextView editBox = dg.findViewById(R.id.FreeClassesAutoCOmplete);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.ClassRooms, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                editBox.setAdapter(adapter);
                String[] myResArray = getResources().getStringArray(R.array.ClassRooms);
                List<String> classes = Arrays.asList(myResArray);
                for(String str:classes)
                    System.out.println(str);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("Idigo","idigo");
                        String selected_class = editBox.getText().toString();
                        Log.e("Selected Class",selected_class);
                        if(!selected_class.equalsIgnoreCase("Select Room") && classes.contains(selected_class)){
                            Intent intent = new Intent(AdminActivity.this,ClassRoomTimeTable.class);
                            intent.putExtra("classRoom",selected_class);
                            startActivity(intent);


                        }else{
                            Toast.makeText(AdminActivity.this, "Please Choose Valid Option", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setCancelable(false);

                alert = builder.create();
                alert.show();


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
        finish();
    }
}