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

public class SubjectFacultyAdapter extends ArrayAdapter<SubjectFaculty> {

    Context context;
    List<SubjectFaculty> faculties;


    public SubjectFacultyAdapter(@NonNull Context context, int resource, List<SubjectFaculty> faculties) {
        super(context, R.layout.subjectfacultyitem,faculties);
        this.context = context;
        this.faculties = faculties;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.subjectfacultyitem,parent,false);
        TextView t1;
        TextView t2;
        TextView t3;

        t1 = v.findViewById(R.id.subject_faculty_id);
        t2 = v.findViewById(R.id.subject_faculty_name);
        t3 = v.findViewById(R.id.subject_faculty_section);

        SubjectFaculty subjectFaculty = faculties.get(position);
        t1.setText(subjectFaculty.getId());
        t2.setText(subjectFaculty.getName());
        t3.setText(subjectFaculty.getSection());

        return v;

    }
}
