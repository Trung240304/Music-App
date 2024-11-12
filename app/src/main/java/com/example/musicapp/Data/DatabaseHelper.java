package com.example.musicapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MusicApp.db";
    private static final int DATABASE_VERSION = 5;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        AccountTable.onCreate(db);
        SingerTable.onCreate(db);
        GenreTable.onCreate(db);
        SongTable.onCreate(db);
        FavoriteTable.onCreate(db); // Thêm bảng FavoriteTable
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AccountTable.onUpgrade(db, oldVersion, newVersion);
        SingerTable.onUpgrade(db, oldVersion, newVersion);
        GenreTable.onUpgrade(db, oldVersion, newVersion);
        SongTable.onUpgrade(db, oldVersion, newVersion);
        FavoriteTable.onUpgrade(db, oldVersion, newVersion); // Nâng cấp bảng FavoriteTable
    }
}
