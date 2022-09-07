package com.cse.timetableapp;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;

public class MyApplication extends Application {

    public static HashMap<String, ArrayList<String>> namesmap = new HashMap<>();
    public static String currentYear = "";
    public static String FirebaseYear = "";

}
