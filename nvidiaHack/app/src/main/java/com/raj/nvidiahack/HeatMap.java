package com.raj.nvidiahack;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HeatMap extends AppCompatActivity {

    StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    String uid;
    TextView peopleCount;
    Integer heatMapImageCount;
    Integer heatMappeopleCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heat_map);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        progressDialog.setMessage("Loading...");
        uid=mAuth.getCurrentUser().getUid();

        peopleCount=findViewById(R.id.peopleCount);
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();



        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                heatMappeopleCount =dataSnapshot.child("Heatmap").child("numberOfPeople").getValue(Integer.class);
                heatMapImageCount=dataSnapshot.child("HeatMapImageCount").getValue(Integer.class);

                progressDialog.show();
                StorageReference filepath=mStorage.child("/main"+String.valueOf(heatMapImageCount)+".png");
                final long ONE_MEGABYTE = 1024 * 1024;
                filepath.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        progressDialog.dismiss();
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        ImageView image = (ImageView) findViewById(R.id.heatMapImage);

                        image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(),
                                image.getHeight(), false));
                        peopleCount.setText(String.valueOf(heatMappeopleCount));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
}
