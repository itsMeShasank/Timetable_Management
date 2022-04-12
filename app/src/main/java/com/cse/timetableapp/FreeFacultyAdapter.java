package com.cse.timetableapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cse.timetableapp.R;

import java.util.List;

public class FreeFacultyAdapter extends ArrayAdapter<FreeFacultyLoader> {
    Context context;
    List<FreeFacultyLoader> list;

    public FreeFacultyAdapter(@NonNull Context context, int resource, List<FreeFacultyLoader> list) {
        super(context, R.layout.free_faculty_item,list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.free_faculty_item,parent,false);
        TextView t1,t2;
        t1 = v.findViewById(R.id.free_facult_id);
        t2 = v.findViewById(R.id.free_faculty_name);

        FreeFacultyLoader f = list.get(position);
        t1.setText(f.getFacultyid());
        t2.setText(f.getFacultyname());
        return v;
    }
}
