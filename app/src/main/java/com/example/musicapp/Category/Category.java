package com.example.musicapp.Category;

public class Category {
    private String name;
    private String imageResource; // Đổi thành String để lưu URL hình ảnh

    public Category(String name, String imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getImageResource() {
        return imageResource;
    }
}
