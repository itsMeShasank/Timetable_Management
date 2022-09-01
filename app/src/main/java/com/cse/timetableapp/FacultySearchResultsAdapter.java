package com.cse.timetableapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FacultySearchResultsAdapter extends ArrayAdapter<FacultySearchItems> {

    Context context;
    List<FacultySearchItems> facultySearchItems;


    public FacultySearchResultsAdapter(@NonNull Context context, int resource, @NonNull List<FacultySearchItems> facultySearchItems) {
        super(context, R.layout.faculty_search_item, facultySearchItems);
        this.context = context;
        this.facultySearchItems = facultySearchItems;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = LayoutInflater.from(context).inflate(R.layout.faculty_search_item,parent,false);
        TextView id = v.findViewById(R.id.faculty_search_id);
        TextView name = v.findViewById(R.id.faculty_search_name);


        id.setText(facultySearchItems.get(position).getId());
        name.setText(facultySearchItems.get(position).getName());

        return v;




    }
}
