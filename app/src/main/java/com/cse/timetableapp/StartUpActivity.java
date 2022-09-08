package com.cse.timetableapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

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

        if(!isConectedToInternet(this)) {
            customDialog();
        }

        tabLayout = findViewById(R.id.OpenTabLayout);
        viewPager = findViewById(R.id.OpenViewPager);

        myViewPager = new MyViewPager(getSupportFragmentManager());

        secondyearStudent  secondyear = new secondyearStudent();
        myViewPager.AddFragment(secondyear,"II YEAR");

        StudentFragment studentFragment = new StudentFragment();
        myViewPager.AddFragment(studentFragment,"III YEAR");

        FragmentForForthYear fragmentForForthYear = new FragmentForForthYear();
        myViewPager.AddFragment(fragmentForForthYear,"IV YEAR");

        FacultyFragment facultyFragment = new FacultyFragment();
        myViewPager.AddFragment(facultyFragment,"Faculty");

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setAdapter(myViewPager);
        tabLayout.setupWithViewPager(viewPager);


    }

    private boolean isConectedToInternet(StartUpActivity start) {


        ConnectivityManager connectivityManager = (ConnectivityManager) start.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            Log.e("vachaa","if");
            return true;
        }else {
            Log.e("vachaa","else");
            return false;
        }
    }

    private void customDialog() {

        AlertDialog.Builder alertDialog =  new AlertDialog.Builder(StartUpActivity.this);
        alertDialog.setMessage("Please Connect to Internet to proceed further.")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        alertDialog.show();
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