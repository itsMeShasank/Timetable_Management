package com.cse.timetableapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class FreeFaculty extends AppCompatActivity {

    LoadingDialog loadingDialog;
    ArrayList<String> array;
    DatabaseReference databaseReference;
    ArrayList<String> rooms;
    List<FreeFacultyLoader> list;
    ListView listView;
    String period,day;
    com.google.android.material.floatingactionbutton.FloatingActionButton fab;
    HashMap<String,FacultySearchItems> facultyDetails;


    TextView periodt,dayt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_faculty);

        loadingDialog = new LoadingDialog(this);
        loadingDialog.load();
        facultyDetails = new HashMap<>();
        array = new ArrayList<>();
        rooms = new ArrayList<>();
        list = new ArrayList<>();

        fab = findViewById(R.id.fab_free_faculty);
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


        periodt = findViewById(R.id.free_facult_periodsss);
        dayt = findViewById(R.id.free_facult_day);


        periodt.setText(period);
        dayt.setText(day+"day");


        Log.e("Period",period);
        Log.e("Day",day);


        listView = findViewById(R.id.FacultylistView);

        getFacultyIntoHashMap();





    }


    private  void getFacultyIntoHashMap(){
        FirebaseDatabase.getInstance().getReference().child("FacultyDealing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    FacultySearchItems child = snap.getValue(FacultySearchItems.class);
                    facultyDetails.put(snap.getKey(),child);
                }

                completeDataFetch();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void completeDataFetch(){



        try {
            //Faculty Free Data
            databaseReference = FirebaseDatabase.getInstance().getReference("FacultyDetails");
            HashSet<String> addedList = new HashSet<>();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count = 1;



                    for(DataSnapshot snap:snapshot.getChildren()){

                        if(snap.hasChild(day)){

                            int flag = 0;

                            for(DataSnapshot years:snap.getChildren()){
                                if(years.hasChild(day)){
                                    flag = 1;
                                    break;
                                }
                            }
                            if(flag==0){
                                addedList.add(snap.getKey());

                                if (facultyDetails.containsKey(snap.getKey())) {
                                    FreeFacultyLoader facultyLoader = new FreeFacultyLoader(facultyDetails.get(snap.getKey()).getId(), facultyDetails.get(snap.getKey()).getName());
                                    list.add(facultyLoader);
                                } else {
                                    list.add(new FreeFacultyLoader("NCS"+(count++),snap.getKey()));
                                }
                            }

                        }else{
                            addedList.add(snap.getKey());
                            if (facultyDetails.containsKey(snap.getKey())) {
                                FreeFacultyLoader facultyLoader = new FreeFacultyLoader(facultyDetails.get(snap.getKey()).getId(), facultyDetails.get(snap.getKey()).getName());
                                list.add(facultyLoader);
                            } else {
                                list.add(new FreeFacultyLoader("NCS"+(count++),snap.getKey()));
                            }
                        }

                    }

                    Collections.sort(list, new Comparator<FreeFacultyLoader>() {
                        @Override
                        public int compare(FreeFacultyLoader i, FreeFacultyLoader j) {
                            if(i.facultyid.compareTo(j.facultyid) > 0)
                                return 1;
                            else
                                return -1;
                        }
                    });


                    //Previous Code
                    /*for (DataSnapshot snap : snapshot.getChildren()) {
                        DataSnapshot s = snap.child(day);


                        if (s.exists()) {
                            for(DataSnapshot i:s.getChildren()){
                                if(!i.hasChild(period)){
                                    array.add(snap.getKey());
                                    String id = snap.getKey();
                                    //if (!id.contains("E0"))

                                    if(facultyDetails.containsKey(snap.getKey())) {
                                        FreeFacultyLoader loader = new FreeFacultyLoader(facultyDetails.get(snap.getKey()).getId(), facultyDetails.get(snap.getKey()).getName());
                                        if(!addedList.contains(id)){
                                            list.add(loader);
                                            addedList.add(id);
                                        }
                                    }else{
                                        Log.e("ee faculty Id ledu",snap.getKey());
                                        list.add(new FreeFacultyLoader("NCS"+(count++), snap.getKey()));
                                    }
                                }
                            }
                        } else {



                        }
                    }*/
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

        loadingDialog.dismisss();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),AdminActivity.class));
        finish();
    }
}