package com.cse.timetableapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cse.timetableapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class FacultyFragment extends Fragment {


    LoadingDialog loadingDialog;
    RadioButton name,id;
    com.google.android.material.button.MaterialButton button;
    com.google.android.material.textfield.TextInputEditText editText;
    com.google.android.material.checkbox.MaterialCheckBox checkBox;
    String faculty_id ="";
    TextView textView;
    View root;
    SharedPreferences preferences;
    static final String filename = "remember";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.faculty_tab_fragment,container,false);

        loadingDialog = new LoadingDialog(getActivity());
        name = root.findViewById(R.id.SearchWithName);
        id = root.findViewById(R.id.SearchingWithID);
        editText = root.findViewById(R.id.FacultyInputText);
        button = root.findViewById(R.id.FacultySubmitButton);
        textView = root.findViewById(R.id.adminLogin);
        checkBox = root.findViewById(R.id.faculty_login_checkbox);

        //CheckBox Setup And Preferences
        preferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
        String check = preferences.getString("remember","");
        if(check.equalsIgnoreCase("true")){

            faculty_id =preferences.getString("faculty_id","").toString();
            System.out.println("Saved   "+faculty_id);
            if(!faculty_id.equals("")){
                System.out.println(faculty_id);
                loadingDialog.load();
                String type = preferences.getString("type","").toString();
                System.out.println(type);
                if(type.equals("name"))
                    searchForMatchingName(faculty_id);
                else
                    callNext(faculty_id);

            }


        }else if(!check.equalsIgnoreCase("true")){
            //Toast.makeText(root.getContext(), "Not  Saved!!!", Toast.LENGTH_SHORT).show();

        }


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){

                    SharedPreferences preferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText(getContext(), "Login Saved", Toast.LENGTH_SHORT).show();

                }else if(!buttonView.isChecked()){
                    SharedPreferences preferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(getContext(), "Login Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String check = editText.getText().toString().trim();
                loadingDialog.load();
                if(name.isChecked()){
                    check = check.trim().toLowerCase(Locale.ROOT);
                    searchForMatchingName(check);
                }else{
                    callNext(check);
                }
            }
        });
        
        
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext(),AdminLogin.class));
            }
        });

        return root;
    }


    public void searchForMatchingName(String name){
        FirebaseDatabase.getInstance().getReference("FacultyDealing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<FacultySearchItems> results = new ArrayList<>();
                ArrayList<String> searchResults = new ArrayList<>();
                for(DataSnapshot snap:snapshot.getChildren()){

                    String key = snap.getKey();
                    Log.e("snapOchinid",key+"    "+name);
                    if(key.contains(name)){
                        Log.e("Mathced","error");
                        FacultySearchItems facultyData = snap.getValue(FacultySearchItems.class);
                        //FacultySearchItems item = new FacultySearchItems(key,key);
                        searchResults.add(snap.getKey());
                        results.add(facultyData);
                    }
                }

                /*for(FacultySearchItems i:results){
                    Log.e("Name",i.getName());
                }*/

                if(results.size()==1){

                    if(checkBox.isChecked()){
                        SharedPreferences preferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("faculty_id", searchResults.get(0));
                        editor.putString("type", "name");
                        editor.putString("section", "");
                        editor.putString("isFacultySaved","yes");
                        editor.apply();
                    }

                    loadingDialog.dismisss();
                    Intent intent = new Intent(root.getContext(),MainActivity.class);
                    intent.putExtra("type","faculty");
                    intent.putExtra("text",searchResults.get(0));
                    intent.putExtra("FacultyDetails",results.get(0));
                    startActivity(intent);
                    getActivity().finish();

                }
                else if(results.size()>1){
                    loadingDialog.dismisss();
                    Intent intent = new Intent(getContext(), FacultyResults.class);
                    intent.putParcelableArrayListExtra("SearchResults", results);
                    intent.putStringArrayListExtra("IdsForFaculty",searchResults);
                    startActivity(intent);
                }else{
                    loadingDialog.dismisss();
                    Toast.makeText(getActivity(), "No Matching Key Words", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void callNext(String check){
        if(!check.equals("")){
            SharedPreferences preferences=getActivity().getSharedPreferences(filename,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("faculty_id",check);
            editor.putString("section","");
            editor.apply();

            String str = "";
            char ch[] = check.toCharArray();
            int flag =0;
            while(ch[flag]=='0')
                flag++;
            str = new String(ch,flag,ch.length-flag);

           /* DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FacultyDetails");
            Query checkData = databaseReference.child(str);
            checkData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String str = "";
                        char ch[] = check.toCharArray();
                        int flag =0;
                        while(ch[flag]=='0')
                            flag++;
                        str = new String(ch,flag,ch.length-flag);
                        Intent intent = new Intent(root.getContext(),MainActivity.class);
                        intent.putExtra("type","faculty");
                        intent.putExtra("text",str);
                        startActivity(intent);
                        getActivity().finish();
                    }else{
                        Toast.makeText(root.getContext(), "Check Input!!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/



            FirebaseDatabase.getInstance().getReference("FacultyDealing").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String str = "";
                    char ch[] = check.toCharArray();
                    int flag =0;
                    while(ch[flag]=='0')
                        flag++;
                    str = new String(ch,flag,ch.length-flag);
                    for(DataSnapshot snap:snapshot.getChildren()){
                        FacultySearchItems facultyData = snap.getValue(FacultySearchItems.class);
                        String key = facultyData.getId();
                        Log.e("snapOchinid",key+"    "+name);
                        if(key.equalsIgnoreCase(str)){
                            Log.e("Mathced","error");

                            if(checkBox.isChecked()){
                                SharedPreferences preferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("remember","true");
                                editor.putString("type","id");
                                editor.putString("isFacultySaved","yes");
                                editor.apply();
                                Toast.makeText(getContext(), "Login Saved", Toast.LENGTH_SHORT).show();
                            }

                            loadingDialog.dismisss();
                            Intent intent = new Intent(getContext(),MainActivity.class);
                            intent.putExtra("type","faculty");
                            intent.putExtra("text",snap.getKey());
                            intent.putExtra("FacultyDetails",facultyData);
                            startActivity(intent);



                        }
                    }

                /*for(FacultySearchItems i:results){
                    Log.e("Name",i.getName());
                }*/
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else{
            Toast.makeText(root.getContext(), "Please Enter ID...", Toast.LENGTH_SHORT).show();
        }
    }

}
