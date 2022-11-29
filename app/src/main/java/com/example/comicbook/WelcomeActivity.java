package com.example.comicbook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaParser;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Button login=(Button) findViewById(R.id.log_rediract_btn);
        Button skip=(Button)findViewById(R.id.skip_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(i);

            }
        });

//        skip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(WelcomeActivity.this,HomeActivity.class);
//                startActivity(i);
//
//            }
//        });
    }
}