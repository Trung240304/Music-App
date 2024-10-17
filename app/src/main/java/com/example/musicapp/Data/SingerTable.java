package com.example.musicapp.Data;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SingerTable {
    private static final String TABLE_NAME = "singers";
    private static final String COL_SINGER_NAME = "singer_name";
    private static final String COL_SINGER_IMAGE = "singer_image";
    private static final String COL_SINGER_BIOGRAPHY = "singer_biography";

    public static void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_SINGER_NAME + " TEXT PRIMARY KEY, "
                + COL_SINGER_IMAGE + " BLOB, "
                + COL_SINGER_BIOGRAPHY + " TEXT)";
        db.execSQL(createTable);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static boolean insertSinger(SQLiteDatabase db, String name, byte[] image, String biography) {
        ContentValues values = new ContentValues();
        values.put(COL_SINGER_NAME, name);
        values.put(COL_SINGER_IMAGE, image);
        values.put(COL_SINGER_BIOGRAPHY, biography);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }
}
