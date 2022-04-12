package com.cse.timetableapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.cse.timetableapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FreeClasses extends AppCompatActivity {

    DatabaseReference databaseReference;
    HashMap<String,String> room_details;
    ArrayList<String> rooms;
    String period,day;
    ListView listView;
    com.google.android.material.floatingactionbutton.FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_classes);


        fab = findViewById(R.id.fab_free_classes);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                finish();

            }
        });
        period="";
        day ="";
        Intent intent =getIntent();
        period = intent.getStringExtra("period");
        day = intent.getStringExtra("day");


        rooms = new ArrayList<>();
        assignVals();
        databaseReference = FirebaseDatabase.getInstance().getReference("ClassDetails");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    StudentData studentData = snap.child(day).child(period).getValue(StudentData.class);
                    if(!(studentData==null)){
                    Log.e(studentData.getSec(),studentData.getRoom());
                    rooms.remove(studentData.getRoom());
                    }
                }
                assignData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void assignData() {
        listView = findViewById(R.id.listView);
        List<String> class_types = new ArrayList<>();
        for(String str:rooms)
            class_types.add(str+" "+room_details.get(str));
        FacultyAdapter facultyAdapter= new FacultyAdapter(getApplicationContext(),class_types);
        listView.setAdapter(facultyAdapter);
    }


    private void assignVals() {
        room_details = new HashMap<String,String>();
        room_details.put("VPSF-1","LAB");
        room_details.put("VPSF-2","LAB");
        room_details.put("VPSF-3","CLASS");
        room_details.put("VPSF-4","LAB");
        room_details.put("VPSF-5","CLASS");
        room_details.put("VPSF-6","CLASS");
        room_details.put("VPSF-7","LAB");
        room_details.put("VPSF-8","CLASS");
        room_details.put("VPSF-9","CLASS");
        room_details.put("VPSF-10","CLASS");
        room_details.put("VPSF-11","CLASS");
        room_details.put("VPSF-12","CLASS");

        room_details.put("VPTF-1","LAB");
        room_details.put("VPTF-2","LAB");
        room_details.put("VPTF-3","LAB");
        room_details.put("VPTF-4","LAB");
        room_details.put("VPTF-5","CLASS");
        room_details.put("VPTF-6","CLASS");
        room_details.put("VPTF-7","CLASS");
        room_details.put("VPTF-8","CLASS");
        rooms.add("VPSF-1");
        rooms.add("VPSF-2");
        rooms.add("VPSF-3");
        rooms.add("VPSF-4");
        rooms.add("VPSF-5");
        rooms.add("VPSF-6");
        rooms.add("VPSF-7");
        rooms.add("VPSF-8");
        rooms.add("VPSF-9");
        rooms.add("VPSF-10");
        rooms.add("VPSF-11");
        rooms.add("VPSF-12");

        rooms.add("VPTF-1");
        rooms.add("VPTF-2");
        rooms.add("VPTF-3");
        rooms.add("VPTF-4");
        rooms.add("VPTF-5");
        rooms.add("VPTF-6");
        rooms.add("VPTF-7");
        rooms.add("VPTF-8");
        rooms.add("VPTF-9");
        rooms.add("VPTF-10");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),AdminActivity.class));
        finish();
    }
}