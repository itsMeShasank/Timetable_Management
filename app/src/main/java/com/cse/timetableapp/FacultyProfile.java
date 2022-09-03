package com.cse.timetableapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cse.timetableapp.Workloads.WorkLoads;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FacultyProfile extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6,t7;
    String id;
    DatabaseReference databaseReferenc1e;
    int labs,coding,idp,theory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_profile);

        Intent intent = getIntent();
        WorkLoads work =  intent.getParcelableExtra("faculty");
        id = work.getFaculty_id();
        labs = 0;
        coding = 0;
        idp = 0;
        theory = 0;
        t1 = findViewById(R.id.faculty_profile_id);
        t2 = findViewById(R.id.faculty_profile_name);
        t3 = findViewById(R.id.faculty_profile_workload);
        t4 = findViewById(R.id.faculty_profile_labs);
        t5 = findViewById(R.id.faculty_profile_coding);
        t6 = findViewById(R.id.faculty_profile_idp);
        t7 = findViewById(R.id.faculty_profile_theory);


        t1.setText(id);
        t2.setText(work.getFaculty_name());
        t3.setText(work.getWorkload());
        t4.setText(labs+" ");
        t5.setText(coding+" ");
        t6.setText(idp+" ");
        t7.setText(theory+" ");
        fillFromFireBase();
    }

    public void fillFromFireBase(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FacultyDetails").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    Log.e("hello","hello");
                  if(!snap.getKey().equals("Details")) {
                        for(DataSnapshot snapper:snap.getChildren()){

                            FacultyData data = snapper.getValue(FacultyData.class);
                            String sub = data.getShortVal();
                            if(sub.contains("Lab"))
                                labs++;
                            else if(sub.contains("IDP"))
                                idp++;
                            else if(sub.contains("Coding"))
                                coding++;
                            else
                                theory++;


                        }

                    }
                }
                fillVals();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void fillVals(){
        t4.setText(labs+" ");
        t5.setText(coding+" ");
        t6.setText(idp+" ");
        t7.setText(theory+" ");
    }
}