package com.olins.movie.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SqliteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "item_db";

    public SqliteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyFavorite.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + MyFavorite.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public long insertItem(String title, String year, String imdbid, String type, String poster) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(MyFavorite.ITEM_COLUMN_TITLE, title);
        values.put(MyFavorite.ITEM_COLUMN_YEAR, year);
        values.put(MyFavorite.ITEM_COLUMN_IMDBID, imdbid);
        values.put(MyFavorite.ITEM_COLUMN_TYPE, type);
        values.put(MyFavorite.ITEM_COLUMN_POSTER, poster);
        long id = db.insert(MyFavorite.TABLE_NAME, null, values);
        db.close();
        return id;
    }


    public List<MyFavorite> getAllItemProduct() {
        List<MyFavorite> itemProducts = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + MyFavorite.TABLE_NAME + " ORDER BY " +
                MyFavorite.ITEM_COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MyFavorite itemProduct = new MyFavorite();
                itemProduct.setId(cursor.getInt(0));
                itemProduct.setTitle(cursor.getString(1));
                itemProduct.setYear(cursor.getString(2));
                itemProduct.setImdbid(cursor.getString(3));
                itemProduct.setType(cursor.getString(4));
                itemProduct.setPoster(cursor.getString(5));
                itemProduct.setTimestamp(cursor.getString(6));
                itemProducts.add(itemProduct);
            } while (cursor.moveToNext());
        }
        db.close();
        return itemProducts;
    }


    public int getItemCount() {
        String countQuery = "SELECT  * FROM " + MyFavorite.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateItem(MyFavorite itemProduct) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MyFavorite.ITEM_COLUMN_TITLE, itemProduct.getTitle());

        // updating row
        return db.update(MyFavorite.TABLE_NAME, values, MyFavorite.ITEM_COLUMN_ID + " = ?",
                new String[]{String.valueOf(itemProduct.getId())});
    }


    public void deleteItem(int itemProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MyFavorite.TABLE_NAME, MyFavorite.ITEM_COLUMN_ID + " = ?",
                new String[]{String.valueOf(itemProduct)});
        db.close();
    }

    public MyFavorite getItemProduct(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(MyFavorite.TABLE_NAME,
                new String[]{MyFavorite.ITEM_COLUMN_ID, MyFavorite.ITEM_COLUMN_TITLE, MyFavorite.ITEM_COLUMN_YEAR,
                        MyFavorite.ITEM_COLUMN_IMDBID, MyFavorite.ITEM_COLUMN_TYPE, MyFavorite.ITEM_COLUMN_POSTER,
                        MyFavorite.ITEM_COLUMN_TIMESTAMP},
                MyFavorite.ITEM_COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        MyFavorite itemProduct = new MyFavorite(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));

        // close the db connection
        cursor.close();

        return itemProduct;
    }

}
