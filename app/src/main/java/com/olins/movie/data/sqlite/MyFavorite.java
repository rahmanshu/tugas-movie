package com.olins.movie.data.sqlite;

public class MyFavorite {

    public static final String TABLE_NAME = "myFavorite";
    public static final String ITEM_COLUMN_ID = "id";
    public static final String ITEM_COLUMN_TITLE = "title";
    public static final String ITEM_COLUMN_YEAR = "year";
    public static final String ITEM_COLUMN_IMDBID = "imdbid";
    public static final String ITEM_COLUMN_TYPE = "type";
    public static final String ITEM_COLUMN_POSTER = "poster";
    public static final String ITEM_COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String title;
    private String year;
    private String imdbid;
    private String type;
    private String poster;
    private String timestamp;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + ITEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ITEM_COLUMN_TITLE + " TEXT,"
                    + ITEM_COLUMN_YEAR + " TEXT,"
                    + ITEM_COLUMN_IMDBID + " TEXT,"
                    + ITEM_COLUMN_TYPE + " TEXT,"
                    + ITEM_COLUMN_POSTER + " TEXT,"
                    + ITEM_COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public MyFavorite() {
    }

    public MyFavorite(int id,
            String title,
            String year,
            String imdbid,
            String type,
            String poster,
            String timestamp) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.imdbid = imdbid;
        this.type = type;
        this.poster = poster;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImdbid() {
        return imdbid;
    }

    public void setImdbid(String imdbid) {
        this.imdbid = imdbid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
