package com.cse.timetableapp;

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

import java.util.ArrayList;
import java.util.List;

public class Wednesday extends Fragment {
    LinearLayout linearLayout;
    View root;
    RecyclerView recyclerView;
    List<FacultyData> facultyData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.wednesday_layout,container,false);


        facultyData = new ArrayList<>();
        if(getArguments()!=null){
            facultyData = getArguments().getParcelableArrayList("faculty");
            //Log.e("data","vachindi");
        }

        linearLayout = root.findViewById(R.id.wednesday_layout);

        recyclerView = root.findViewById(R.id.wednesday_recycler_view);
        MyRecyclerAdapter myRecyclerAdapter = new MyRecyclerAdapter(getContext(),facultyData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myRecyclerAdapter);

        return root;
    }


}
