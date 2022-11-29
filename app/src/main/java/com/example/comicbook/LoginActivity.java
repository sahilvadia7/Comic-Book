package com.example.comicbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    //view element object
    Button login;
    TextView registration;
    EditText uemail,upass;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog object
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //setup progress dialog
        progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);


        //object
        login=(Button) findViewById(R.id.login_btn);
        registration=(TextView) findViewById(R.id.reg_redirect_btn);
        uemail=(EditText)findViewById(R.id.email);
        upass=(EditText)findViewById(R.id.pass);


        //handle click, go to home screen
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               validateData();
            }
        });

        //handle click, go to registration screen
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(i);
            }
        });
    }

    private String email="",pass="";
    private void validateData() {

        //get data
        email = uemail.getText().toString().trim();
        pass = upass.getText().toString().trim();

        //validate data
        if(email.equals(null)){
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
        }
        else if(pass.equals(null)){
            Toast.makeText(this, "please enter pass", Toast.LENGTH_SHORT).show();
        }
        else{
            loginUser();
        }
    }

    private void loginUser() {
    progressDialog.setMessage("Logging In");
    progressDialog.show();


    firebaseAuth.signInWithEmailAndPassword(email,pass)
            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                //login success, check if user is user or admin
                    checkUser();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "failed to login", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void checkUser() {

        progressDialog.setMessage("verifying user");

        //check if user or admin from realtime database
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //check in database
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");
        ref.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        progressDialog.dismiss();

                        //get user type
                        String userType = ""+ snapshot.child("UserType").getValue();

                        //check user type
                        if(userType.equals("user")){

                            //this is simple user, open user dashboard
                            startActivity(new Intent(LoginActivity.this,UserDashboardActivity.class));
                            finish();
                        }
                        else if(userType.equals("admin")){

                            //this is admin, open admin dashboard
                            startActivity(new Intent(LoginActivity.this,AdminActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}