package com.cse.timetableapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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
    public void yearchecking(String year) {



    }

}