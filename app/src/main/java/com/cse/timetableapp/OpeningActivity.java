package com.cse.timetableapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cse.timetableapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OpeningActivity extends AppCompatActivity {

    com.google.android.material.textfield.TextInputEditText editText;
    com.google.android.material.button.MaterialButton materialButton,materialButton1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening2);

        editText = findViewById(R.id.getIdText);
        materialButton = findViewById(R.id.submitButton);
        materialButton1 = findViewById(R.id.submitButton2);


        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FacultyDetails");
                Query checkData = databaseReference.child(editText.getText().toString());
                checkData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String text = editText.getText().toString();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("type","faculty");
                            intent.putExtra("text",text);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(OpeningActivity.this, "Check Input!!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        materialButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ClassDetails");
                Query checkData = databaseReference.child(editText.getText().toString());
                checkData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String text = editText.getText().toString();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("type","student");
                            intent.putExtra("text",text);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(OpeningActivity.this, "Check Input!!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
}