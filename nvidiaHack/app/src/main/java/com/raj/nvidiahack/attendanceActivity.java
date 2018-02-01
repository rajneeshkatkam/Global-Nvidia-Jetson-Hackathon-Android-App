package com.raj.nvidiahack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static android.text.TextUtils.concat;

public class attendanceActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;
    String uid;
    ArrayList<String> attendanceStringArray;
    ArrayList<String> months;
    ArrayList<String> days;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        attendanceStringArray = new ArrayList<>();
        listView=findViewById(R.id.listView);
        months=new ArrayList<>();
        days=new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        uid = mAuth.getCurrentUser().getUid();

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

    public void fetchValuesFromDatabase(DataSnapshot dataSnapshot) {
        months.clear();
        for (DataSnapshot snap : dataSnapshot.child(uid).child("attendance").child("2018").getChildren()) {
            months.add(snap.getKey());
        }
       // Log.i("months", months.toString());

        days.clear();
        for (int i = 1; i <= months.size(); i++) {
            for (DataSnapshot snap : dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(i)).getChildren()) {
                days.add(snap.getKey());
            }
        }
        Log.i("monthsDays", days.toString());

        Log.i("monthsDays", String.valueOf(days.size()));
        int j = 1;
        Boolean janMonthFlag=true;
        String s = "";
        attendanceStringArray.clear();


        for (int i = 0; i < days.size(); i++) {
            if(j==1 && janMonthFlag) {
                attendanceStringArray.add(monthReturn(j));
                janMonthFlag=false;
            }

            if (Objects.equals(days.get(i), "lateCount")) {
                s="        Late Attendance In This Month: "+String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child("lateCount").getValue());
                j++;
                attendanceStringArray.add(s);
                if(!(i==days.size()-1))                         ////this condition is for not adding the next month name into the list unless n until its date exists in that month
                {
                    attendanceStringArray.add("");
                    attendanceStringArray.add(monthReturn(j));
                }

            } else {
                if (Boolean.valueOf(String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("late").getValue()))) {
                  //  Log.i("  Date: " + String.valueOf(days.get(i)) + "-" + String.valueOf(j) + "-" + "2018  " + " Late", String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("late").getValue()).concat("   ").concat(String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("Time").getValue())));
                    s = " Date: " + String.valueOf(days.get(i)) + "-" + String.valueOf(j) + "-" + "2018   " + " Late: "+"Yes"+"      Time: "+String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("Time").getValue());
                } else {
                   // Log.i("  Date: " + String.valueOf(days.get(i)) + "-" + String.valueOf(j) + "-" + "2018  " + " Late", String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("late").getValue()).concat("  ").concat(String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("Time").getValue())));
                    s = " Date: " + String.valueOf(days.get(i)) + "-" + String.valueOf(j) + "-" + "2018   " + " Late: "+"No"+"      Time: "+String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("Time").getValue());
                }
                attendanceStringArray.add(s);
            }


        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,attendanceStringArray);
        listView.setAdapter(adapter);


        //Log.i("  Date: " + String.valueOf(days.get(i)) + "-" + String.valueOf(j) + "-" + "2018  " + " Late", String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("late").getValue()).concat("   ").concat(String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("Time").getValue())));



    }


    public String monthReturn(int i)
    {
        String s="";
        switch (i) {
            case 1: s="JANUARY";break;
            case 2: s="FEBRUARY";break;
            case 3: s="MARCH";break;
            case 4: s="APRIL";break;
            case 5: s="MAY";break;
            case 6: s="JUNE";break;
            case 7: s="JULY";break;
            case 8: s="AUGUST";break;
            case 9: s="SEPTEMBER";break;
            case 10: s="OCTOBER";break;
            case 11: s="NOVEMBER";break;
            case 12: s="DECEMBER";break;
        }

      return s;
    }


}
