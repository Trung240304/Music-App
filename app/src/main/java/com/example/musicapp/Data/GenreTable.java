package com.example.musicapp.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.musicapp.Category.Category;
import java.util.ArrayList;

public class GenreTable {
    private static final String TABLE_NAME = "genres";
    private static final String COL_GENRE_NAME = "genre_name";
    private static final String COL_GENRE_IMAGE_URL = "genre_image_url";

    public static void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_GENRE_NAME + " TEXT PRIMARY KEY, " +
                COL_GENRE_IMAGE_URL + " TEXT)";
        db.execSQL(createTable);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static boolean insertGenre(SQLiteDatabase db, String name, String imageUrl) {
        if (genreExists(db, name)) {
            return false; // Thể loại đã tồn tại
        }

        ContentValues values = new ContentValues();
        values.put(COL_GENRE_NAME, name);
        values.put(COL_GENRE_IMAGE_URL, imageUrl);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public static boolean updateGenre(SQLiteDatabase db, String oldName, String newName, String imageUrl) {
        ContentValues values = new ContentValues();
        values.put(COL_GENRE_NAME, newName);
        values.put(COL_GENRE_IMAGE_URL, imageUrl);
        int rowsUpdated = db.update(TABLE_NAME, values, COL_GENRE_NAME + "=?", new String[]{oldName});
        return rowsUpdated > 0;
    }

    public static ArrayList<Category> getAllGenres(SQLiteDatabase db) {
        ArrayList<Category> genres = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_GENRE_NAME));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COL_GENRE_IMAGE_URL));
                genres.add(new Category(name, imageUrl));
            }
            cursor.close();
        }
        return genres;
    }

    public static boolean deleteGenre(SQLiteDatabase db, String name) {
        int rowsDeleted = db.delete(TABLE_NAME, COL_GENRE_NAME + "=?", new String[]{name});
        return rowsDeleted > 0;
    }

    private static boolean genreExists(SQLiteDatabase db, String name) {
        Cursor cursor = db.query(TABLE_NAME, null, COL_GENRE_NAME + "=?", new String[]{name}, null, null, null);
        boolean exists = (cursor != null && cursor.moveToFirst());
        if (cursor != null) cursor.close();
        return exists;
    }

    // Thêm phương thức để lấy thể loại theo tên
    public static Category getGenreByName(SQLiteDatabase db, String name) {
        Cursor cursor = db.query(TABLE_NAME, null, COL_GENRE_NAME + "=?", new String[]{name}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String genreName = cursor.getString(cursor.getColumnIndexOrThrow(COL_GENRE_NAME));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COL_GENRE_IMAGE_URL));
            cursor.close();
            return new Category(genreName, imageUrl);
        }
        return null;
    }
}
