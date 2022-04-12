package com.cse.timetableapp;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.cse.timetableapp.R;

import java.util.List;

public class FacultyAdapter extends ArrayAdapter {

    List<String> facultyIds;
    Context context;

    public FacultyAdapter(@NonNull Context context, List<String> facultyIds) {
        super(context, R.layout.admin_item,facultyIds);
        this.context = context;
        this.facultyIds = facultyIds;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_item,parent,false);
        TextView textView = v.findViewById(R.id.facult_id);
        String text = facultyIds.get(position);
        if(text.contains("LAB")){
            textView.setTextColor(context.getColor(R.color.black));
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(18);
        }
        textView.setText(facultyIds.get(position));
        return v;
    }
}
