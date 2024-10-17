package com.example.musicapp.Song;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private String songName;
    private String artistName;
    private String genre;
    private int artistImageResource;
    private int songResource;

    public Song(String songName, String artistName, String genre, int artistImageResource, int songResource) {
        this.songName = songName;
        this.artistName = artistName;
        this.genre = genre;
        this.artistImageResource = artistImageResource;
        this.songResource = songResource;
    }

    protected Song(Parcel in) {
        songName = in.readString();
        artistName = in.readString();
        genre = in.readString();
        artistImageResource = in.readInt();
        songResource = in.readInt();
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

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getSongResource() {
        return songResource;
    }

    public int getArtistImageResource() {
        return artistImageResource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songName);
        dest.writeString(artistName);
        dest.writeString(genre);
        dest.writeInt(artistImageResource);
        dest.writeInt(songResource);
    }
}
