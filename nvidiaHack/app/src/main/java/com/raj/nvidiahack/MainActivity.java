package com.raj.nvidiahack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    //private FirebaseAuth mAuth;
    EditText emailId;
    EditText password;
    Pattern p;
    Matcher m;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailId=(EditText) findViewById(R.id.emailId);
        password=(EditText) findViewById(R.id.password);
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        //mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {

            startActivity(new Intent(getApplicationContext(),mainActivityAfterSuccessfulLogin.class));
            Toast.makeText(getApplicationContext(),"Welcome back",Toast.LENGTH_LONG).show();
            finish();

        }
        // updateUI(currentUser);
    }




    void loginClick(View v)
    {
        String emailId1=emailId.getText().toString();
        String password1=password.getText().toString();
        p= Pattern.compile("@(.*?).com");
        m=p.matcher(emailId1);

        ///Email ID
        if(Objects.equals(emailId1, ""))
        {
            emailId.setError("Please enter your email id");
            emailId.requestFocus();
            //Toast.makeText(this,"Please enter your email id",Toast.LENGTH_SHORT).show();
        }
        else if(!m.find())
        {
            emailId.setError("Enter a valid Email Address");
            emailId.requestFocus();
            // Toast.makeText(this,"Enter a valid Email Address",Toast.LENGTH_SHORT).show();
        }


        ///Password
        else if(Objects.equals(password1, ""))
        {
            password.setError("Please enter your password");
            password.requestFocus();
            //Toast.makeText(this,"Please enter your password",Toast.LENGTH_SHORT).show();
        }
        else if(password1.length()<6)
        {
            password.setError("Minimum password length is 6");
            password.requestFocus();
            //Toast.makeText(this, "Minimum password length is 6", Toast.LENGTH_SHORT).show();
        }

        ////New User SignUp
        else {
            progressDialog.setMessage("Logging In..");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(emailId1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if(task.isSuccessful())
                    {

                        Intent intent=new Intent(getApplicationContext(),mainActivityAfterSuccessfulLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(),"You are now Logged In",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                    }

                }
            });

        }

    }
    void dontHaveAnAccountClick(View v)
    {

        Log.i("Register","Register button pressed");
        startActivity(new Intent(this,SignUpActivity.class));
    }


}
