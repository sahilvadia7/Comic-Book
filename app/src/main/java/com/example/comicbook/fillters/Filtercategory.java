package com.example.comicbook.fillters;

import android.widget.Filter;

import com.example.comicbook.adapters.AdapterCategory;
import com.example.comicbook.models.ModelCategorys;

import java.util.ArrayList;

public class Filtercategory extends Filter {

    //arraylist which wie will search
    ArrayList<ModelCategorys> filterlist;
    //adapter in which filter nedd to be implemented
    AdapterCategory adapterCategory;

    //constructor
    public Filtercategory(ArrayList<ModelCategorys> filterlist, AdapterCategory adapterCategory) {
        this.filterlist = filterlist;
        this.adapterCategory = adapterCategory;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //value should not be nulland empty
        if(constraint!=null && constraint.length()>0){
            //change to upper case,or lower case to avoid case sensitivity
            constraint=constraint.toString().toUpperCase();
            ArrayList<ModelCategorys> filterModels=new ArrayList<>();
            for(int i=0; i<filterlist.size();i++){
                //validate
                if(filterlist.get(i).getCategory().toUpperCase().contains(constraint)){
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
        adapterCategory.categoryArrayList =(ArrayList<ModelCategorys>) results.values;

        //modify changes
        adapterCategory.notifyDataSetChanged();
    }
}
