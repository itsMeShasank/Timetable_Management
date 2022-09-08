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
import com.cse.timetableapp.FacultySearchItems;
import com.cse.timetableapp.LoadingDialog;
import com.cse.timetableapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FacultyWorkload extends AppCompatActivity {

    LoadingDialog loadingDialog;
    ListView listView;
    List<WorkLoads> workLoads;
    ArrayList<String> facultyids;
    HashMap<String, FacultySearchItems> facultyDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_workload);

        listView = findViewById(R.id.WorkloadlistView);
        workLoads = new ArrayList<>();
        facultyDetails = new HashMap<>();
        facultyids = new ArrayList<>();
        loadingDialog = new LoadingDialog(this);
        loadingDialog.load();
        getFacultyIntoHashMap();
//        getWorkLoad();
    }


    private  void getFacultyIntoHashMap(){
        FirebaseDatabase.getInstance().getReference().child("FacultyDealing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    FacultySearchItems child = snap.getValue(FacultySearchItems.class);
                    facultyDetails.put(snap.getKey(),child);
                }
                getWorkLoad();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getWorkLoad(){
        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference("FacultyDetails");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                workLoads.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){




                    String str = "siddhu";
                    FacultyDetails facultyDetailsAdder= new FacultyDetails(" ","siddhu","10");
                    if(facultyDetails.containsKey(snapshot1.getKey())){
                        facultyDetailsAdder.setName(facultyDetails.get(snapshot1.getKey()).getName());
                        facultyDetailsAdder.setId(facultyDetails.get(snapshot1.getKey()).getId());
                    }else{
                        facultyDetailsAdder.setName(snapshot1.getKey());
                    }
                    int count = 0;
                    for(DataSnapshot snap:snapshot1.getChildren()){
                        if(snap.getKey().equals("Details")) {
                            facultyDetailsAdder = snap.getValue(FacultyDetails.class);
                        }else{
                            for(DataSnapshot i:snap.getChildren())
                                count += i.getChildrenCount();
                        }
                    }

                    String faculty_id = snapshot1.getKey();
                    if(!faculty_id.contains("E0") || faculty_id.contains("EEE0")){
                    WorkLoads work = new WorkLoads(facultyDetailsAdder.getId(),count+"",facultyDetailsAdder.getName());
                    workLoads.add(work);
                    facultyids.add(faculty_id);
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

                Log.e("clicked","yupe");
                WorkLoads work = workLoads.get(i);
                String actualId = work.getFaculty_id();
                work.faculty_id = facultyids.get(i);
                System.out.println(work.getFaculty_name());
                System.out.println(work.getFaculty_id());
                Intent intent = new Intent(getApplicationContext(), FacultyProfile.class);
                intent.putExtra("faculty",work);
                intent.putExtra("actualId",actualId);
                startActivity(intent);
            }
        });


        loadingDialog.dismisss();
    }

    @Override
    public void onBackPressed() {

        finish();
    }
}