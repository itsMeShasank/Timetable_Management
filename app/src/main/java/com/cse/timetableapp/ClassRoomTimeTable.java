package com.cse.timetableapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.cse.timetableapp.Contact;
import com.cse.timetableapp.Days.StudentFriday;
import com.cse.timetableapp.Days.StudentMonday;
import com.cse.timetableapp.Days.StudentSaturday;
import com.cse.timetableapp.Days.StudentThursday;
import com.cse.timetableapp.Days.StudentTuesday;
import com.cse.timetableapp.Days.StudentWednesday;
import com.cse.timetableapp.FacultySearchItems;
import com.cse.timetableapp.MyViewPager;
import com.cse.timetableapp.R;
import com.cse.timetableapp.StartUpActivity;
import com.cse.timetableapp.StudentData;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

public class ClassRoomTimeTable extends AppCompatActivity {


    HashMap<String, ArrayList<StudentData>> classRooms;
    String reqClassRoom;
    TextView textView;
    TabLayout tabLayout;
    ViewPager viewPager;
    private MyViewPager adapter;
    HashMap<String,FacultySearchItems> facultyDetails;
    static int workload;

    ArrayList<StudentData> smondayList,stuesdayList,swednesdayList,sthursdayList,sfridayList,ssaturdayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room_time_table);


        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        textView = findViewById(R.id.textViewinToolbar);
        textView.setText("");

        workload = 0;

        facultyDetails = new HashMap<>();

        tabLayout = findViewById(R.id.ClassRoomTimeTableLayout);
        viewPager = findViewById(R.id.ClassRoomTimeTableViewPager);

        classRooms = new HashMap<>();
        assignValues();

        reqClassRoom = getIntent().getStringExtra("classRoom");
        toolbar.setTitle("Room ID : "+reqClassRoom);
        reqClassRoom = reqClassRoom.replaceAll("[-,.()0 ]","").toLowerCase(Locale.ROOT);
        getFacultyIntoHashMap();







    }


    private void getDataFromFireBase() {

        FirebaseDatabase.getInstance().getReference().child("StudentDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot yearClass:snapshot.getChildren()){
                    for(DataSnapshot day:yearClass.getChildren()){
                        for(DataSnapshot periods:day.getChildren()){
                            StudentData studentData = periods.getValue(StudentData.class);
                            //studentData.setRoom(yearClass.getKey());
                            String Time = yearClass.getKey()+" "+day.getKey()+"  "+periods.getKey();
                            String classRoom = studentData.getRoom();
                            classRoom = classRoom.replaceAll("[-,.()0 ]","").toLowerCase(Locale.ROOT);
                            if (classRoom.equalsIgnoreCase(reqClassRoom)) {
                                workload++;
                                studentData.setRoom(yearClass.getKey());
                                String faculty = studentData.getFaculty();
                                System.out.println(faculty);

                                if(facultyDetails.containsKey(faculty))
                                    studentData.setFaculty(facultyDetails.get(faculty).getName());

                                if(day.getKey().equals("Mon")){
                                    smondayList.add(studentData);
                                }
                                else if(day.getKey().equals("Tue")){
                                    stuesdayList.add(studentData);
                                }
                                else if(day.getKey().equals("Wed")){
                                    swednesdayList.add(studentData);
                                }
                                else if(day.getKey().equals("Thu")){
                                    sthursdayList.add(studentData);
                                }
                                else if(day.getKey().equals("Fri")){
                                    sfridayList.add(studentData);
                                }
                                else if(day.getKey().equals("Sat")){
                                    ssaturdayList.add(studentData);
                                }
                            }

                        }
                    }
                }
                mainClassPrint();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void mainClassPrint() {

        Comparator<StudentData> comparator = new Comparator<StudentData>() {
            @Override
            public int compare(StudentData studentData, StudentData t1) {
                if(studentData.getPer() > t1.getPer())
                    return 1;
                else
                    return -1;
            }
        };

        Collections.sort(smondayList, comparator);
        Collections.sort(stuesdayList,comparator);
        Collections.sort(swednesdayList, comparator);
        Collections.sort(sthursdayList,comparator);
        Collections.sort(sfridayList, comparator);
        Collections.sort(ssaturdayList,comparator);
        // SOrting over

        for(StudentData i:smondayList){
            Log.e("Monday List",i.getSub());
        }



        StudentMonday monday = new StudentMonday();
        Bundle mondaybundle = new Bundle();

        mondaybundle.putParcelableArrayList("student",smondayList);
        monday.setArguments(mondaybundle);

        StudentTuesday tuesday = new StudentTuesday();
        Bundle tuesdaybundle = new Bundle();
        tuesdaybundle.putParcelableArrayList("student",stuesdayList);
        tuesday.setArguments(tuesdaybundle);

        StudentWednesday wednesday = new StudentWednesday();
        Bundle wednesdaybundle = new Bundle();
        wednesdaybundle.putParcelableArrayList("student",swednesdayList);
        wednesday.setArguments(wednesdaybundle);


        StudentThursday thursday = new StudentThursday();
        Bundle thursdaybundle = new Bundle();
        thursdaybundle.putParcelableArrayList("student",sthursdayList);
        thursday.setArguments(thursdaybundle);


        StudentFriday friday = new StudentFriday();
        Bundle fridaybundle = new Bundle();
        fridaybundle.putParcelableArrayList("student",sfridayList);
        friday.setArguments(fridaybundle);


        StudentSaturday saturday = new StudentSaturday();
        Bundle saturdaybundle = new Bundle();
        saturdaybundle.putParcelableArrayList("student",ssaturdayList);
        saturday.setArguments(saturdaybundle);


        tabLayout = findViewById(R.id.ClassRoomTimeTableLayout);
        viewPager = findViewById(R.id.ClassRoomTimeTableViewPager);
        adapter = new MyViewPager(getSupportFragmentManager());

        adapter.AddFragment(monday,"Monday");
        adapter.AddFragment(tuesday,"Tuesday");
        adapter.AddFragment(wednesday,"wednesday");
        adapter.AddFragment(thursday,"Thursday");
        adapter.AddFragment(friday,"Friday");
        adapter.AddFragment(saturday,"Saturday");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        textView.setText("Load : "+workload);

    }



    private  void getFacultyIntoHashMap(){
        FirebaseDatabase.getInstance().getReference().child("FacultyDealing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    FacultySearchItems child = snap.getValue(FacultySearchItems.class);
                    facultyDetails.put(snap.getKey(),child);
                }
                System.out.println("__________________________________________________-");
                for(String name:facultyDetails.keySet()){
                    System.out.println(facultyDetails.get(name).getName());
                }

                if(reqClassRoom != null || !reqClassRoom.equals("")){
                    getDataFromFireBase();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void assignValues() {
        //Student
        smondayList = new ArrayList<>();
        stuesdayList  = new ArrayList<>();
        swednesdayList = new ArrayList<>();
        sthursdayList = new ArrayList<>();
        sfridayList = new ArrayList<>();
        ssaturdayList = new ArrayList<>();

    }



    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.itemlist,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.care:
                finish();

            case R.id.logout:
                finish();

        }

        return true;
    }

}