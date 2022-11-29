package com.example.comicbook.models;

public class ModelCategorys {

//make sure ton use some spellings for mode;

    String id,category,uid;
    long timestamp;




    //constructor empty required for firebase
     public ModelCategorys(){

     }

     //parameterized constructor
     public ModelCategorys(String id, String category, String uid, long timestamp) {
         this.id = id;
         this.category = category;
         this.uid = uid;
         this.timestamp = timestamp;
     }

     //Getter...setter//

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
