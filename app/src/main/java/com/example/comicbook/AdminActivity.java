package com.example.comicbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.comicbook.adapters.AdapterCategory;
import com.example.comicbook.models.ModelCategorys;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    TextView admin_display_name;
    ImageView logout;
    Button add_cat;
    ImageView add_pdf;
    RecyclerView crv;
    EditText search;

    private FirebaseAuth firebaseAuth;
    //arraylist to store catefories
    private ArrayList<ModelCategorys> categorysArrayList;
    //adapter
    private AdapterCategory adapterCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //hide app name on top
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        //getting data
        admin_display_name=(TextView)findViewById(R.id.admin_display);
        logout=(ImageView)findViewById(R.id.logout_btn);
        add_cat = findViewById(R.id.add_category_btn);
        add_pdf=findViewById(R.id.add_pdf_btn);
        crv=findViewById(R.id.categoriesRV);
        search=findViewById(R.id.searchEt);



        //btn click handle
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });


        add_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,CategoryAddActivity.class));
            }
        });

        add_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AdminActivity.this,pdfAdd.class));
            }
        });

                    search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            try{
                                adapterCategory.getFilter().filter(s);
                            }catch (Exception e){
                                System.out.println("Exception in search :"+e);
                            }
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //called as and when user type each letter
                            try{
                                adapterCategory.getFilter().filter(s);
                            }
                            catch (Exception e){

                            }

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });




        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
       loadCategories();


        //handle click,start category add screen

    }

    private void loadCategories() {
        //init arraylist
        categorysArrayList=new ArrayList<>();

        //get all categories from firebase categories
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               //clear arraylist before adding data into it
               categorysArrayList.clear();
               for(DataSnapshot ds:snapshot.getChildren()){
                   //get data#
                   ModelCategorys model=ds.getValue(ModelCategorys.class);
                   //add to arraylist
                   categorysArrayList.add(model);

               }
               //set up adapter
                adapterCategory =new AdapterCategory(AdminActivity.this,categorysArrayList);
                  //set adapter to recycleview
                crv.setAdapter(adapterCategory);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUser() {

        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser==null){
            //goto main screen
            startActivity(new Intent(AdminActivity.this,WelcomeActivity.class));
            finish();
        }
        else{
            //logged in, got get user info
            String email = firebaseUser.getEmail();

            //set in textview of toolbar
            admin_display_name.setText(email);

        }
    }
}