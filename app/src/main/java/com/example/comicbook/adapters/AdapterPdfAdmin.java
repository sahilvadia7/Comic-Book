package com.example.comicbook.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicbook.MyApplication;
import com.example.comicbook.PdfDetailActivity;
import com.example.comicbook.PdfEditActivity;
import com.example.comicbook.R;
import com.example.comicbook.fillters.FilterPdfAdmin;
import com.example.comicbook.models.ModelPdf;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;

public class AdapterPdfAdmin extends RecyclerView.Adapter<AdapterPdfAdmin.HolderPdfAdmin> implements Filterable {

    //context
    private Context context;

    //arraylist to hold list of data of type modelPdf
    public ArrayList<ModelPdf> pdfArrayList, filterList;

    private FilterPdfAdmin filter;

    private ProgressDialog progressDialog;

    private static final String TAG = "PDF_ADAPTER_TAG";

    //constructor
    public AdapterPdfAdmin(Context context, ArrayList<ModelPdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
        this.filterList = pdfArrayList;

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("please wait..");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @NonNull
    @Override
    public HolderPdfAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        using view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pdf_admin, parent, false);
        return new HolderPdfAdmin(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPdfAdmin holder, int position) {
        //get data / set data / handle click etc..

        //get data
        ModelPdf model = pdfArrayList.get(position);
        String title = model.getTitle();
        String pdfId= model.getId();
        String category = model.getCategory();
        String pdfUrl= model.getUrl();
        String description = model.getDescription();
        long timestamp = model.getTimestamp();

        //we need convert timestamp to dd/mm/yyyy  format
        //calling Myapplication class for getting date
        String formattedDate = MyApplication.formatTimestamp(timestamp);

        //set data
        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.dateTv.setText(formattedDate);


        //load further details like category, pdf from uri, pdf size in separate function
        MyApplication.loadCategory(
                ""+category,
                  holder.categoryTv);

        MyApplication.loadPdfFromUrl(
                ""+pdfUrl,
                ""+title,
                holder.pdfView,
                holder.progressBar);

        MyApplication.loadPdfSize(
                ""+pdfUrl,
                ""+title,
                holder.sizeTv

        );

        //handle click, show dialog with option 1) Edit, 2) Delete
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PdfDetailActivity.class);
                intent.putExtra("bookId",pdfId);
                context.startActivity(intent);
            }
        });

        //handle click,show dialog with options
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.moreOptionsDialog(model, holder);
            }
        });

    }






    @Override
    public int getItemCount() {
        return pdfArrayList.size();}//return number of records / list size


    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterPdfAdmin(filterList, this);
        }

        return filter;
    }

    //View Holder class for row_pdf_admin.xml
    class HolderPdfAdmin extends RecyclerView.ViewHolder {

        //ui views of raw_pdf_admin.xml
        PDFView pdfView;
        ProgressBar progressBar;
        TextView titleTv, descriptionTv, categoryTv, sizeTv, dateTv;
        ImageButton moreBtn;


        public HolderPdfAdmin(@NonNull View itemView) {
            super(itemView);

            //init ui views
            pdfView = itemView.findViewById(R.id.pdfView);
            progressBar = itemView.findViewById(R.id.progressBar);
            titleTv = itemView.findViewById(R.id.titleTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            categoryTv = itemView.findViewById(R.id.categoryTv);
            sizeTv = itemView.findViewById(R.id.sizeTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            moreBtn = itemView.findViewById(R.id.moreBtn);


        }


        public void moreOptionsDialog(ModelPdf model, HolderPdfAdmin holder) {

            String bookId = model.getId();
            String bookUrl = model.getUrl();
            String bookTitle = model.getTitle();

            //options to show in dialog box
            String[] options = {"Edit", "Delete"};

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("choose options")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //handle dialog option click
                            if (which==0) {
                                //Edit clicked,open new activity to edit the book info
                                Intent i = new Intent(context, PdfEditActivity.class);
                                i.putExtra("bookId", bookId);
                                context.startActivity(i);
                            } else if (which==1) {
                                //deleted click
                                MyApplication.deleteBook(context ,
                                        ""+bookId,
                                        ""+bookUrl,
                                        ""+bookTitle
                                );//create method

                            }

                        }
                    })
                    .show();
        }




    }

}


