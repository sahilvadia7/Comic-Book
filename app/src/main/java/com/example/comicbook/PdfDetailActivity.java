package com.example.comicbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PdfDetailActivity extends AppCompatActivity {


    //pdf id, get from intent
    String bookId;
    TextView titleTv,descriptionTv,viewTv,downloadsTv,dateTv;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_detail);

        //get data from intent e.g bookId
        Intent intent=getIntent();
        bookId = intent.getStringExtra("bookId");
//        Toast.makeText(this, ""+bookId, Toast.LENGTH_SHORT).show();
        
        loadBookDetails();

        //increment book view whenever this page starts
        MyApplication.incrementBookViewCount(bookId);

        //handle click go back
        backBtn=findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void loadBookDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
        reference.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get data
                        String title = ""+snapshot.child("title").getValue();
                        String description = ""+snapshot.child("description").getValue();
                        String category = ""+snapshot.child("category").getValue();
                        String viewCount = ""+snapshot.child("viewCount").getValue();
                        String downloadCount = ""+snapshot.child("downloadCount").getValue();
                        String url = ""+snapshot.child("url").getValue();
                        String timestamp = ""+snapshot.child("timestamp").getValue();


                        //format date
                        String date = MyApplication.formatTimestamp(Long.parseLong(timestamp));
                        MyApplication.loadCategory(
                                ""+category,
                                findViewById(R.id.categoryTv)
                        );
                        MyApplication.loadPdfFromUrl(
                                ""+url,
                                ""+title,
                                findViewById(R.id.pdfView),
                                findViewById(R.id.progressBar)
                        );
                        MyApplication.loadPdfSize(
                                ""+url,
                                ""+title,
                                findViewById(R.id.sizeTv)
                        );

                        //set data
                        titleTv=findViewById(R.id.titleTv);
                        descriptionTv=findViewById(R.id.descriptionTv);
                        viewTv=findViewById(R.id.viewTv);
                        downloadsTv=findViewById(R.id.downloadTv);
                        dateTv=findViewById(R.id.dateTv);

                        titleTv.setText(title);
                        descriptionTv.setText(description);
                        viewTv.setText(viewCount.replace("null","N/A"));
                        downloadsTv.setText(downloadCount.replace("null","N/A"));
                        dateTv.setText(date);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}