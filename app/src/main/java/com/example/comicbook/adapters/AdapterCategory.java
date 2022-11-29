package com.example.comicbook.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicbook.PdfListAdminActivity;
import com.example.comicbook.fillters.Filtercategory;
import com.example.comicbook.models.ModelCategorys;
import com.example.comicbook.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;



public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.HolderCategory>implements Filterable {
    TextView categoryTv;
    ImageButton deleteBtn;

private Context context;
public ArrayList<ModelCategorys> categoryArrayList,filterList;

//instance of our filter class
    private Filtercategory filter;

    //view b
    public AdapterCategory(Context context, ArrayList<ModelCategorys> categorysArrayList) {
        this.context = context;
        this.categoryArrayList = categorysArrayList;
        this.filterList=categorysArrayList;

    }

    @NonNull
    @Override

    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category,parent,false);
        return new HolderCategory(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, int position) {
        //get data
    ModelCategorys model= categoryArrayList.get(position);
    String id= model.getId();

    //testing
    String category=model.getCategory();
    String uid=model.getUid();
    long timestamp=model.getTimestamp();

    //set data
    holder.categoryTv.setText(category);

    //handle click, delete category
    holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        //confirm delete dialog
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("Delete")
                    .setMessage("Are you sure you want to delete this category ?'")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                //begin delete
                            Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show();
                            deleteCategory(model,holder);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .show();
        }
    });

    //handle click , goto PdfListAdminActivity, also pass pdfCategory and categoryId
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, PdfListAdminActivity.class);
                intent.putExtra("category",id);
                intent.putExtra("categoryTitle",category);
                context.startActivity(intent);
            }
        });

  }

    private void deleteCategory(ModelCategorys model, HolderCategory holder) {
        //get id of category to delete
        //Firebase DB> Categories > categoryId
        String id= model.getId();
        DatabaseReference  ref= FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                   //deleted successfully
                        Toast.makeText(context, "Successfully deleted....", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed to delete
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    @Override
    public Filter getFilter() {

        if(filter==null){
            filter=new Filtercategory(filterList,this);
        }
        return filter;
    }

    //view holder class to hold UI views for row_category.xml
    class HolderCategory extends RecyclerView.ViewHolder{

        //ui view of row category
    TextView categoryTv;
    ImageButton deleteBtn;

        public HolderCategory(@NonNull View itemview){
            super(itemview);

            //init ui views
            categoryTv = itemView.findViewById(R.id.categoryTv);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);


        }

    }

}
