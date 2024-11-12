package com.example.musicapp.Song;
import java.io.Serializable;

public class Song implements Serializable  {
    private String name;
    private String singer;
    private String genre;
    private String imageUrl;
    private String url;

    public Song(String name, String singer, String genre, String imageUrl, String url) {
        this.name = name;
        this.singer = singer;
        this.genre = genre;
        this.imageUrl = imageUrl;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    public String getGenre() {
        return genre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUrl() {
        return url;
    }
}
