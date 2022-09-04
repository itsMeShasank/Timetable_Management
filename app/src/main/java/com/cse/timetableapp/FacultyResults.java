package com.cse.timetableapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FacultyResults extends AppCompatActivity {

    ListView listView;
    com.google.android.material.checkbox.MaterialCheckBox checkBox;
    com.google.android.material.button.MaterialButton submit;
    String faculty_id ="";
    TextView textView;
    View root;
    SharedPreferences preferences;
    static final String filename = "remember";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_results);


        listView = findViewById(R.id.listViewForSearchResults);
        checkBox = findViewById(R.id.faculty_search_checkbox);
        submit = findViewById(R.id.FacultySearchButton);
        preferences = getSharedPreferences(filename, Context.MODE_PRIVATE);



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){

                    SharedPreferences preferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText( getApplicationContext(), "Login Saved", Toast.LENGTH_SHORT).show();

                }else if(!buttonView.isChecked()){
                    SharedPreferences preferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Login Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ArrayList<String> idsForSendingNext = getIntent().getStringArrayListExtra("IdsForFaculty");
        ArrayList<FacultySearchItems> results = getIntent().getParcelableArrayListExtra("SearchResults");

        FacultySearchResultsAdapter adapter = new FacultySearchResultsAdapter(getApplicationContext(),R.layout.faculty_search_item,
                results);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FacultySearchItems result = results.get(i);
                String idToSearch = idsForSendingNext.get(i);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("type","faculty");
                intent.putExtra("text",idToSearch);
                intent.putExtra("FacultyDetails",result);
                startActivity(intent);
            }
        });





    }
}