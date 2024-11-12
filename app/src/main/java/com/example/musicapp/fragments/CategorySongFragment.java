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

public class CategorySongFragment extends Fragment {
    private RecyclerView recyclerViewSongs;
    private SongAdapter songAdapter;
    private List<Song> filteredSongList;

    // Tạo danh sách bài hát mẫu theo thể loại
    private List<Song> getSongsForCategory(String categoryName) {
        List<Song> songs = new ArrayList<>();
        switch (categoryName) {
            case "Pop":
                break;
            case "Rock":
                break;
            case "Jazz":
                break;
            case "Hip Hop":
                break;
            default:
                break;
        }
        return songs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_song, container, false);

        recyclerViewSongs = view.findViewById(R.id.recyclerViewSongsCategory);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(getContext()));

        // Nhận dữ liệu từ HomeFragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            String categoryName = bundle.getString("categoryName");
            // Gọi phương thức lấy bài hát theo thể loại
            filteredSongList = getSongsForCategory(categoryName);
        }



        recyclerViewSongs.setAdapter(songAdapter);

        return view;
    }
}
