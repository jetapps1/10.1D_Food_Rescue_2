package com.example.a101dfoodrescue;

import android.net.Uri;

public class Card {
    private int id;
    private String title;
    private String desc;
    private Uri image;

    public Card(int id, Uri image, String title, String desc) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getImage() { return image; }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
