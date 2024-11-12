package com.example.musicapp.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicapp.Song.Song;

import java.util.ArrayList;
import java.util.List;

public class SongTable {
    private static final String TABLE_NAME = "songs";
    private static final String COL_SONG_NAME = "song_name";
    private static final String COL_SONG_SINGER = "song_singer";
    private static final String COL_SONG_GENRE = "song_genre";
    private static final String COL_SONG_URL = "song_url";
    private static final String COL_SONG_IMAGE = "song_image";

    public static void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_SONG_NAME + " TEXT PRIMARY KEY, " +
                COL_SONG_SINGER + " TEXT, " +
                COL_SONG_GENRE + " TEXT, " +
                COL_SONG_URL + " TEXT, " +
                COL_SONG_IMAGE + " TEXT, " +
                "FOREIGN KEY(" + COL_SONG_SINGER + ") REFERENCES singers(singer_name), " +
                "FOREIGN KEY(" + COL_SONG_GENRE + ") REFERENCES genres(genre_name))";
        db.execSQL(createTable);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static boolean createSong(SQLiteDatabase db, String name, String singer, String genre, String url, String image) {
        ContentValues values = new ContentValues();
        values.put(COL_SONG_NAME, name);
        values.put(COL_SONG_SINGER, singer);
        values.put(COL_SONG_GENRE, genre);
        values.put(COL_SONG_URL, url);
        values.put(COL_SONG_IMAGE, image);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1; // Trả về true nếu insert thành công
    }

    public static Song getSongByName(SQLiteDatabase db, String songName) {
        Song song = null;
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_SONG_NAME + " = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{songName});
            if (cursor.moveToFirst()) {
                String singer = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_SINGER));
                String genre = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_GENRE));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_URL));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_IMAGE));
                song = new Song(songName, singer, genre, image, url);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return song; // Trả về null nếu không tìm thấy bài hát
    }

    public static boolean updateSong(SQLiteDatabase db, String originalName, String newName, String singer, String genre, String url, String image) {
        ContentValues values = new ContentValues();
        values.put(COL_SONG_NAME, newName);
        values.put(COL_SONG_SINGER, singer);
        values.put(COL_SONG_GENRE, genre);
        values.put(COL_SONG_URL, url);
        values.put(COL_SONG_IMAGE, image);
        int rowsUpdated = db.update(TABLE_NAME, values, COL_SONG_NAME + " = ?", new String[]{originalName});
        return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
    }

    public static boolean deleteSong(SQLiteDatabase db, String songName) {
        int rowsDeleted = db.delete(TABLE_NAME, COL_SONG_NAME + " = ?", new String[]{songName});
        return rowsDeleted > 0; // Trả về true nếu xóa thành công
    }

    public static List<Song> getAllSongs(SQLiteDatabase db) {
        List<Song> songList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_NAME));
                    String singer = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_SINGER));
                    String genre = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_GENRE));
                    String url = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_URL));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_IMAGE));
                    songList.add(new Song(name, singer, genre, image, url));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return songList; // Trả về danh sách bài hát
    }
    public static List<Song> getSongsBySinger(SQLiteDatabase db, String singer) {
        List<Song> songList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, COL_SONG_SINGER + "=?", new String[]{singer}, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_NAME));
                    String genre = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_GENRE));
                    String url = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_URL));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_IMAGE));
                    songList.add(new Song(name, singer, genre, image, url));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return songList; // Trả về danh sách bài hát theo nghệ sĩ
    }


    // Thêm phương thức để lấy danh sách bài hát theo thể loại
    public static List<Song> getSongsByGenre(SQLiteDatabase db, String genre) {
        List<Song> songList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, COL_SONG_GENRE + "=?", new String[]{genre}, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_NAME));
                    String singer = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_SINGER));
                    String url = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_URL));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow(COL_SONG_IMAGE));
                    songList.add(new Song(name, singer, genre, image, url));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return songList; // Trả về danh sách bài hát theo thể loại
    }
}
