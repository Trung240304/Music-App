package com.example.musicapp.Song;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private String songName;
    private String artistName;
    private String genre;
    private int songImage; // ID ảnh nhạc
    private int songFile; // Đường dẫn file nhạc (sử dụng R.raw)

    public Song(String songName, String artistName, String genre, int songImage, int songFile) {
        this.songName = songName;
        this.artistName = artistName;
        this.genre = genre;
        this.songImage = songImage;
        this.songFile = songFile;
    }

    // Getters
    public String getSongName() { return songName; }
    public String getArtistName() { return artistName; }
    public String getGenre() { return genre; }
    public int getSongImage() { return songImage; }
    public int getSongFile() { return songFile; }

    // Implement Parcelable
    protected Song(Parcel in) {
        songName = in.readString();
        artistName = in.readString();
        genre = in.readString();
        songImage = in.readInt();
        songFile = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songName);
        dest.writeString(artistName);
        dest.writeString(genre);
        dest.writeInt(songImage);
        dest.writeInt(songFile);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
