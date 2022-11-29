package com.example.comicbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PdfEditActivity extends AppCompatActivity {

    //
    //book id get from intent start from Adapterpdfadmin
    private String bookId;

    //progress dialog
    private ProgressDialog progressDialog;

    private ArrayList<String> categoryTitleArrayList,categoryIdArrayList;

    private static  final String TAG="BOOK_EDIT_TAG";

    ImageButton backBtn;
    TextView categoryTv;
    Button  submitBtn;
    EditText titleEt,descEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_edit);
        Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
        backBtn=findViewById(R.id.backBtn);
        categoryTv=findViewById(R.id.categoryTv);
        submitBtn=findViewById(R.id.submitbtn);
        titleEt=findViewById(R.id.titleEt);
        descEt=findViewById(R.id.descEt);

        //book id get from intent start from Adapterpdfadmin
        bookId=getIntent().getStringExtra("bookId");

        //setup progress dialog
        progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);


        loadCategories();
        loadBookInfo();

        //handle click,pick category
        categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryDialog();
            }
        });


        //handle click,go to previous screen
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //handle click being upload
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void loadBookInfo() {
        Log.d(TAG,"loadBookInfo : Loading Books Info");

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Books");
        reference.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //get book info
                        selectedCategoryId = ""+snapshot.child("category").getValue();
                        String description = ""+snapshot.child("description").getValue();
                        String title = ""+snapshot.child("title").getValue();

                        //set to views
                        titleEt.setText(title);
                        descEt.setText(description);

                        Log.d(TAG,"onDataChange: Loading category Book Info ");
                        DatabaseReference refBookCategory=FirebaseDatabase.getInstance().getReference("Categories");
                        refBookCategory.child(selectedCategoryId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //get category
                                        String category=""+snapshot.child("category").getValue();

                                        categoryTv.setText(category);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private String title="",description="";
    private void validateData(){
        title=titleEt.getText().toString().trim();
        description=descEt.getText().toString().trim();

        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "Enter title...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "Enter description...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(selectedCategoryId)){
            Toast.makeText(this, "Pick Category", Toast.LENGTH_SHORT).show();
        }
        else{
            updatePdf();
        }


    }

    private void updatePdf() {

        Log.d(TAG,"updatePdf: starting updating pdf info....");
        //show progress
        progressDialog.setMessage("updating book info....");
        progressDialog.show();

        //setup database to update
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("title",""+title);
        hashMap.put("description",""+description);
        hashMap.put("category",""+selectedCategoryId);


        //start updating
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"onSuccess : Book updated ");
                        progressDialog.dismiss();
                        Toast.makeText(PdfEditActivity.this, "Book info updated....", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onFailure: failed to update due to"+e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(PdfEditActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private String selectedCategoryId="",selectedCategoryTitle="";

    private void categoryDialog(){
        //make string array from arraylist of string
        String[] categoriesArray=new String[categoryTitleArrayList.size()];
        for(int i=0; i<categoryTitleArrayList.size();i++){
            categoriesArray[i]=categoryTitleArrayList.get(i);
        }

        //Alert dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("choose Category")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        selectedCategoryId=categoryIdArrayList.get(which);
                        selectedCategoryTitle=categoryTitleArrayList.get(which);
                        //set a textview
                        categoryTv.setText(selectedCategoryTitle);

                    }
                })
                .show();
    }


    private void loadCategories() {
        Log.d(TAG,"loadCategories: Loading categories...");
        categoryIdArrayList=new ArrayList<>();
        categoryTitleArrayList=new ArrayList<>();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Categories");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryIdArrayList.clear();
                categoryTitleArrayList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    String id=""+ds.child("id").getValue();
                    String category=""+ds.child("category").getValue();
                    categoryIdArrayList.add(id);
                    categoryTitleArrayList.add(category);

                    Log.d(TAG,"onDataChange: ID: "+id);
                    Log.d(TAG,"onDataChange: Category: "+category);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}