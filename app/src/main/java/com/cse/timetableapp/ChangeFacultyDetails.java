package com.cse.timetableapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ChangeFacultyDetails extends AppCompatActivity {

    Button button,dummy_button;
    String extension="";
    HashMap<String,FacultyDetailsObject> facultydetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_faculty_details);
        askPermissionAndBrowseFile();



        System.out.println("Current PLayList lo unnavi************************");
        HashMap<String, ArrayList<String>> namesmap = MyApplication.namesmap;
        for(String name:namesmap.keySet()){
            ArrayList<String> subs = namesmap.get(name);
            System.out.print(name+" : ");
            for(String str:subs)
                System.out.print(str+" , ");
            System.out.println();
        }
        System.out.println("******************************************************");

        facultydetails = new HashMap<>();
        FirebaseDatabase.getInstance().getReference().child("FacultyDealing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    Log.e("CURRENT",snap.getKey());
                    FacultyDetailsObject facultyDetailsObject = snap.getValue(FacultyDetailsObject.class);
                    if(!(facultyDetailsObject.list == null))
                        facultydetails.put(snap.getKey(),facultyDetailsObject);
                }
                printchesichupi();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        button = findViewById(R.id.add_new_file);

        if(MyApplication.namesmap.size() == 0) {
            button.setEnabled(false);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] mimetypes =
                        { "application/vnd.ms-excel",
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        };
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                startActivityForResult(intent, 100);

            }
        });

        dummy_button = findViewById(R.id.details_dummy_object);
        if(MyApplication.namesmap.size() == 0) {
           dummy_button.setEnabled(false);
        }
        dummy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                changeDetailsOfFaculty();
            }
        });
    }

    private void printchesichupi() {

        for(String names:facultydetails.keySet()){
            FacultyDetailsObject facultyDetailsObject = facultydetails.get(names);
            if(facultyDetailsObject.list == null)
                Log.e(facultyDetailsObject.name,"Add Directly");
            else
                Log.e(facultyDetailsObject.name,facultyDetailsObject.list.toString());
        }
        Log.e("Fire Base Data","Done Retreiving Data");
    }


    private  void changeDetailsOfFaculty(){

        //Firebase Data in FacultyDetails
        //Current Loaded Details in NamesMap
        HashMap<String, ArrayList<String>> namesmap = MyApplication.namesmap;
        /*System.out.println("******************* NAMESMAP *************");
        for(String name:namesmap.keySet()){
            System.out.print("--------------"+name+"-------------     :");
            ArrayList<String> currentSubs = namesmap.get(name);
            for(String sub:currentSubs){
                System.out.print(sub);

            }
            System.out.println();
        }
        System.out.println("********************************************************************************");
        for(String name:facultydetails.keySet()){
            System.out.print("--------------"+name+"-------------     :");
            ArrayList<String> currentSubs = facultydetails.get(name).list;
            for(String sub:currentSubs){
                System.out.print(sub);

            }
            System.out.println();

        }
        System.out.println("********************************************************************************");*/

        for(String name:facultydetails.keySet()){


            FirebaseDatabase.getInstance().getReference().child("FacultyDetails").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {



                    ArrayList<String> valuesToRemove = new ArrayList<>();
                    ArrayList<String> finalSubs = new ArrayList<>();

                    String FirebaseYear = MyApplication.FirebaseYear;
                    System.out.println("--------------"+name+"-------------     :");



                    String currentYear = MyApplication.currentYear;
                    currentYear =  MyApplication.FirebaseYear;
                    if(currentYear.equals("III - CSE"))
                        currentYear = "III";
                    if(currentYear.equals("II - CSE"))
                        currentYear = "II";
                    if(currentYear.equals("IV - CSE"))
                        currentYear = "IV";

                    /*if(currentYear.equals("III - AI&ML"))
                        currentYear = "III - AIML";
                    if(currentYear.equals("II - AI&ML"))
                        currentYear = "II - AI&ML";*/

                    System.out.println("Current Year :"+currentYear+" **********************");

                    ArrayList<String> databaseSubs = facultydetails.get(name).list;
                    finalSubs = (ArrayList<String>) databaseSubs.clone();

                    //This code is written on 15-10-2022

                    if (namesmap.containsKey(name)) {
                        ArrayList<String> currentDatabaseSubs = new ArrayList<>();
                        ArrayList<String> finalList = new ArrayList<>();

                        for(String str:databaseSubs){
                            int i = str.lastIndexOf("-");
                            String sub = str.substring(0,i).trim();
                            if(sub.equals(currentYear)){
                                currentDatabaseSubs.add(str);
                            }else
                                finalList.add(str);
                        }

                        ArrayList<String> currentFileSubs = new ArrayList<>();
                        currentFileSubs =  MyApplication.namesmap.get(name);

                        System.out.print("Final Database Subjects : ");
                        for(String str:currentDatabaseSubs)
                            System.out.print(str+" ;");
                        System.out.println();

                        for(String str:currentDatabaseSubs){
                            if(!currentFileSubs.contains(str)){
                                valuesToRemove.add(str);
                            }
                        }


                        if(currentFileSubs.size()>0){
                            System.out.print("Final Current Subjects  : ");
                            for(String str:currentFileSubs){
                                System.out.print(str+" ;");
                                finalList.add(str);
                            }
                            System.out.println();
                        }


                        System.out.print("Final List              : ");
                        for(String str:finalList){
                            System.out.print(str+" ; ");
                        }
                        System.out.println();


                        for(String str:valuesToRemove){
                            for(DataSnapshot snap:snapshot.getChildren()){
                                for(DataSnapshot i:snap.getChildren()){
                                    String key = i.getKey();
                                    if(key.equals(FirebaseYear)){
                                        for(DataSnapshot j:i.getChildren()){
                                            FacultyData data = j.getValue(FacultyData.class);
                                            str = str.replaceAll("[-+.^:, ]","").toLowerCase(Locale.ROOT);
                                            String compare = data.getSectionId()+","+data.getShortVal();
                                            compare = compare.replaceAll("[-+.^:, ]","").toLowerCase(Locale.ROOT);
                                            if(str.equalsIgnoreCase(compare)){
                                                System.out.println("Update at "+name+" "+i.getKey()+" "+j.getKey()+" ");
                                                FirebaseDatabase.getInstance().getReference("FacultyDetails").child(name)
                                                        .child(snap.getKey()).child(i.getKey()).child(j.getKey()).removeValue();
                                            }
                                        }
                                    }
                                }
                            }
                        }


                        HashSet<String> hashset = new HashSet<>();
                        for(String str:finalList)
                            hashset.add(str);
                        ArrayList<String> ideInkaLast = new ArrayList<>();
                        for(String str:hashset)
                            ideInkaLast.add(str);
                        FirebaseDatabase.getInstance().getReference("FacultyDealing").child(name).child("list").setValue(ideInkaLast);


                    }







                    /*if(namesmap.containsKey(name)){






                        System.out.print("Database :     ");
                        for(String sub:databaseSubs){
                            int ind = sub.lastIndexOf("-");
                            String check = sub.substring(0,ind).trim();
                            //System.out.println("From  "+sub+" Taken  "+check);
                            if(check.equals(currentYear))
                                System.out.print(sub+" ;");
                        }
                        System.out.println();
               *//* for(String sub:currentSubs)
                    if(sub.startsWith(currentYear))
                        System.out.print(sub+" ;");
                System.out.println();*//*
                        ArrayList<String> subs = new ArrayList<>();
                        ArrayList<String> dummy = namesmap.get(name);
                        if(dummy != null){
                            for (String str : dummy)
                                subs.add(str);
                        }
                        if(facultydetails.containsKey(name)){
                            ArrayList<String> subsDealing = facultydetails.get(name).list;
                            if(subsDealing !=null) {
                                for (String str : subsDealing)
                                    if(!subs.contains(str))
                                        subs.add(str);
                            }
                        }


                        ArrayList<String> currentSubs = namesmap.get(name);
                        System.out.print("Current File : ");
                        for(String sub:currentSubs)
                            if(sub.startsWith(currentYear))
                                System.out.print(sub+" ;");
                        System.out.println();

                        for(String str:databaseSubs){
                            int ind = str.lastIndexOf("-");
                            String check = str.substring(0,ind).trim();
                            System.out.println("From  "+str+" Taken  "+check);


                            if(str.equals(currentYear)){
                                if(!currentSubs.contains(str)){
                                    subs.remove(str);
                                    valuesToRemove.add(str);
                                }
                            }
                        }

                        System.out.print("All Merged :   ");
                        for(String sub:subs)
                            if(sub.startsWith(currentYear))
                                System.out.print(sub+" ;");
                        System.out.println();

                        finalSubs = (ArrayList<String>) subs.clone();

                    }
                    else{
                        System.out.print("Ivi Complete ga poyayi  ");
                        for(String sub:databaseSubs) {
                            if (sub.startsWith(currentYear)) {
                                System.out.print(sub + " ;");
                                valuesToRemove.add(sub);
                                finalSubs.remove(sub);
                                //sub
                            }
                        }
                        System.out.println();
                    }*/



                    //This is firebaseCall
                    /*for(String str:valuesToRemove){
                        for(DataSnapshot snap:snapshot.getChildren()){
                            for(DataSnapshot i:snap.getChildren()){
                                String key = i.getKey();
                                if(key.equals(FirebaseYear)){
                                    for(DataSnapshot j:i.getChildren()){
                                        FacultyData data = j.getValue(FacultyData.class);
                                        str = str.replaceAll("[-+.^:, ]","").toLowerCase(Locale.ROOT);
                                        String compare = data.getSectionId()+","+data.getShortVal();
                                        compare = compare.replaceAll("[-+.^:, ]","").toLowerCase(Locale.ROOT);
                                        if(str.equalsIgnoreCase(compare)){
                                            System.out.println("Update at "+name+" "+i.getKey()+" "+j.getKey()+" ");
                                            FirebaseDatabase.getInstance().getReference("FacultyDetails").child(name)
                                                    .child(snap.getKey()).child(i.getKey()).child(j.getKey()).removeValue();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    FirebaseDatabase.getInstance().getReference("FacultyDealing").child(name).child("list").setValue(finalSubs);*/

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





        }




    }

    private void askPermissionAndBrowseFile()  {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()){

                // If you don't have access, launch a new activity to show the user the system's dialog
                // to allow access to the external storage
            }else{
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri content_describer = data.getData();
        String type="";

        InputStream in = null;
        OutputStream out = null;
        try {
            // open the user-picked file for reading:
            try {
                in = getContentResolver().openInputStream(content_describer);
                type=getContentResolver().getType(content_describer);
                Log.d("path1",type);
                if(type.equalsIgnoreCase("application/vnd.ms-excel")){
                    extension=".xls";
                }
                else if(type.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                {
                    extension=".xlsx";
                }
                else{
                    extension=".xls";
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // open the output-file:
            try {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TimeTable");
                if (!file.exists()) {
                    file.mkdirs();
                }
                out = new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString()+"/TimeTable/faculty"+extension));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // copy the content:
            byte[] buffer = new byte[1024];
            int len=0;
            while (true) {
                try {
                    if (!((len = in.read(buffer)) != -1)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.write(buffer, 0, len);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Contents are copied!
            try {
                getdata();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getdata() {
        Map<Integer,List<String>>sheet= null;
        try {
            //sheet = readJExcel("//storage//emulated//0//Download//CSE Staff List.xls", "Teaching");
            sheet = readJExcel(Environment.getExternalStorageDirectory().toString()+"/TimeTable/faculty"+extension, "Teaching");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        List<String>curRow = new ArrayList<>();
        List<String>facultyDetails = new ArrayList<>();
        ArrayList<List<String>> ll = new ArrayList<>();
        for(Integer x : sheet.keySet()) {
            curRow = sheet.get(x);
            ll.add(curRow);
        }

        for(List<String> values : ll) {
            facultyDetails.add(values.get(2));
            String result = values.get(3);
            facultyDetails.add(result);
        }
        int c=0;
        for(String details : facultyDetails) {
            c+=1;
            System.out.print(details+" ");
            if(c==2) {
                System.out.println();
                c=0;
            }
        }

        HashMap<String,ArrayList<String>> namesmap = MyApplication.namesmap;


        for(int i=0;i<facultyDetails.size();i+=2){
            String id = facultyDetails.get(i);
            String name = facultyDetails.get(i+1);
            String keyVal = name.replaceAll("[,.+^* ]","").toLowerCase(Locale.ROOT);

            ArrayList<String> subs = new ArrayList<>();

            ArrayList<String> dummy = namesmap.get(keyVal);
            if(dummy != null){
                for (String str : dummy)
                    subs.add(str);
            }


            if(facultydetails.containsKey(keyVal)){
                ArrayList<String> subsDealing = facultydetails.get(keyVal).list;
                if(subsDealing !=null) {
                    for (String str : subsDealing)
                        if(!subs.contains(str))
                            subs.add(str);
                }
            }


            /* Firebase Calling Function*/

            FacultyDetailsObject obj = new FacultyDetailsObject(name,id,subs);
            FirebaseDatabase.getInstance().getReference().child("FacultyDealing").child(keyVal).setValue(obj);
        }

    }

    public static Map<Integer, List<String>> readJExcel(String filelocation, String sheetname) throws IOException, BiffException {

        Map<Integer, List<String>> data = new HashMap<>();
        Workbook workbook = Workbook.getWorkbook(new File(filelocation));
        Sheet sheet = workbook.getSheet(sheetname);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        for(int i=4;i<rows;i++) {
            data.put(i,new ArrayList<String>());
            for(int j=0;j<columns;j++) {
                data.get(i)
                        .add(sheet.getCell(j,i)
                                .getContents());
            }
        }
        return data;

    }
}