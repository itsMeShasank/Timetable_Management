package com.cse.timetableapp.Days;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cse.timetableapp.R;
import com.cse.timetableapp.StudentData;
import com.cse.timetableapp.StudentRecyclerAdapter;

import java.util.ArrayList;

public class StudentThursday extends Fragment {
    LinearLayout linearLayout;
    View root;
    RecyclerView recyclerView;
    ArrayList<StudentData> studentData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.thursday_layout,container,false);
        studentData = new ArrayList<>();

        if(getArguments()!=null){
            studentData = getArguments().getParcelableArrayList("student");
            //Log.e("data","vachindi");
        }



        linearLayout = root.findViewById(R.id.thursday_layout);
        recyclerView = root.findViewById(R.id.thursday_recycler_view);
        StudentRecyclerAdapter studentRecyclerAdapter = new StudentRecyclerAdapter(getContext(),studentData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(studentRecyclerAdapter);

        return root;
    }


}
