package com.example.comicbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    //    view elements
    Button registration;
    EditText uname;
    EditText uemail;
    EditText upass;
    EditText urepass;
    ImageButton backActivity;

    //    firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();


        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait");
        progressDialog.setCanceledOnTouchOutside(false);


        //Get_userdata
        uname=(EditText)findViewById(R.id.reg_user);
        uemail=(EditText)findViewById(R.id.reg_email);
        upass=(EditText)findViewById(R.id.reg_pass);
        urepass=(EditText)findViewById(R.id.reg_repass);

        //btn obj
        registration= (Button) findViewById(R.id.reg_btn);
        backActivity=(ImageButton)findViewById(R.id.backActivity);

        //handle click of backActivity
        backActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateData();
            }
        });

    }

    private String name = "", email = "", pass = "";
    private void validateData() {

        //get data
        name =uname.getText().toString().trim();
        email = uemail.getText().toString().trim();
        pass = upass.getText().toString().trim();
        String repass =urepass.getText().toString().trim();

//        dummy data
//        name="sahil";
//        email="sahilvadia7776@gmail.com";
//        pass="123456";
//        String repass="123456";
//


        //validation
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "please enter name", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email pattern"+email, Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pass) || TextUtils.isEmpty(repass)){
            Toast.makeText(this, "Password is not empty", Toast.LENGTH_SHORT).show();
        }
        else if(!(pass.length() >=6)){
            Toast.makeText(this, "length at least 6", Toast.LENGTH_SHORT).show();
        }
        else if(!pass.equals(repass)){
            Toast.makeText(this, "password doesn't match", Toast.LENGTH_SHORT).show();
        }
        else{
            createUserAccount();
        }

    }

    private void createUserAccount() {
        //show progress
        progressDialog.setMessage("Creating account");
        progressDialog.show();

        //create user in firebase auth
        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        //account creation success, add in firebase realtime database
                        updateUserInfo();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //account creating failed
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });
    }

    private void updateUserInfo() {
        progressDialog.setMessage("saving user info");

        //timestamp
        long ts = System.currentTimeMillis();

        //get current user uid, since user is register so we can get now
        String uid= firebaseAuth.getUid();

        //setup data to add in db
        HashMap<String, Object> hashMap= new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("email",email);
        hashMap.put("name",name);
        hashMap.put("ProfileImage","");
        hashMap.put("UserType","user");//possible values are user, admin :will make manually in firebase realtime db by changing thisvalue
        hashMap.put("timestamp",ts);


        //set data in db
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        //data added to db
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationActivity.this,UserDashboardActivity.class));
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //data failed to add in db
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "something wrong", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}