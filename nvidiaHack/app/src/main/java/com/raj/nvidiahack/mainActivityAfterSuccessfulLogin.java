package com.raj.nvidiahack;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class mainActivityAfterSuccessfulLogin extends AppCompatActivity {
    private static final String TAG = "afterLoginActivity";

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;


    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;
    SharedPreferences file;
    Boolean flag;
    String uid;

    //Info attendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_after_successful_login);

        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        mDatabaseReference=FirebaseDatabase.getInstance().getReference();

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);



        ///Storage of flag value after the app is killed or shutdown
        file = getSharedPreferences("save", 0);
        flag = Boolean.valueOf(file.getString("flag", "false"));
        if (flag == false)////File permission check
            checkFilePermissions();

        SharedPreferences.Editor edit = file.edit();
        edit.putString("flag", String.valueOf(flag));
        edit.apply();


        ////Real time database update and control
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
        ArrayList<String> months=new ArrayList<>();
        for(DataSnapshot snap:dataSnapshot.child(uid).child("attendance").child("2018").getChildren())
        {
            months.add(snap.getKey());
        }
        Log.i("months",months.toString());

        ArrayList<String> days=new ArrayList<>();
        for(int i=1;i<=months.size();i++) {
            for (DataSnapshot snap : dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(i)).getChildren()) {
                days.add(snap.getKey());
            }
        }
        Log.i("monthsDays", days.toString());

        Log.i("monthsDays", String.valueOf(days.size()));
        int j=1;
        String s="";
        ArrayList<String> attendanceStringArray=new ArrayList<>();

        for(int i=0;i<days.size();i++) {
            if(Objects.equals(days.get(i), "lateCount"))
            {
                j++;
            }
            else
            {
                if(Boolean.valueOf(String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("late").getValue())))
                {
                    Log.i( "  Date: "+String.valueOf(days.get(i))+"-"+String.valueOf(j)+"-"+"2018  "+" Late",String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("late").getValue()).concat("   ").concat(String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("Time").getValue())));
                    s=String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("late").getValue()).concat("   ").concat(String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("Time").getValue()));
                }
                else
                {
                    Log.i( "  Date: "+String.valueOf(days.get(i))+"-"+String.valueOf(j)+"-"+"2018  "+" Late",String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("late").getValue()).concat("  ").concat(String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("Time").getValue())));
                    s=String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("late").getValue()).concat("  ").concat(String.valueOf(dataSnapshot.child(uid).child("attendance").child("2018").child(String.valueOf(j)).child(String.valueOf(days.get(i))).child("Time").getValue()));
                }
                attendanceStringArray.add(s);
            }

        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,attendanceStringArray);


    }

    void attendance(View v)
    {
        startActivity(new Intent(getApplicationContext(),attendanceActivity.class));
    }


    void print(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser() == null) {

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }
        // updateUI(currentUser);
    }



    ////File Permission Check
    private void checkFilePermissions() {


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = mainActivityAfterSuccessfulLogin.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += mainActivityAfterSuccessfulLogin.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
            flag = true;
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }

    }


    ///textToSpeechActivity Button onClick for Navigating to TTS activity

    public void logout(View v)
    {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        mAuth.signOut();
        finish();
    }

    public void chatBot(View v)
    {
        startActivity(new Intent(getApplicationContext(),chatbotUIMain.class));
    }

    public void heatMap(View v)
    {


    }

    public void amazonGo(View v)
    {


    }

    public void switches(View v)
    {



    }



}


