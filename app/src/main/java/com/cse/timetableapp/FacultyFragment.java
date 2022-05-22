package com.cse.timetableapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cse.timetableapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FacultyFragment extends Fragment {

    com.google.android.material.button.MaterialButton button;
    com.google.android.material.textfield.TextInputEditText editText;
    com.google.android.material.checkbox.MaterialCheckBox checkBox;
    String faculty_id ="";
    TextView textView;
    View root;
    SharedPreferences preferences;
    static final String filename = "remember";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.faculty_tab_fragment,container,false);

        editText = root.findViewById(R.id.FacultyInputText);
        button = root.findViewById(R.id.FacultySubmitButton);
        textView = root.findViewById(R.id.adminLogin);
        checkBox = root.findViewById(R.id.faculty_login_checkbox);

        //CheckBox Setup And Preferences
        preferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
        String check = preferences.getString("remember","");
        if(check.equalsIgnoreCase("true")){

            faculty_id =preferences.getString("faculty_id","").toString();
            if(!faculty_id.equals(""))
                callNext(faculty_id);


        }else if(!check.equalsIgnoreCase("true")){
            //Toast.makeText(root.getContext(), "Not  Saved!!!", Toast.LENGTH_SHORT).show();

        }


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){

                    SharedPreferences preferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText(getContext(), "Login Saved", Toast.LENGTH_SHORT).show();

                }else if(!buttonView.isChecked()){
                    SharedPreferences preferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(getContext(), "Login Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String check = editText.getText().toString();
                callNext(check);
            }
        });
        
        
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext(),AdminLogin.class));
            }
        });

        return root;
    }

    public void callNext(String check){
        if(!check.equals("")){
            SharedPreferences preferences=getActivity().getSharedPreferences(filename,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("faculty_id",check);
            editor.putString("section","");
            editor.apply();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FacultyDetails");
            String str = "";
            char ch[] = check.toCharArray();
            int flag =0;
            while(ch[flag]=='0')
                flag++;
            str = new String(ch,flag,ch.length-flag);

            Query checkData = databaseReference.child(str);
            checkData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String str = "";
                        char ch[] = check.toCharArray();
                        int flag =0;
                        while(ch[flag]=='0')
                            flag++;
                        str = new String(ch,flag,ch.length-flag);
                        Intent intent = new Intent(root.getContext(),MainActivity.class);
                        intent.putExtra("type","faculty");
                        intent.putExtra("text",str);
                        startActivity(intent);
                        getActivity().finish();
                    }else{
                        Toast.makeText(root.getContext(), "Check Input!!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else{
            Toast.makeText(root.getContext(), "Please Enter ID...", Toast.LENGTH_SHORT).show();
        }
    }

}
