package com.cse.timetableapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cse.timetableapp.R;

import java.util.List;

public class FacultyWorkloadAdapter extends ArrayAdapter<WorkLoads> {
    Context context;
    List<WorkLoads> workload;


    public FacultyWorkloadAdapter(@NonNull Context context, int resource, @NonNull List<WorkLoads> list) {
        super(context, R.layout.workload_item, list);
        this.context = context;
        this.workload = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.workload_item,parent,false);
        TextView t1 = v.findViewById(R.id.faculty_id_working);
        TextView t2  = v.findViewById(R.id.faculty_work_load);
        TextView t3 = v.findViewById(R.id.faculty_name_working);
        WorkLoads work = workload.get(position);
        int work_hours = Integer.parseInt(work.workload);
        if(work_hours>=20)
            t2.setTextColor(Color.parseColor("#FF0000"));
        t1.setText(work.getFaculty_id());
        t2.setText(work.getWorkload());
        t3.setText(work.getFaculty_name());
        return v;
    }
}
