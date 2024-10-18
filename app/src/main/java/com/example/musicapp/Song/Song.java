package com.example.musicapp.Song;

import java.io.Serializable;

public class Song implements Serializable {
    private String songName;
    private String artistName;
    private String genre;
    private int songImage; // ID ảnh nhạc
    private int songFile;  // Đường dẫn file nhạc (sử dụng R.raw)

    public Song(String songName, String artistName, String genre, int songImage, int songFile) {
        this.songName = songName;
        this.artistName = artistName;
        this.genre = genre;
        this.songImage = songImage;
        this.songFile = songFile;
    }

    // Getters
    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getGenre() {
        return genre;
    }

    public int getSongImage() {
        return songImage;
    }

    public int getSongFile() {
        return songFile;
    }
}
