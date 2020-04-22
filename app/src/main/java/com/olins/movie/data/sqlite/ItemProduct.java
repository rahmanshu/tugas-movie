package com.olins.movie.data.sqlite;

public class ItemProduct {

    public static final String TABLE_NAME = "item";
    public static final String ITEM_COLUMN_ID = "id";
    public static final String ITEM_COLUMN_NAME = "name";
    public static final String ITEM_COLUMN_DESC = "desc";
    public static final String ITEM_COLUMN_PRICE = "price";
    public static final String ITEM_COLUMN_IMAGE = "image";
    public static final String ITEM_COLUMN_IMAGE_PATH = "imagepath";
    public static final String ITEM_COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String name;
    private String desc;
    private String price;
    private byte[] image;
    private String imagepath;
    private String timestamp;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + ITEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ITEM_COLUMN_NAME + " TEXT,"
                    + ITEM_COLUMN_DESC + " TEXT,"
                    + ITEM_COLUMN_PRICE + " TEXT,"
                    + ITEM_COLUMN_IMAGE + " BLOB,"
                    + ITEM_COLUMN_IMAGE_PATH + " TEXT,"
                    + ITEM_COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public ItemProduct() {
    }

    public ItemProduct(int id, String name, String desc, String price, byte[] image, String imagepath, String timestamp) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.image = image;
        this.imagepath = imagepath;
        this.timestamp = timestamp;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
