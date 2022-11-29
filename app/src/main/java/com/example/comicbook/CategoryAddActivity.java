package com.example.comicbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CategoryAddActivity extends AppCompatActivity {

        //view binding

    private FirebaseAuth  firebaseAuth;
    Button submitbtn;
    ImageView backToAdmin;
    EditText EditText_Of_AddCategory;

    //progress dialog
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_add);
        submitbtn=(findViewById(R.id.submitBtn));
        EditText_Of_AddCategory=(findViewById(R.id.EditText_Of_AddCategory));
        backToAdmin=(findViewById(R.id.backToAdmin));
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //handle click go back
        backToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //handle click,being upload category
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }
    private String category= "";
    private void validateData(){
        //get data
       category=EditText_Of_AddCategory.getText().toString().trim();
       //valiadate if not empty
        if(TextUtils.isEmpty(category)){
            Toast.makeText(this, "please enter category", Toast.LENGTH_SHORT).show();
        }
        else{
            addCategoryFirebase();
        }
    }
    private void addCategoryFirebase(){
        progressDialog.setMessage("Adding Category....");
        progressDialog.show();

        //getTime Stamp
        long timestamp=System.currentTimeMillis();

        //setup info to add in firebase db
        HashMap<String,Object> hashmap=new HashMap<>();
        hashmap.put("id",""+timestamp);
        hashmap.put("category",""+category);
        hashmap.put("timestamp",timestamp);
        hashmap.put("uid",""+firebaseAuth.getUid());

        //add to firebase db...fatabase root
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(""+timestamp)
                .setValue(hashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this, "category added successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CategoryAddActivity.this,AdminActivity.class));
                        finish();
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(CategoryAddActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        }
                
    }
