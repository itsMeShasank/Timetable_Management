package com.cse.timetableapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.os.Bundle;

import com.cse.timetableapp.R;
import com.google.android.material.tabs.TabLayout;


public class StartUpActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    MyViewPager myViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        tabLayout = findViewById(R.id.OpenTabLayout);
        viewPager = findViewById(R.id.OpenViewPager);

        myViewPager = new MyViewPager(getSupportFragmentManager());

        secondyearStudent  secondyear = new secondyearStudent();
        myViewPager.AddFragment(secondyear,"II YEAR");

        StudentFragment studentFragment = new StudentFragment();
        myViewPager.AddFragment(studentFragment,"III YEAR");


        FacultyFragment facultyFragment = new FacultyFragment();
        myViewPager.AddFragment(facultyFragment,"Faculty");



        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setAdapter(myViewPager);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(StartUpActivity.this);

        builder.setMessage("Do you want to exit ?");
        builder.setTitle("Alert!!");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}