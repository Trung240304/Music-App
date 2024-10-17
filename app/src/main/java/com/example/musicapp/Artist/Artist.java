package com.example.musicapp.Artist;

public class Artist {
    private String name;
    private int imageResId; // ID của hình ảnh

    public Artist(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}
