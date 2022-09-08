package com.cse.timetableapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.cse.timetableapp.R;

public class secondyearStudent extends Fragment {

    View root;
    com.google.android.material.button.MaterialButton button;
    Spinner spinner;
    String selected_class;
    com.google.android.material.checkbox.MaterialCheckBox checkBox;
    SharedPreferences preferences;
    static final String filename = "remember";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.student_tab_fragment,container,false);


        button = root.findViewById(R.id.studentSubmit);
        checkBox = root.findViewById(R.id.login_checkbox);

        //CheckBox Setup And Preferences
        preferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
        String check = preferences.getString("remember","");
        if(check.equalsIgnoreCase("true")){

            selected_class =preferences.getString("section","").toString();
            if(!selected_class.equals(""))
                callNext();


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





        spinner = root.findViewById(R.id.spinner_student);
        selected_class = "";
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(),
                R.array.secondyears, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_class = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_class = spinner.getSelectedItem().toString();

                callNext();
            }
        });

        return root;
    }
/*
if(isConectedToInternet(secondyearStudent.this)) {
                    customDialog();
                }
 */


    public void callNext(){
        if(selected_class.equals("") || selected_class.equals("Section")){
            Toast.makeText(root.getContext(), "Select Valid Option...", Toast.LENGTH_SHORT).show();
        }
        else{

            SharedPreferences preferences=getActivity().getSharedPreferences(filename,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("section",selected_class);
            editor.putString("faculty_id","");
            editor.apply();
            String text = selected_class;
            Intent intent = new Intent(root.getContext(),MainActivity.class);
            intent.putExtra("type","student");
            intent.putExtra("text",text);
            startActivity(intent);
            getActivity().finish();
        }
    }


}