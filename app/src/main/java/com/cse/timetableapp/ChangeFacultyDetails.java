package com.cse.timetableapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ChangeFacultyDetails extends AppCompatActivity {

    Button button;
    String extension="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_faculty_details);
        askPermissionAndBrowseFile();
        button = findViewById(R.id.add_new_file);
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
            FacultyDetailsObject obj = new FacultyDetailsObject(name,id,namesmap.get(keyVal));
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