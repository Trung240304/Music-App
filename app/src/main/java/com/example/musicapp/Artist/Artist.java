package com.example.musicapp.Artist;

public class Artist {
    private String name;
    private String imageUrl;  // URL of the artist's image

    public Artist(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
