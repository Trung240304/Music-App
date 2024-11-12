package com.example.musicapp.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicapp.Song.Song;

import java.util.ArrayList;
import java.util.List;

public class FavoriteTable {
    private static final String TABLE_NAME = "favorites";
    private static final String COL_SONG_NAME = "song_name"; // Foreign key referencing song_name in songs table

    // Create the favorites table
    public static void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_SONG_NAME + " TEXT PRIMARY KEY, " +
                "FOREIGN KEY(" + COL_SONG_NAME + ") REFERENCES songs(song_name) ON DELETE CASCADE)";
        db.execSQL(createTable);
    }

    // Upgrade the favorites table
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Add a song to the favorites table
    public static boolean addToFavorites(SQLiteDatabase db, String songName) {
        ContentValues values = new ContentValues();
        values.put(COL_SONG_NAME, songName);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1; // Returns true if insertion is successful
    }

    // Check if a song is in the favorites table
    public static boolean isFavorite(SQLiteDatabase db, String songName) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_SONG_NAME + " = ?";
        try (Cursor cursor = db.rawQuery(query, new String[]{songName})) {
            return cursor.getCount() > 0; // Returns true if the song exists in the favorites
        }
    }

    // Remove a song from the favorites table
    public static boolean removeFromFavorites(SQLiteDatabase db, String songName) {
        int rowsDeleted = db.delete(TABLE_NAME, COL_SONG_NAME + " = ?", new String[]{songName});
        return rowsDeleted > 0; // Returns true if deletion is successful
    }

    // Get all favorite songs with their details by joining with the songs table
    public static List<Song> getFavoriteSongs(SQLiteDatabase db) {
        List<Song> songList = new ArrayList<>();
        String query = "SELECT song.song_name, song.song_singer, song.song_genre, song.song_image, song.song_url " +
                "FROM " + TABLE_NAME + " AS favorites " +
                "JOIN songs AS song ON favorites.song_name = song.song_name";
        try (Cursor cursor = db.rawQuery(query, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("song_name"));
                    String singer = cursor.getString(cursor.getColumnIndexOrThrow("song_singer"));
                    String genre = cursor.getString(cursor.getColumnIndexOrThrow("song_genre"));
                    String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("song_image"));
                    String url = cursor.getString(cursor.getColumnIndexOrThrow("song_url"));

                    // Create a Song object and add it to the list
                    Song song = new Song(name, singer, genre, imageUrl, url);
                    songList.add(song);
                } while (cursor.moveToNext());
            }
        }
        return songList;
    }
}
