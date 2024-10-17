package com.example.musicapp.Data;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SongTable {
    private static final String TABLE_NAME = "songs";
    private static final String COL_SONG_NAME = "song_name";
    private static final String COL_SONG_SINGER = "song_singer";
    private static final String COL_SONG_GENRE = "song_genre";
    private static final String COL_SONG_LYRICS = "song_lyrics";
    private static final String COL_SONG_FILE = "song_file";

    public static void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_SONG_NAME + " TEXT PRIMARY KEY, "
                + COL_SONG_SINGER + " TEXT, "
                + COL_SONG_GENRE + " TEXT, "
                + COL_SONG_LYRICS + " TEXT, "
                + COL_SONG_FILE + " TEXT, "
                + "FOREIGN KEY(" + COL_SONG_SINGER + ") REFERENCES singers(singer_name),"
                + "FOREIGN KEY(" + COL_SONG_GENRE + ") REFERENCES genres(genre_name))";
        db.execSQL(createTable);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static boolean insertSong(SQLiteDatabase db, String name, String singer, String genre, String lyrics, String songFile) {
        ContentValues values = new ContentValues();
        values.put(COL_SONG_NAME, name);
        values.put(COL_SONG_SINGER, singer);
        values.put(COL_SONG_GENRE, genre);
        values.put(COL_SONG_LYRICS, lyrics);
        values.put(COL_SONG_FILE, songFile);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }
}
