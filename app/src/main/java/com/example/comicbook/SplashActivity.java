package com.example.comicbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        //firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //start main screen after 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        },2000);

    }



    private void checkUser() {

        //get current user, if logged in
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            //if on login and start app newly it goes welcomeActivity
            startActivity(new Intent(SplashActivity.this,WelcomeActivity.class));
            finish();
        }
        else{
            //user logged in check user type

            //check in database
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");
            ref.child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            //get user type
                            String userType = ""+ snapshot.child("UserType").getValue();

                            //check user type
                            if(userType.equals("user")){

                                //this is simple user, open user dashboard
                                startActivity(new Intent(SplashActivity.this,UserDashboardActivity.class));
                                finish();
                            }
                            else if(userType.equals("admin")){

                                //this is admin, open admin dashboard
                                startActivity(new Intent(SplashActivity.this,AdminActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}