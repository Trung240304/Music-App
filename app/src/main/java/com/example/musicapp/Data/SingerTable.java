package com.example.musicapp.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.musicapp.Artist.Artist;
import java.util.ArrayList;

public class SingerTable {
    private static final String TABLE_NAME = "singers";
    private static final String COL_SINGER_NAME = "singer_name";
    private static final String COL_SINGER_IMAGE_URL = "singer_image_url";

    public static void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_SINGER_NAME + " TEXT PRIMARY KEY, " +
                COL_SINGER_IMAGE_URL + " TEXT)";
        db.execSQL(createTable);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static boolean insertSinger(SQLiteDatabase db, String name, String imageUrl) {
        if (singerExists(db, name)) {
            return false; // Nghệ sĩ đã tồn tại
        }

        ContentValues values = new ContentValues();
        values.put(COL_SINGER_NAME, name);
        values.put(COL_SINGER_IMAGE_URL, imageUrl);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public static boolean updateSinger(SQLiteDatabase db, String name, String imageUrl) {
        ContentValues values = new ContentValues();
        values.put(COL_SINGER_IMAGE_URL, imageUrl);
        int rowsUpdated = db.update(TABLE_NAME, values, COL_SINGER_NAME + "=?", new String[]{name});
        return rowsUpdated > 0;
    }

    public static ArrayList<Artist> getAllSingers(SQLiteDatabase db) {
        ArrayList<Artist> singers = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_SINGER_NAME));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COL_SINGER_IMAGE_URL));
                singers.add(new Artist(name, imageUrl));
            }
            cursor.close();
        }
        return singers;
    }

    public static boolean deleteSinger(SQLiteDatabase db, String name) {
        int rowsDeleted = db.delete(TABLE_NAME, COL_SINGER_NAME + "=?", new String[]{name});
        return rowsDeleted > 0;
    }

    private static boolean singerExists(SQLiteDatabase db, String name) {
        Cursor cursor = db.query(TABLE_NAME, null, COL_SINGER_NAME + "=?", new String[]{name}, null, null, null);
        boolean exists = (cursor != null && cursor.moveToFirst());
        if (cursor != null) cursor.close();
        return exists;
    }

    public static Artist getSingerByName(SQLiteDatabase db, String name) {
        Cursor cursor = db.query(TABLE_NAME, null, COL_SINGER_NAME + "=?", new String[]{name}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String singerName = cursor.getString(cursor.getColumnIndexOrThrow(COL_SINGER_NAME));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COL_SINGER_IMAGE_URL));
            cursor.close();
            return new Artist(singerName, imageUrl);
        }
        return null;
    }
}
