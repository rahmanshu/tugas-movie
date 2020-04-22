package com.olins.movie.data.api.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemList implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("itemName")
    @Expose
    private String itemName;

    @SerializedName("itemDesc")
    @Expose
    private String itemDesc;

    @SerializedName("picture")
    @Expose
    private String picture;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("imagepath")
    @Expose
    private String imagepath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}
