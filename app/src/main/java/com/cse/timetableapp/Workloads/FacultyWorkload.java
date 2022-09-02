package com.cse.timetableapp.Workloads;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cse.timetableapp.FacultyDetails;
import com.cse.timetableapp.FacultyProfile;
import com.cse.timetableapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FacultyWorkload extends AppCompatActivity {

    ListView listView;
    List<WorkLoads> workLoads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_workload);

        listView = findViewById(R.id.WorkloadlistView);
        workLoads = new ArrayList<>();
        getWorkLoad();
    }

    public void getWorkLoad(){
        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference("FacultyDetails");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                workLoads.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){

                    String str = "siddhu";
                    FacultyDetails facultyDetails= new FacultyDetails("1","siddhu","10");
                    int count = 0;
                    for(DataSnapshot snap:snapshot1.getChildren()){
                        if(snap.getKey().equals("Details")) {
                            facultyDetails = snap.getValue(FacultyDetails.class);
                        }else
                            count += snap.getChildrenCount();
                    }
                    String faculty_id = snapshot1.getKey();
                    if(!faculty_id.contains("E0") || faculty_id.contains("EEE0")){
                    WorkLoads work = new WorkLoads(faculty_id,count+"",facultyDetails.getName());
                    workLoads.add(work);
                    }
                }
                Log.e("After",workLoads.toString());
                assignVals();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void assignVals(){
        FacultyWorkloadAdapter adapter = new FacultyWorkloadAdapter(getApplicationContext(), R.layout.workload_item,workLoads);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WorkLoads work = workLoads.get(i);
                Intent intent = new Intent(getApplicationContext(), FacultyProfile.class);
                intent.putExtra("faculty",work);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}