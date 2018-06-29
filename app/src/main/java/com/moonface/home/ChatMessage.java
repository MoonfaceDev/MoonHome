package com.moonface.home;

import android.graphics.Bitmap;

public class ChatMessage {

    private String key;

    private String name;
    private String time;
    private String message;
    private boolean admin;
    private String imageUrl;
    private Bitmap image;
    private String uid;

    ChatMessage(){

    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setImage(Bitmap bitmap){
        this.image = bitmap;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public boolean isAdmin() {
        return admin;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getUid() {
        return uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
