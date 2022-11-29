package com.example.comicbook.models;

public class ModelPdf {

    //variable
    String uid,id,title,description,category,url;
    long timestamp,viewCount,downloadCount;

    //empty constructor, for firebase
    public ModelPdf() {

    }

    //constructor with parameter

    public ModelPdf(String uid, String id, String title, String description, String category, String url, long timestamp, long viewCount, long downloadCount) {
        this.uid = uid;
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.url = url;
        this.timestamp = timestamp;
        this.viewCount = viewCount;
        this.downloadCount = downloadCount;
    }


    //Getter - Setters

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(long downloadCount) {
        this.downloadCount = downloadCount;
    }
}
