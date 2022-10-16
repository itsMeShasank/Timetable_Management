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
import java.util.Locale;

public class FreeClasses extends AppCompatActivity {

    DatabaseReference databaseReference;
    HashMap<String,String> room_details;
    ArrayList<String> rooms;
    String period,day;
    ListView listView;
    LoadingDialog loadingDialog;
    com.google.android.material.floatingactionbutton.FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_classes);

        loadingDialog = new LoadingDialog(this);
        loadingDialog.load();

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
        databaseReference = FirebaseDatabase.getInstance().getReference("StudentDetails");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    StudentData studentData = snap.child(day).child(period).getValue(StudentData.class);
                    if(!(studentData==null)){
                        Log.e(studentData.getSec(),studentData.getRoom());
                        removeFromLists(studentData.getRoom());
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

        loadingDialog.dismisss();
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
        room_details.put("VPTF-9","CLASS");
        room_details.put("VPTF-12","CLASS");

        room_details.put("CC-Lab","LAB");
        room_details.put("Srujana Seminar Hall - H Block","CLASS");
        room_details.put("NTR Library (3rd floor)","CLASS");
        room_details.put("NTR Library(2nd floor Lab 03)","LAB");

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
        rooms.add("VPTF-12");

        rooms.add("CC-Lab");
        rooms.add("Srujana Seminar Hall - H Block");
        rooms.add("NTR Library (3rd floor)");
        rooms.add("NTR Library(2nd floor Lab 03)");



        /*
        *
        *   <string-array name="ClassRooms">
        <item>Select Room</item>
       <item>VPTF-01</item>
        <item>VPTF-02</item>
        <item>VPTF-03</item>
       <item>VPTF-04</item>
        <item>VPTF-05</item>
       <item>VPTF-06</item>
       <item>VPTF-07</item>
        <item>VPTF-08</item>
       <item>VPTF-09</item>
        <item>VPTF 12</item>
        <item>VPSF-01</item>
        <item>VPSF-02</item>
        <item>VPSF-03</item>
        <item>VPSF-04</item>
        <item>VPSF-05</item>
        <item>VPSF-06</item>
        <item>VPSF-07</item>
        <item>VPSF-08</item>
        <item>VPSF-09</item>
        <item>VPSF-10</item>
        <item>VPSF-11</item>
        <item>VPSF-12</item>
        <item>CC-Lab</item>
        <item>Srujana Seminar Hall - H Block</item>
        <item>NTR Library (3rd floor)</item>
        <item>NTR Library(2nd floor Lab 03)</item>
        <item>Library - Lab 03</item>
        <item>Srujana Hall -  H - Block</item>
        <item>Srujana Hall</item>

    </string-array>
        *
        *
        *
        *
        *
        *
        *
        * */
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),AdminActivity.class));
        finish();
    }


    private void removeFromLists(String str){
        for(int i=0;i<rooms.size();i++){
            String temp = rooms.get(i);
            str = str.replaceAll("[-,.0 ]","").toLowerCase(Locale.ROOT);
            temp = temp.replaceAll("[-,.0 ]","").toLowerCase(Locale.ROOT);
            if(str.equalsIgnoreCase(temp)){
                rooms.remove(i);
                return;
            }
        }
    }
}