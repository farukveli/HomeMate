package com.example.homemate;


public class RCModel {

    String title;
    String image;

    String uid;

    public RCModel(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public RCModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}