package com.example.musicapp.Data;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class GenreTable {
    private static final String TABLE_NAME = "genres";
    private static final String COL_GENRE_NAME = "genre_name";
    private static final String COL_GENRE_IMAGE = "genre_image";

    public static void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_GENRE_NAME + " TEXT PRIMARY KEY, "
                + COL_GENRE_IMAGE + " BLOB)";
        db.execSQL(createTable);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static boolean insertGenre(SQLiteDatabase db, String name, byte[] image) {
        ContentValues values = new ContentValues();
        values.put(COL_GENRE_NAME, name);
        values.put(COL_GENRE_IMAGE, image);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }
}
