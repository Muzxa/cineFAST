package com.example.cinefast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseManager {
    private static final String DATABASE_NAME = "cineFASTDB";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_SNACKS = "snacks";
    private static final String COLUMN_SNACK_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_DESCRIPTION = "description";

    Context context;
    DBHelper helper;

    public DatabaseManager(Context context){
        this.context = context;
    }

    public void open() {
        helper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void close() {
        helper.close();
    }

    public long insertSnack(Snack snack){
        if(helper == null) {
            this.open();
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, snack.getName());
        cv.put(COLUMN_PRICE, snack.getPrice());
        cv.put(COLUMN_IMAGE, snack.getImageId());
        cv.put(COLUMN_DESCRIPTION, snack.getDescription());
        long id = db.insert(TABLE_SNACKS, null, cv);
        db.close();
        return id;
    }

    public ArrayList<Snack> getAllSnacks() {

        if(helper == null) {
            this.open();
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SNACKS, null, null, null, null, null, null);

        ArrayList<Snack> snacks = new ArrayList<>();

        if(cursor.moveToFirst()) {

            do {
                int index_name = cursor.getColumnIndex(COLUMN_NAME);
                int index_snack_id = cursor.getColumnIndex(COLUMN_SNACK_ID);
                int index_price = cursor.getColumnIndex(COLUMN_PRICE);
                int index_image = cursor.getColumnIndex(COLUMN_IMAGE);
                int index_description = cursor.getColumnIndex(COLUMN_DESCRIPTION);

                String name = cursor.getString(index_name);
                double price = cursor.getDouble(index_price);
                int image = cursor.getInt(index_image);
                String description = cursor.getString(index_description);
                snacks.add(new Snack(name, description, price, 0, image));
            } while (cursor.moveToNext());
        }

        return snacks;
    }

    private class DBHelper extends SQLiteOpenHelper
    {
        public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createQuery = "CREATE TABLE " + TABLE_SNACKS + "("
                                +COLUMN_SNACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                +COLUMN_NAME + " TEXT,"
                                +COLUMN_PRICE + " REAL,"
                                +COLUMN_IMAGE + " INTEGER,"
                                +COLUMN_DESCRIPTION + " TEXT)";
            try {
                db.execSQL(createQuery);
            } catch (SQLException e) {
                Log.e("CineFAST Database", "Error occurred while creating database");
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNACKS);
            onCreate(db);
        }
    }
}
