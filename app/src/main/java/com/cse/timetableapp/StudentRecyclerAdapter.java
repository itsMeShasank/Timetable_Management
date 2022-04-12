package com.cse.timetableapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cse.timetableapp.R;

import java.util.List;

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentRecyclerAdapter.StudentViewHolder>{

    Context context;
    List<StudentData> studentDataList;

    public StudentRecyclerAdapter(Context context, List<StudentData> studentDataList) {
        this.context = context;
        this.studentDataList = studentDataList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.student_item,parent,false);
        StudentViewHolder studentViewHolder = new StudentViewHolder(v);
        return studentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentData studentData = studentDataList.get(position);
        String str = studentData.getPer();
        if(str.contains("LAB"))
            holder.period.setTypeface(holder.period.getTypeface(), Typeface.BOLD);

        holder.period.setText(studentData.getPer());
        holder.faculty.setText(studentData.getFaculty());
        holder.room.setText(studentData.getRoom());
        holder.sub.setText(studentData.getSub());
    }

    @Override
    public int getItemCount() {
        return studentDataList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder{

        TextView period,faculty,room,sub;
        public StudentViewHolder(@NonNull View view) {
            super(view);
            period = view.findViewById(R.id.period_id);
            faculty = view.findViewById(R.id.faculty_id);
            room = view.findViewById(R.id.room_id);
            sub = view.findViewById(R.id.subject_id);
        }
    }
}
