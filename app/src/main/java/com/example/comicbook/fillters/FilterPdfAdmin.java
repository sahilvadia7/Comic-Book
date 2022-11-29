package com.example.comicbook.fillters;

import android.widget.Filter;

import com.example.comicbook.adapters.AdapterCategory;
import com.example.comicbook.adapters.AdapterPdfAdmin;
import com.example.comicbook.models.ModelCategorys;
import com.example.comicbook.models.ModelPdf;

import java.util.ArrayList;

public class FilterPdfAdmin extends Filter {

    //arraylist which wie will search
    ArrayList<ModelPdf> filterlist;
    //adapter in which filter nedd to be implemented
    AdapterPdfAdmin adapterPdfAdmin;

    //constructor
    public FilterPdfAdmin(ArrayList<ModelPdf> filterlist, AdapterPdfAdmin adapterPdfAdmin) {
        this.filterlist = filterlist;
        this.adapterPdfAdmin = adapterPdfAdmin;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //value should not be nulland empty
        if(constraint!=null && constraint.length()>0){
            //change to upper case,or lower case to avoid case sensitivity
            constraint=constraint.toString().toUpperCase();
            ArrayList<ModelPdf> filterModels=new ArrayList<>();
            for(int i=0; i<filterlist.size();i++){
                //validate
                if(filterlist.get(i).getTitle().toUpperCase().contains(constraint)){
                    //add to filtered List
                    filterModels.add(filterlist.get(i));

                }
            }
        results.count=filterModels.size();
            results.values=filterModels;

        }
        else {
            results.count=filterlist.size();
            results.values=filterlist;
        }

        return results;//dons't miss it
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
    //apply filter
        adapterPdfAdmin.pdfArrayList=(ArrayList<ModelPdf>) results.values;

        //modify changes
        adapterPdfAdmin.notifyDataSetChanged();
    }
}
