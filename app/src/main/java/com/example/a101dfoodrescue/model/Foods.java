package com.example.a101dfoodrescue.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Foods implements Parcelable {
    private int food_id;
    private String image;
    private String title;
    private String desc;
    private String user;
    private String cart;
    private String price;

    public Foods(String image, String title, String desc, String user, String cart, String price){
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.user = user;
        this.cart = cart;
        this.price = price;
    }

    public Foods(){}

    protected Foods(Parcel in) {
        food_id = in.readInt();
        image = in.readString();
        title = in.readString();
        desc = in.readString();
        user = in.readString();
        cart = in.readString();
        price = in.readString();
    }

    public static final Creator<Foods> CREATOR = new Creator<Foods>() {
        @Override
        public Foods createFromParcel(Parcel in) {
            return new Foods(in);
        }

        @Override
        public Foods[] newArray(int size) {
            return new Foods[size];
        }
    };

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFoodImage() {
        return image;
    }

    public void setImage(String image) {
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

    public String getUser(){ return this.user; }
    public void setUser(String user){this.user = user; }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(food_id);
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(user);
        dest.writeString(cart);
        dest.writeString(price);
    }
}
