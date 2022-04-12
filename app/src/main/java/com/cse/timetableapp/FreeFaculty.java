package com.cse.timetableapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FreeFaculty extends AppCompatActivity {

    ArrayList<String> array;
    DatabaseReference databaseReference;
    ArrayList<String> rooms;
    List<FreeFacultyLoader> list;
    ListView listView;
    String period,day;
    com.google.android.material.floatingactionbutton.FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_faculty);

        array = new ArrayList<>();
        rooms = new ArrayList<>();
        list = new ArrayList<>();

        fab = findViewById(R.id.fab_free_faculty);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                //finish();

            }
        });

        period="";
        day ="";
        Intent intent =getIntent();
        period = intent.getStringExtra("period");
        day = intent.getStringExtra("day");

        listView = findViewById(R.id.FacultylistView);


        try {
            //Faculty Free Data
            databaseReference = FirebaseDatabase.getInstance().getReference("FacultyDetails");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        DataSnapshot s = snap.child(day);
                        DataSnapshot sid = snap.child("Details");
                        FacultyDetails facultyDetails = sid.getValue(FacultyDetails.class);
                        if (!s.hasChild(period)) {
                            array.add(snap.getKey());
                            String id = snap.getKey();
                            if (!id.contains("E0"))
                                list.add(new FreeFacultyLoader(snap.getKey(), facultyDetails.getName()));
                        }

                    }
                    FacultyAdapter facultyAdapter = new FacultyAdapter(getApplicationContext(), array);
                    FreeFacultyAdapter freeFacultyAdapter = new FreeFacultyAdapter(getApplicationContext(), R.layout.free_faculty_item, list);
                    listView.setAdapter(freeFacultyAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Log.e("Exception",e.toString());
        }


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),AdminActivity.class));
        finish();
    }
}