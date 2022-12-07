package com.cse.timetableapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ModidyCurrentTimetable extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    HashMap<String,ArrayList> subjects = new HashMap<>();
    LoadingDialog loadingDialog;
    Button button,save,cancel;
    public String year = "Select Year";
    public static String siddhu = "";
    String extension="";
    Spinner spinner;
    HashMap<String,ArrayList<String>> namesmap;
    ArrayList<String> temporaryFacultyList;

    String[] timings = {"8:05-9:00","9:00-9:55","10:15-11:10","11:10-12:05","12:05-01:00","02:00-02:55","02:55-03:50","03:50-4:40","04:40-05:30"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modidy_current_timetable);
        askPermissionAndBrowseFile();
        //askPermissionAndWriteFile();



        temporaryFacultyList = new ArrayList<>();
        namesmap = new HashMap<>();

        loadingDialog = new LoadingDialog(this);
        button = findViewById(R.id.browse);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.load();
                String[] mimetypes =
                        { "application/vnd.ms-excel",
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        };
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                startActivityForResult(intent, 100);

            }});

        spinner = findViewById(R.id.spinner_sheet);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = spinner.getSelectedItem().toString();
                siddhu = spinner.getSelectedItem().toString();
                MyApplication.FirebaseYear = year;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





    }





    /*private void askPermissionAndWriteFile(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ModidyCurrentTimetable.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ModidyCurrentTimetable.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(ModidyCurrentTimetable.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
        }
    }*/





    // Copied from sasi
    //@RequiresApi(api = Build.VERSION_CODES.R)

    //@RequiresApi(api = Build.VERSION_CODES.R)
    private void askPermissionAndBrowseFile()/*{
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ModidyCurrentTimetable.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ModidyCurrentTimetable.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(ModidyCurrentTimetable.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
        }
    }*/  {
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
                out = new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString()+"/TimeTable/data"+extension));
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

    public static Map<Integer, List<String>> readJExcel(String fileLocation,String sheetname)
            throws IOException, BiffException {
        Map<Integer, List<String>> data = new HashMap<>();



            Workbook workbook = Workbook.getWorkbook(new File(fileLocation));
            Sheet sheet = workbook.getSheet(sheetname);
            int rows = sheet.getRows();
            int columns = sheet.getColumns();

            for (int i = 0; i < rows; i++) {
                data.put(i, new ArrayList<String>());
                for (int j = 0; j < columns; j++) {
                    data.get(i)
                            .add(sheet.getCell(j, i)
                                    .getContents());
                }
            }
        return data;
    }


    private void customDialog(String Message) {

        AlertDialog.Builder alertDialog =  new AlertDialog.Builder(ModidyCurrentTimetable.this);
        alertDialog.setMessage("Error!!    "+Message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        alertDialog.show();
    }


    public  void getdata() throws BiffException, IOException {


        Map<Integer,List<String>>sheet=readJExcel(Environment.getExternalStorageDirectory().toString()+"/TimeTable/data"+extension, year);
        Set<String> days=new HashSet<String>();
        for(String x:new String[]{"Mon","Tue","Wed","Thu","Fri","Sat"})
            days.add(x);
        String curSec="None";
        String year="";
        //will get only roman numbers from sheet name.
        if(!siddhu.contains("M Tech")) {
            year = siddhu.substring(0, siddhu.indexOf(" "));
        }else {
            year = siddhu;
        }
        MyApplication.currentYear = year;
        String Rooms ="None";
        Log.e("Checking Heading Name",year+" "+siddhu);
        MyApplication.namesmap.clear();
        List<String>roomsList = new ArrayList<>();
        List<String>entireList = new ArrayList<>();

        List<List>entireroomsList = new ArrayList<>();
        for(Integer x :sheet.keySet())
        {
            List<String>curRow=sheet.get(x);
            if(days.contains(curRow.get(0)))
            {
                if(year.contains("II") || year.contains("IV") || year.contains("M Tech")) {
                    curRow.remove(3);
                    curRow.remove(6);
                }else {
                    curRow.remove(3);
                    curRow.remove(5);
                }
                String prev="None";
                ListIterator<String> it=curRow.listIterator();
                while(it.hasNext())
                {
                    String curCol=it.next();

                    if(curCol.equals(""))
                        curCol=prev;
                    else
                        prev=curCol;
                    curCol=curCol.replace("\n", " ");
                    it.set(curCol);

                }
                int i;
                for(i=0;i<curRow.size();i++) {
                    String value = curRow.get(i);

                    if(value.contains("(P)") || value.contains("(T)")) {
                        String roomdata =null;
                        if(value.contains("Hall")) {
                            if(!value.contains("3rd floor")) {
                            roomdata = value.substring(value.lastIndexOf("(")+1,value.length()-1);
                            }else {
                                roomdata = value.substring(value.lastIndexOf(")")+1,value.length()-1);
                            }
                            value = value.substring(0,value.indexOf(")")+1);
                            roomsList.add(roomdata);
                        }
                        else if(value.contains("NTR")) {
                            roomsList.add(value.substring(value.indexOf(")")+1,value.length()));
                            value = value.substring(0,value.indexOf(")")+1);
                            curRow.set(i,value);
                        }
                        else if(value.contains("VPTF") || value.contains("VPSF") || value.contains("VBF") || value.contains("VBS") || value.contains("VBT") || value.contains("VSF")) {
                            //DBMS (P/T) (VPTF-07)
                            String roomsdata = value.substring(value.lastIndexOf("(")+1,value.length()-1);
                            value = value.substring(0,value.indexOf(")")+1);
                            roomsList.add(roomsdata);
                        }
                        else if(value.contains("CC Lab")  || value.contains("MAT Lab") || value.contains("CP Lab")||value.contains("TEC") || value.contains("Floor") || value.contains("floor") || value.contains("CSE") && !value.contains("NTR")) {

                            roomsList.add(value.substring(value.indexOf("("),value.length()));
                            value = value.substring(0,value.indexOf("("));
                            curRow.set(i,value);
                        }
                        else {
                            value = value.substring(0, value.indexOf(")")+1);
                            roomsList.add(Rooms);
                        }
                        curRow.set(i, value);
                        //roomsList.add(roomdata);

                    }

                    else if(value.contains("SCIRP") || value.contains("IDP")) {
                        System.out.println("idpppp : "+value);
                        if(value.contains("VPTF") || value.contains("VPSF") || value.contains("Hall") ||((value.contains("Library"))&&(value.contains("Lab")))) {
                            curRow.set(i, value);
                            String roomdata = "refer section";
                            roomdata = value.substring(value.indexOf("(")+1, value.lastIndexOf(")"));
                            roomsList.add(roomdata);
                        }else {
                            roomsList.add("refer section");
                        }
                    }

                    else if(value.contains("VPTF") || value.contains("VPSF") || value.contains("Hall") || value.contains("VBF") || value.contains("VBS") || value.contains("VBT") || value.contains("VSF")) {
                        System.out.println(value);
                        String roomdata = null;
                        roomdata = value.substring(value.indexOf("(")+1, value.lastIndexOf(")"));
                        roomsList.add(roomdata);
                        value = value.substring(0, value.indexOf("("));
                        curRow.set(i, value);
                    }

                    else if(value.contains("Open") || value.contains("Test") || value.contains("IDP")){

                        curRow.set(i,value);
                        roomsList.add("refer section ");
                    }

                    else if(value.contains("Library") && value.contains("Lab")) {
                        String roomdata=null;
                        roomdata = value.substring(value.indexOf("(")+1, value.lastIndexOf(")"));
                        roomsList.add(roomdata);
                        value = value.substring(0,value.indexOf("(")-1);
                        curRow.set(i, value);
                    }
                    else if(value.equals("Library")) {

                        String roomdata = value;
                        roomsList.add(roomdata);
                    }
                    else if(value.contains("NTR")) {

                        String roomdata = value.substring(value.indexOf("N"),value.lastIndexOf(")")+1);
                        roomsList.add(roomdata);
                        value = value.substring(0,value.indexOf("("));
                        curRow.set(i,value);
                    }
                    else if(value.contains("CC Lab")  || value.contains("MAT Lab") || value.contains("CP Lab") || value.contains("TEC Lab") || value.contains("Floor") || value.contains("floor") || value.contains("CSE")) {

                        roomsList.add(value.substring(value.indexOf("(")+1,value.lastIndexOf(")")));
                        value = value.substring(0,value.indexOf("("));
                        curRow.set(i,value);

                    }else if(value.contains("Honors") || value.contains("CRT") || value.contains("crt") || value.contains("Crt")) {
                        roomsList.add("refer section");
                    }else if(value.contains("Sports") || value.contains("-") || value.contains("***")) {
                        roomsList.add("-");
                    }
                    else {
                        roomsList.add(Rooms);
                    }
                }
                curRow.add(0, curSec);
                for(int k=0;k<curRow.size();k++) {
                    entireList.add(curRow.get(k));
                }

                entireroomsList.add(roomsList);
                continue;
            }

            if(!days.contains(curRow.get(0)) && !curRow.contains("Day") && !curRow.get(0).contains("Section") && curRow.get(0)!="") {
                ListIterator<String> itr1 = curRow.listIterator();
                HashMap<String,ArrayList> map = new HashMap<>();
                while(itr1.hasNext()) {
                    String data = itr1.next();
                    if(data.length()!=0) {
                        StringTokenizer st = new StringTokenizer(data,":");
                        while(st.hasMoreTokens()) {
                            String key, value;
                            key = curSec + "," + st.nextToken();
                            if (st.hasMoreTokens()) {
                                value = st.nextToken();
                                StringTokenizer st1 = new StringTokenizer(value, ",");
                                ArrayList<String> list = new ArrayList<>();
                                while (st1.hasMoreTokens()) {
                                    String facultyName = st1.nextToken().trim();
                                    facultyName = facultyName.replaceAll("[-+.^:, ]", "").toLowerCase(Locale.ROOT);
                                    list.add(facultyName);
                               }
                                map.put(key, list);

                                for (String name : list) {
                                    if (namesmap.containsKey(name))
                                        namesmap.get(name).add(key);
                                    else {
                                        ArrayList<String> ll = new ArrayList<>();
                                        ll.add(key);
                                        namesmap.put(name, ll);
                                    }
                                }
                            }
                        }

                    }
                }
                for (String key: map.keySet()){
                    subjects.put(key, map.get(key));
                }
            }

            if (curRow.get(0).contains("Section")) {
                String nameSec = curRow.get(0);
                Rooms = curRow.get(0);
                if ((!nameSec.contains("VPTF") && !nameSec.contains("VPSF")) &&(!curRow.get(0).contains("AI&ML") || !curRow.get(0).contains("CS") || !curRow.get(0).contains("CSBS"))) {
                    StringTokenizer st = new StringTokenizer(nameSec, " ");
                    st.nextToken();
                    st.nextToken();
                    curSec = year + " - " + st.nextToken();
                    Rooms = nameSec.substring(nameSec.indexOf("(") + 1, nameSec.length() - 1);
                    System.out.println("nenu vacheysaaa : "+nameSec);
                } else {
                    StringTokenizer st = new StringTokenizer(nameSec, " ");
                    while (st.hasMoreTokens()) {
                        String in = st.nextToken();

                        if (!in.contains("Section") && !in.contains(":") && !(in.contains("VPTF") || in.contains("VPSF")) && !nameSec.contains("Hall") && !nameSec.contains("NTR")) {
                            curSec = year + " - " + in;
                        }
                        if (in.contains("VPTF") || in.contains("VPSF")) {
                            in = in.substring(1, in.length() - 1);
                            Rooms = in;
                        }else {
                            Rooms = "None";
                        }
                    }
                }
                System.out.println(Rooms+" deafault room");

            }
            if((curRow.get(0).contains("AI&ML") || curRow.get(0).contains("CS") || curRow.get(0).contains("CSBS"))&&(curRow.get(0).contains("Section")))
            {
                //II AI&ML Section: A
                //II AI&ML Section: A (VPTF -07)

                String nameSec = curRow.get(0);
                if(nameSec.contains("VPTF") || nameSec.contains("VPSF")) {
                    StringTokenizer st = new StringTokenizer(nameSec, " ");
                    String yy = st.nextToken();
                    String spl = st.nextToken();
                    st.nextToken();
                    st.nextToken();
                    String sec = st.nextToken();
                    curSec = yy+" - "+spl+" - "+sec;
                    String roomVal = st.nextToken();
                    Rooms = roomVal.substring(1, roomVal.indexOf(")"));


                }else {
                    StringTokenizer st = new StringTokenizer(nameSec, " ");
                    String yy = st.nextToken();
                    String spl = st.nextToken();
                    st.nextToken();
                    st.nextToken();
                    String sec = st.nextToken();
                    curSec = yy+" - "+spl+" - "+sec;
                    Rooms = "None";
                }

            }
        }


        System.out.println(entireList+"\n"+entireroomsList+"\n"+subjects);


        for(String name:namesmap.keySet()){
            Log.e(name,namesmap.get(name)+" ");
        }

        Log.e("Completed","Edo okati");
        for(String name:subjects.keySet()){
            Log.e(name,subjects.get(name)+" ");
        }
        //MyApplication.namesmap = namesmap;

        /*    Firebase Calling Functions    */

        System.out.println(entireList+"\n"+entireroomsList+"\n");
        faculty(entireList, entireroomsList.get(0), subjects);
        //student(entireList,entireroomsList.get(0),subjects);

    }

    @SuppressLint("NewApi")
    public void faculty(List<String> entireList, List<String> entireroomsList, HashMap<String, ArrayList> subjects) {

        int columncount = 0;
        for (String key : entireList) {
            if (key.contains("M Tech") || key.contains("II") || key.contains("IV") || key.contains("III") || key.contains("I")) {
                columncount = 0;
            } else {

                columncount += 1;
            }
        }
        int c=0;
        List<String>entireListwithoutrepeation = new ArrayList<>();
        List<String>entireroomsListwithoutrepeation = new ArrayList<>();
        List<String>repeationdays = new ArrayList<>();
        for(int period=0;period < entireList.size();period++) {
            if(c <= columncount) {
                //System.out.println(c);
                repeationdays.add(entireList.get(period));
            }
            if(c == columncount) {
                int x = columncount;
                while(x > 8) {
                    repeationdays.remove(x-1);
                    x-=1;
                }
                for(String value : repeationdays) {
                    entireListwithoutrepeation.add(value);
                }
                repeationdays.clear();
                c=-1;
            }
            c+=1;
        }
        entireList.clear();
        for(String value : entireListwithoutrepeation) {
            entireList.add(value);
        }
        repeationdays.clear();
        columncount-=1;
        for(int period=0;period < entireroomsList.size();period++) {
            if(c <= columncount) {
                //System.out.println(c);
                repeationdays.add(entireroomsList.get(period));
            }
            if(c == columncount) {
                int x = columncount;
                while(x > 7) {
                    repeationdays.remove(x-1);
                    x-=1;
                }
                for(String value : repeationdays) {
                    entireroomsListwithoutrepeation.add(value);
                }
                repeationdays.clear();
                c=-1;
            }
            c+=1;
        }
        entireroomsList.clear();
        for(String value : entireroomsListwithoutrepeation) {
            entireroomsList.add(value);
        }
        System.out.println(entireList+"\n"+entireroomsList);
        columncount=8;
        int sub = 2, room = 1, subcount = 0, roomcount = 0, periodnumber = 1;

        String Prsntday = null, section = null;
        ArrayList faculty = null;
        while (sub < entireList.size() && room < entireroomsList.size()) {
            if (sub == 2 && room == 1) {
                section = entireList.get(0);
                Prsntday = entireList.get(1);
            }
            if (subcount == columncount - 1 && roomcount == columncount - 1) {
                subcount = roomcount = 0;
                sub += 2;
                room += 1;
                Prsntday = entireList.get(sub - 1);
                section = entireList.get(sub - 2);
                periodnumber = 1;
            }
            String copy1 = entireList.get(sub);
            if(entireList.get(sub).contains("SCIRP") && !entireList.get(sub).contains("Honors")) {
                String subject1 = copy1.substring(0,copy1.indexOf("("));
                faculty = subjects.get(section.trim() + "," + subject1.trim());

            }else {
                faculty = subjects.get(section.trim() + "," + entireList.get(sub).trim());
            }
            //faculty = subjects.get(section.trim() + "," + entireList.get(sub).trim());
            String facultyNameIDP= null;
            if (faculty != null) {
                //System.out.println("every thing : "+periodnumber+", "+timings[periodnumber-1]+", "+section+", "+Prsntday+", "+entireList.get(sub)+" "+entireroomsList.get(room)+", "+faculty);
                //
                int t = 0;


                while (t < faculty.size()) {

                    if(entireList.get(sub).contains("SCIRP") || (entireList.get(sub).contains("IDP") && (entireList.get(sub).contains("VPTF") || entireList.get(sub).contains("VPTF")))) {
                        System.out.println("********************SCIIRP********************");
                        String copy = entireList.get(sub);
                        facultyNameIDP = entireList.get(sub).substring(entireList.get(sub).lastIndexOf(")")+1,entireList.get(sub).length());
                        facultyNameIDP = facultyNameIDP.replaceAll("[.,+ ]","").toLowerCase(Locale.ROOT);
                        String subject = copy.substring(0,copy.indexOf("("));
                        if (namesmap.containsKey(facultyNameIDP))
                            namesmap.get(facultyNameIDP).add(subject);
                        else {
                            ArrayList<String> ll = new ArrayList<>();
                            ll.add(subject);
                            namesmap.put(facultyNameIDP, ll);
                        }

                        if(periodnumber <8) {
                            //System.out.println("faculty luu vunaruu : "+periodnumber+", "+timings[periodnumber-1]+", "+section+", "+Prsntday+", "+subject+", "+entireroomsList.get(room)+", "+facultyNameIDP);
                            facultyFirebase1(1,timings[periodnumber - 1], periodnumber, section, Prsntday, subject, entireroomsList.get(room).toString(), facultyNameIDP);
                        }
                    }else {
                        if(periodnumber <8){

                            //System.out.println("faculty vunaru : "+periodnumber+", "+timings[periodnumber-1]+", "+section+", "+Prsntday+", "+entireList.get(sub)+", "+entireroomsList.get(room)+", "+faculty.get(t));
                            //System.out.println("scrip kadu : "+entireList.get(sub));
                            if(faculty.get(t).equals("") || faculty == null) {
                                facultyFirebase1(2,timings[periodnumber - 1], periodnumber, section, Prsntday, entireList.get(sub), entireroomsList.get(room).toString(),"notmentioned");
                            }else {
                                facultyFirebase1(3, timings[periodnumber - 1], periodnumber, section, Prsntday, entireList.get(sub), entireroomsList.get(room).toString(), faculty.get(t).toString());
                            }
                        }
                    }
                    t += 1;
                }
            }else {

                if (periodnumber < 8) {
                    // honors/ scirp or honors/library periods kii
                    if ((entireList.get(sub).contains("IDP") || entireList.get(sub).contains("Honors/SCIRP")) &&((!entireList.get(sub).contains("VPTF") && !entireList.get(sub).contains("VPSF") && !entireList.get(sub).contains("NTR") && !entireList.get(sub).contains("Library"))) && periodnumber < 8) {

                        String copy = entireList.get(sub);
                        facultyNameIDP = copy.substring(copy.indexOf("P")+1,copy.length());
                        facultyNameIDP = facultyNameIDP.replaceAll("[.,+ ]","").toLowerCase(Locale.ROOT);
                        copy = copy.substring(0,copy.indexOf("P")+1);
                        facultyFirebase1(4,timings[periodnumber - 1], periodnumber, section, Prsntday, copy, entireroomsList.get(room).toString(), facultyNameIDP);

                    }
                    //scrip contains ntr as room
                    else if(((entireList.get(sub).contains("IDP") || entireList.get(sub).contains("SCIRP")) &&((entireList.get(sub).contains("Seminar")) || (entireList.get(sub).contains("VPTF") || entireList.get(sub).contains("VPSF") || entireList.get(sub).contains("NTR") || entireList.get(sub).contains("Library")))) && periodnumber < 8) {
                        String copy = entireList.get(sub);
                        System.out.println(entireList.get(sub)+"--------------else if");
                        facultyNameIDP = entireList.get(sub).substring(entireList.get(sub).lastIndexOf(")")+1,entireList.get(sub).length());
                        facultyNameIDP = facultyNameIDP.replaceAll("[.,+ ]","").toLowerCase(Locale.ROOT);
                         String subject = copy.substring(0,copy.indexOf("("));

                        if (namesmap.containsKey(facultyNameIDP))
                            namesmap.get(facultyNameIDP).add(section+","+subject);
                        else {
                            ArrayList<String> ll = new ArrayList<>();
                            ll.add(section+","+subject);
                            namesmap.put(facultyNameIDP, ll);
                        }
                        if(entireroomsListwithoutrepeation.contains(facultyNameIDP)) {
                            facultyFirebase1(51, timings[periodnumber - 1], periodnumber, section, Prsntday, subject, entireroomsList.get(room).toString(), "None");
                        }else {
                            facultyFirebase1(52, timings[periodnumber - 1], periodnumber, section, Prsntday, subject, entireroomsList.get(room).toString(), facultyNameIDP);
                        }
                    }
                    else {

                        facultyFirebase1(6,timings[periodnumber - 1], periodnumber, section, Prsntday, entireList.get(sub), entireroomsList.get(room).toString(), "None");
                    }
                }
            }
            subcount += 1;
            roomcount += 1;
            sub += 1;
            room += 1;
            periodnumber += 1;
        }
        //student(entireList,entireroomsList,subjects);

    }

    public void facultyFirebase1(int x,String timing, int periodnumber, String section, String prsntday, String subject, String room, String faculty) {
        int t=0;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FacultyDetails");
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("StudentDetails");

        /*Log.e(section,prsntday+"   "+faculty);
        Log.e("Excel : " +subject,"Firebase : ");*/



        if(faculty == null || faculty.equals("") || faculty.equals(" ")) {
            faculty = "None";
        }
        //System.out.println(x+" "+timing+"  "+periodnumber+"  "+section+"  "+prsntday+"  "+subject+"  "+room+"  "+faculty);
        String finalFaculty = faculty;
        if(!subject.equals("***") && !subject.contains("Sat") ) {
            System.out.println(x+" "+timing+"  "+periodnumber+"  "+section+"  "+prsntday+"  "+subject+"  "+room+"  "+faculty);
            data.child(section).child(prsntday).child(timing).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Log.e("snap: ",dataSnapshot.toString());
                    students st = dataSnapshot.getValue(students.class);
                    Log.e(section, prsntday);


                    if (!finalFaculty.equalsIgnoreCase("None")) {
                        System.out.println(prsntday + " else part");
                        if (st != null && st.getPer() < 8 && !subject.contains("Sat") && !subject.equals("-")) {
                            Log.e(section, prsntday);
                            Log.e("Excel : " + subject, "Firebase : " + st.getSub());
                            if (!st.getSub().equals(subject)) {
                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("FacultyDetails");
                                String name = st.getFaculty();
                                //System.out.println(name);
                                String result = name.replaceAll("[-+.^:, ]", "").toLowerCase(Locale.ROOT);
                                Log.e("idi students deggara", result);


                                databaseReference1.child(result).child(st.getDay()).child(year).child(timing).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            DatabaseReference studentsaving = FirebaseDatabase.getInstance().getReference("StudentDetails");
                                            students newstudent = new students(periodnumber, finalFaculty, section, prsntday, subject, room, timing);
                                            studentsaving.child(section).child(prsntday).child(timing).setValue(newstudent);
                                            System.out.println("faculty ki eyna theruvathae student changed");
                                        }
                                    }
                                });

                                //working : System.out.println("same kaduu update aendhiii data : "+st.getPer()+" "+st.getFaculty()+" "+st.getSec()+" "+st.getSub()+" "+faculty+" "+subject+periodnumber);
                            }
                            save();
                        }
                    } else {

                        if (st != null) {
                            String key = st.getFaculty();
                            FirebaseDatabase.getInstance().getReference().child("FacultyDetails").child(key).child(st.getDay()).child(siddhu).child(st.getTime()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //String day, String faculty, long per, String room, String sec, String sub, String time
                                    StudentData studentData = new StudentData(prsntday, "-", periodnumber, room, section, subject, timing);
                                    FirebaseDatabase.getInstance().getReference().child("StudentDetails").child(section).child(prsntday).child(timing).setValue(studentData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.e("Update When No Faculty", section + " " + prsntday + "  " + periodnumber);
                                        }
                                    });
                                }
                            });
                        } else {

                            DatabaseReference facultySaving = FirebaseDatabase.getInstance().getReference("FacultyDetails");
                            DatabaseReference studentsaving1 = FirebaseDatabase.getInstance().getReference("StudentDetails");

                            if (st == null && finalFaculty != null) {
                                System.out.println("COMING HERE AT NIGHT");
                                students students = new students(periodnumber, finalFaculty, section, prsntday, subject, room, timing);
                                FacultyData facultyData = new FacultyData(prsntday, finalFaculty, section, subject, room, timing, periodnumber);
                                String result = finalFaculty.replaceAll("[-+.^:, ]", "").toLowerCase(Locale.ROOT);
                                facultySaving.child(result).child(prsntday).child(siddhu).child(timing).setValue(facultyData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        studentsaving1.child(section).child(prsntday).child(timing).setValue(students).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.e("if period doesn't", " exist in database will save now" + prsntday + ", " + periodnumber + ", " + section);
                                            }
                                        });
                                    }
                                });
                            } else {

                                students students = new students(periodnumber, finalFaculty, section, prsntday, subject, room, timing);
                                studentsaving1.child(section).child(prsntday).child(timing).setValue(students).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.e("no faculty for ", "this periods" + periodnumber + ", " + section + ", " + prsntday);
                                    }
                                });
                            }
                        }


                    }

                }

                private void save() {
                    if (periodnumber < 8) {
                        saveFaculties saveFaculties = new saveFaculties(prsntday, finalFaculty, section, room, subject, timing, periodnumber);
                        String name = saveFaculties.getName();

                        if (!name.equals("")) {
                            System.out.println("facultyyyyy " + name);
                        }
                        String result = name.replaceAll("[-+.^:,]", "");
                        databaseReference.child(result).child(prsntday).child(year).child(timing).setValue(saveFaculties).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    // null values for faculites
                                    String key = section + "," + subject;
                                    ArrayList<String> faculties = subjects.get(key.trim());

                                    if (faculties != null) {
                                        if (faculties.size() >= 2) {
                                            System.out.println(periodnumber + ", " + faculties.get(0) + ", " + section + ", " + prsntday + ", " + subject + ", " + room + ", " + timing);
                                            DatabaseReference studentsaving = FirebaseDatabase.getInstance().getReference("StudentDetails");
                                            students newstudent = new students(periodnumber, faculties.get(0), section, prsntday, subject, room, timing);
                                            studentsaving.child(section).child(prsntday).child(timing).setValue(newstudent);
                                            Log.e("lab data : ", "saved");
                                        } else {
                                            System.out.println(periodnumber + ", " + finalFaculty + ", " + section + ", " + prsntday + ", " + subject + ", " + room + ", " + timing);
                                            DatabaseReference studentsaving = FirebaseDatabase.getInstance().getReference("StudentDetails");
                                            students newstudent = new students(periodnumber, finalFaculty, section, prsntday, subject, room, timing);
                                            studentsaving.child(section).child(prsntday).child(timing).setValue(newstudent);
                                            Log.e("non lab data : ", "saved");
                                        }
                                    }

                                }
                            }
                        });
                        //System.out.println("save cheystaaa poo");
                    }
                }

                //pmob tues 2 hymavathii
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        //modifyFacultyDetails();
        MyApplication.namesmap = namesmap;
        loadingDialog.dismisss();

    }


    private  void  modifyFacultyDetails(){

        HashMap<String,FacultyDetailsObject> facultydetails;
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
                changeDetailsOfFaculty(facultydetails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private  void changeDetailsOfFaculty(HashMap<String,FacultyDetailsObject> facultydetails){

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


                    ArrayList<String> databaseSubs = facultydetails.get(name).list;
                    String currentYear = MyApplication.currentYear;
                    finalSubs = (ArrayList<String>) databaseSubs.clone();
                    if(namesmap.containsKey(name)){
                        ArrayList<String> currentSubs = namesmap.get(name);

                        System.out.print("Database :     ");
                        for(String sub:databaseSubs)
                            if(sub.startsWith(currentYear))
                                System.out.print(sub+" ;");
                        System.out.println();
               /* for(String sub:currentSubs)
                    if(sub.startsWith(currentYear))
                        System.out.print(sub+" ;");
                System.out.println();*/
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
                        System.out.print("Current File : ");
                        for(String sub:currentSubs)
                            if(sub.startsWith(currentYear))
                                System.out.print(sub+" ;");
                        System.out.println();

                        for(String str:databaseSubs){
                            if(str.startsWith(currentYear)){
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

                    }else{
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
                    }

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

                    FirebaseDatabase.getInstance().getReference("FacultyDealing").child(name).child("list").setValue(finalSubs);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





        }

        loadingDialog.dismisss();


    }

    private void student(List<String> entireList, List<String> entireroomsList, HashMap<String, ArrayList> subjects) {
        int columncount=0;
        for(String key : entireList) {
            if(key.contains("M Tech") || key.contains("II") || key.contains("IV") || key.contains("III") || key.contains("I")) {
                columncount=0;
            }else {
                columncount+=1;
            }
        }
        System.out.println(columncount);
        int c=0;
        List<String>entireListwithoutrepeation = new ArrayList<>();
        List<String>entireroomsListwithoutrepeation = new ArrayList<>();
        List<String>repeationdays = new ArrayList<>();
        for(int period=0;period < entireList.size();period++) {
            if(c <= columncount) {
                //System.out.println(c);
                repeationdays.add(entireList.get(period));
            }
            if(c == columncount) {
                int x = columncount;
                while(x > 8) {
                    repeationdays.remove(x-1);
                    x-=1;
                }
                for(String value : repeationdays) {
                    entireListwithoutrepeation.add(value);
                }
                repeationdays.clear();
                c=-1;
            }
            c+=1;
        }
        entireList.clear();
        for(String value : entireListwithoutrepeation) {
            entireList.add(value);
        }
        repeationdays.clear();
        columncount-=1;
        for(int period=0;period < entireroomsList.size();period++) {
            if(c <= columncount) {
                //System.out.println(c);
                repeationdays.add(entireroomsList.get(period));
            }
            if(c == columncount) {
                int x = columncount;
                while(x > 7) {
                    repeationdays.remove(x-1);
                    x-=1;
                }
                for(String value : repeationdays) {
                    entireroomsListwithoutrepeation.add(value);
                }
                repeationdays.clear();
                c=-1;
            }
            c+=1;
        }
        entireroomsList.clear();
        for(String value : entireroomsListwithoutrepeation) {
            entireroomsList.add(value);
        }
        System.out.println(entireList+"\n"+entireroomsList);
        columncount=8;
        //String[] timings = {"08:05-09:00","09:00-09:55","10:15-11:10","11:10-12:05","12:05-01:00","02:00-02:55","02:55-03:50","03:50-4:40","04:40-05:30"};
        int sub=2,room=1,subcount=0,roomcount=0,periodnumber=1;
        String Prsntday=null,section=null;
        while(sub < entireList.size() && room < entireroomsList.size()) {
            if(sub == 2 && room == 1) {
                section = entireList.get(0);
                Prsntday = entireList.get(1);
            }
            if(subcount == columncount-1 && roomcount == columncount-1) {
                subcount=roomcount=0;
                sub+=2;
                room+=1;
                Prsntday = entireList.get(sub-1);
                section = entireList.get(sub-2);
                periodnumber=1;
            }
            ArrayList faculty = subjects.get(section.trim()+","+entireList.get(sub).trim());

            if(faculty != null) {

                if(periodnumber<8) {
                    //System.out.println(periodnumber+", "+timings[periodnumber-1]+", "+section+", "+Prsntday+", "+entireList.get(sub)+" "+entireroomsList.get(room)+", "+faculty.get(0));
                    StudentFirebase(timings[periodnumber - 1], periodnumber, section, Prsntday, entireList.get(sub), entireroomsList.get(room).toString(), faculty.get(0).toString());
                }
            }else {
                //System.out.println(entireList.get(sub)+"*****");
                if((entireList.get(sub).contains("SCIRP") || entireList.get(sub).contains("IDP")) && !entireList.get(sub).contains("Honors/Scirp") && !entireList.contains("Library/Scirp")&& !entireList.get(sub).contains("Honors/SCIRP") && !entireList.contains("Library/SCIRP")) {
                    //System.out.println(entireList.get(sub)+"*****If Loki ochiindi****");
                    String copy = entireList.get(sub);
                    String facultyNameIDP = entireList.get(sub).substring(entireList.get(sub).lastIndexOf(")")+1,entireList.get(sub).length());
                    facultyNameIDP = facultyNameIDP.replaceAll("[-+.^:, ]","").toLowerCase(Locale.ROOT);
                    String subject = copy.substring(0,copy.indexOf("("));
                    //System.out.println(periodnumber+", "+timings[periodnumber-1]+", "+section+", "+Prsntday+", "+subject+", "+entireroomsList.get(room)+", "+facultyNameIDP);
                    if(periodnumber!=8 && !Prsntday.contains("Sat")) {
                        StudentFirebase(timings[periodnumber - 1], periodnumber, section, Prsntday, subject, entireroomsList.get(room).toString(), facultyNameIDP);
                    }
                }else {
                    //System.out.println(entireList.get(sub)+"*****ELse Loki ochiindi****");
                    if(periodnumber<8) {
                        //System.out.println(periodnumber + ", " + timings[periodnumber - 1] + ", " + section + ", " + Prsntday + ", " + entireList.get(sub) + ", " + entireroomsList.get(room) + ", " + "not mentioned");
                        StudentFirebase(timings[periodnumber - 1], periodnumber, section, Prsntday, entireList.get(sub), entireroomsList.get(room).toString(), "not mentioned");
                    }
                }
                //StudentFirebase(timings[periodnumber-1],periodnumber,section,Prsntday,entireList.get(sub),entireroomsList.get(room).toString(),"not mentioned");
            }
            subcount+=1;
            roomcount+=1;
            sub+=1;
            room+=1;
            periodnumber+=1;
        }
    }

    public void StudentFirebase(String time,int period, String section, String prsntday, String subject, String room, String faculty) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("StudentDetails");
        students st = new students(period,faculty,section,prsntday,subject,room,time);
        //..hello jsd
        if(!st.getSub().equals("***") && !st.getSub().contains("Sat") ) {
            System.out.println("students : "+time+", "+st.getPer()+", "+st.getDay()+", "+st.getSec()+", "+st.getSub()+", "+st.getRoom()+", "+st.getFaculty());
            databaseReference.child(section).child(prsntday).child(time).setValue(st).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Log.e("yupee","done");
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            startActivity(new Intent(getApplicationContext(),AdminActivity.class));
            finish();
        }


    public void facultyFirebasefornull(String timing, int periodnumber, String section, String prsntday, String subject, String room, String faculty) {

        DatabaseReference data = FirebaseDatabase.getInstance().getReference("StudentDetails");
        DatabaseReference data1 = FirebaseDatabase.getInstance().getReference("FacultyDetails");
        students st = new students(periodnumber,faculty,section,prsntday,subject,room,timing);
        data.child(section).child(prsntday).child(timing).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("json : ",dataSnapshot.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void getFacultyCLassSection(String section,String year, String subject){
        section = section.replaceAll("[ ]","");
        year = year.replaceAll("[ ]","");
        subject = subject.replaceAll("[ ]","");



        temporaryFacultyList.clear();
        String finalSection = section;
        String finalSubject = subject;
        String finalYear = year;
        FirebaseDatabase.getInstance().getReference().child("FacultyDealing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot faculty:snapshot.getChildren()){
                        FacultySearchItems item = faculty.getValue(FacultySearchItems.class);

                        ArrayList<String> subs = item.getList();
                        for(String sub:subs){
                            String parts[] = sub.split(",");
                            String[] part1 = parts[0].split("-");

                            String curr_sec = parts[0].replaceAll("[ ]","");
                            String curr_sub = parts[1];

                            System.out.println(curr_sec+" "+curr_sub);
                            System.out.println(finalSection +" "+ finalSubject +" "+ finalYear);

                            if(curr_sub.equalsIgnoreCase(finalSubject) && curr_sec.equalsIgnoreCase(finalSection)){
                                temporaryFacultyList.add(faculty.getKey());
                            }
                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


/*
Faculties faculties;
    public void facultyData(students st, String timing) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FacultyDetails");
        ArrayList<String> facultydetails = new ArrayList<String>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()) {
                    //Log.e("data : ",snap.getValue().toString());
                    facultydetails.add(snap.getKey());
                }
                for(String id : facultydetails) {

                    facultysave(id,st,timing);
                }
            }

            public void facultysave(String id, students st, String timing) {
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("FacultyDetails");
                databaseReference1.child(id).child("Details").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String id = dataSnapshot.child("Id").getValue(String.class);
                        String name = dataSnapshot.child("Name").getValue(String.class);
                        String subject = dataSnapshot.child("subject").getValue(String.class);
                        faculties = new Faculties(id,name,subject);
                        if(faculties.getId() == id) {
                            saveFaculties saveFaculties = new saveFaculties(faculties.getId(),st.getPer(),st.getSec(),st.getRoom(),st.getSub(),timing);
                            //databaseReference1.child(st.getDay()).child(st.getTime()).setValue(saveFaculties);
                            Log.e("student details : ",st.getPer()+", "+st.getTime()+", "+st.getRoom()+", "+st.getSec()+", "+st.getSub()+", "+st.getFaculty());
                            Log.e("Faculty details : ",saveFaculties.getPeriod()+", "+saveFaculties.getTime()+", "+saveFaculties.getRoom()+", "+saveFaculties.getSectionId()+", "+saveFaculties.getShortVal()+", "+saveFaculties.getFacultyId());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
 */
/*

public void FacultyFirebase(int i, int period, String section, String prsntday, String subject, String room, String faculty, int periodcount) {
        ArrayList<String> facultyDetails = new ArrayList<>();
        final Faculties[] faculties = new Faculties[1];
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("FacultyDetails");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e("snap : ",dataSnapshot.getValue().toString());
                for(DataSnapshot snap : dataSnapshot.getChildren()) {
                    //Log.e("ss : ",snap.getKey());
                    facultyDetails.add(snap.getKey());
                }
                //System.out.println("hello : "+facultyDetails);
                ff(facultyDetails);
            }

            private void ff(ArrayList<String> facultyDetails) {
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("FacultyDetails");
                databaseReference1.child(facultyDetails.get(0)).child("Details").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Id = dataSnapshot.child("Id").getValue(String.class);
                        String Name = dataSnapshot.child("Name").getValue(String.class);
                        String sub = dataSnapshot.child("subject").getValue(String.class);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

 */


/*
private void FacultyFirebase(ArrayList<String> list, List<String> entireList, List entireroomsList,HashMap<String,ArrayList> subjects) {

        int columncount=0;
        for(String key : entireList) {
            if(key.contains("II")) {
                columncount=0;
            }else {
                columncount+=1;
            }
        }
        String[] timings = {"8:10-9:00","9:00-9:50","10:05-10:55","10:55-11:45","11:45-12:35","1:30-2:20","2:20-3:10","3:10-4:00","4:00-4:50"};
        int sub=2,room=1,subcount=0,roomcount=0,periodnumber=1;
        String Prsntday=null,section=null;
        while(sub < entireList.size() && room < entireroomsList.size()) {
            if(sub == 2 && room == 1) {
                section = entireList.get(0);
                Prsntday = entireList.get(1);
            }
            if(subcount == columncount-1 && roomcount == columncount-1) {
                subcount=roomcount=0;
                sub+=2;
                room+=1;
                Prsntday = entireList.get(sub-1);
                section = entireList.get(sub-2);
                periodnumber=1;
            }
            int t=0;
            ArrayList faculty = subjects.get(section.trim()+","+entireList.get(sub).trim());
            //System.out.println(subjects.get(section.trim()+","+entireList.get(sub).trim()));
            if(faculty != null && faculty.size() > 1) {
                while(t  < faculty.size()) {
                    int l=0;
                    System.out.println(periodnumber+", "+timings[periodnumber-1]+", "+section+", "+Prsntday+", "+entireList.get(sub)+" "+entireroomsList.get(room)+", "+faculty.get(t));
                    t++;
                }
            }else if(faculty != null && faculty.size() == 1) {
                System.out.println(periodnumber+", "+timings[periodnumber-1]+", "+section+", "+Prsntday+", "+entireList.get(sub)+" "+entireroomsList.get(room)+", "+faculty.get(0));
            }
            subcount+=1;
            roomcount+=1;
            sub+=1;
            room+=1;
            periodnumber+=1;
        }

    }
 */
}