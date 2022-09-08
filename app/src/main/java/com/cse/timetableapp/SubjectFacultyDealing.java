package com.cse.timetableapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cse.timetableapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SubjectFacultyDealing extends AppCompatActivity {

    LoadingDialog loadingDialog;
    com.google.android.material.floatingactionbutton.FloatingActionButton fab;
    Spinner spinner,year_spinnner;
    ListView listView;
    List<SubjectFaculty> subjectFaculties;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_faculty_dealing);


        try {
            subjectFaculties = new ArrayList<>();
            fab = findViewById(R.id.fab_subject_faculty_dealing);
            spinner = findViewById(R.id.spinner_subject_delaing);
            listView = findViewById(R.id.subject_faculty_dealing);
            year_spinnner = findViewById(R.id.spinner_year);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                    finish();
                }
            });
            /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                    R.array.Subjects, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);*/
            year_spinnner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(year_spinnner.getSelectedItem().toString().equals("III")) {
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                R.array.Subjects, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinner.setAdapter(adapter);
                    }
                    if(year_spinnner.getSelectedItem().toString().equals("II")) {
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                R.array.secondYearsSubject, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinner.setAdapter(adapter);
                    }

                    if(year_spinnner.getSelectedItem().toString().equals("IV")) {
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                R.array.fourthYearsSubject, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinner.setAdapter(adapter);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String str = spinner.getSelectedItem().toString();
                    if (!str.equals("Subject")) {
                        subjectFaculties.clear();
                        getDataFromFirebase(str);

                    } else {
                        //Toast.makeText(SubjectFacultyDealing.this, "Please Select a Subject", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }catch (Exception e){
            //Log.e("Error",e.toString());
            //Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataFromFirebase(String str){
        loadingDialog = new LoadingDialog(this);
        loadingDialog.load();
        subjectFaculties.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FacultyDealing");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    FacultySearchItems item = snap.getValue(FacultySearchItems.class);
                    if(item.getList() != null){
                        for (String i : item.getList()) {
                            if (i.contains(str)) {
                                subjectFaculties.add(new SubjectFaculty(item.getId(), item.getName(), i));
                            }
                        }
                    }



                        /*DataSnapshot s = snap.child("Details");
                            FacultyDetails facultyDetails = null;
                            if (s.exists()) {
                                facultyDetails = s.getValue(FacultyDetails.class);
                                    String arr[] = facultyDetails.getSubject().split(",");
                                    for (int i = 0; i < arr.length; i += 1) {
                                        String sid = arr[i];
                                        if (sid.equals(str)) {
                                            String section = arr[i + 1];
                                            subjectFaculties.add(new SubjectFaculty(facultyDetails.getId(), facultyDetails.getName(), section));
                                        }
                                    }
                            }*/


                }


                SubjectFacultyAdapter subjectFacultyAdapter = new SubjectFacultyAdapter(getApplicationContext(),R.layout.subjectfacultyitem,subjectFaculties);
                listView.setAdapter(subjectFacultyAdapter);

                loadingDialog.dismisss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),AdminActivity.class));
        finish();
    }
}

/*

String arr[] = facultyDetails.getSubject().split(",");
                        for(int i=0;i<arr.length;i+=1){
                            String sid = arr[i];
                            //sid = sid.substring(1,sid.length()-1);
                            Log.e(str,sid);
                        }



                        if(sid.equals(str))
                            String section = arr[i+1];
                            section = section.substring(1,section.length()-1);
                            subjectFaculties.add( new SubjectFaculty(facultyDetails.getId(),facultyDetails.getName(),section));
 */