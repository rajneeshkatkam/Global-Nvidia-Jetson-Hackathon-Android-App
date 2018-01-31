package com.raj.nvidiahack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class attendanceActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        mAuth=FirebaseAuth.getInstance();
        mDatabaseReference=FirebaseDatabase.getInstance().getReference();
        uid=mAuth.getCurrentUser().getUid();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchValuesFromDatabase(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    ////DataBase Update and Fetch

    public void fetchValuesFromDatabase(DataSnapshot dataSnapshot)
    {
        /// Attendance Status
        //long attendance =dataSnapshot.child(uid).child("attendance").child("2018").child("1").child("4").getChildrenCount();
        Log.i("attendance", Arrays.asList(dataSnapshot.child(uid).child("attendance")).toString());

    }






}
