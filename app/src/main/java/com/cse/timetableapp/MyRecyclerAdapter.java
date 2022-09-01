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

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    Context context;
    List<FacultyData> facultyData = new ArrayList<>();

    public MyRecyclerAdapter(Context context, List<FacultyData> facultyData) {
        this.context = context;
        this.facultyData = facultyData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            FacultyData faculty = facultyData.get(position);
            holder.faculty_id.setText(""+faculty.getPeriod());

            holder.short_val.setText(faculty.getShortVal());
            holder.faculty_name.setText(faculty.getName());
            holder.class_id.setText(faculty.getSectionId());
            holder.room.setText(faculty.getRoom());
            holder.period_time.setText(faculty.getTime());
    }

    @Override
    public int getItemCount() {
        return facultyData.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView faculty_name,faculty_id,class_id,short_val,room,period_time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            faculty_name = itemView.findViewById(R.id.faculty_name);
            faculty_id = itemView.findViewById(R.id.faculty_id);
            class_id = itemView.findViewById(R.id.class_name);
            short_val = itemView.findViewById(R.id.short_name);
            room = itemView.findViewById(R.id.room_id);
            short_val.setTypeface(null, Typeface.BOLD_ITALIC);
            period_time = itemView.findViewById(R.id.period_time);
            /*
            *
            * String shortValue = faculty.getShortVal();
            if(shortValue.contains("LAB")){
                Log.e("Helooooo",shortValue);
                holder.short_val.setTypeface(null, Typeface.BOLD_ITALIC);
                //holder.short_val.getTypeface()
            }
            *
            * */
        }
    }

}
