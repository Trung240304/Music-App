package com.example.musicapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.R;
import com.example.musicapp.Song.Song;
import com.example.musicapp.Song.SongAdapter;

import java.util.ArrayList;
import java.util.List;

public class SongListArtistFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private List<Song> songList;

    // Tạo danh sách bài hát mẫu theo nghệ sĩ
    private List<Song> getSongsForArtist(String artistName) {
        List<Song> songs = new ArrayList<>();
        switch (artistName) {
            case "Nadia Sitova":
                break;
            case "Original":

                break;
            case "Jonathan Grado":

                break;
            case "Puk Khantho":
                break;
            case "Caio Henrique":
                break;
            case "Alice Moore":
                break;
            // Tiếp tục thêm nghệ sĩ khác nếu cần...
            default:
                break;
        }
        return songs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Nhận tên nghệ sĩ từ arguments
        if (getArguments() != null) {
            String artistName = getArguments().getString("artist_name");
            songList = getSongsForArtist(artistName);
        }

        // Thiết lập Adapter cho RecyclerView


        recyclerView.setAdapter(songAdapter);

        return view;
    }
}
