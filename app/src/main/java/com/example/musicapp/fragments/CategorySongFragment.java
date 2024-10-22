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
                songs.add(new Song("Pop Song 1", "Pop Artist 1", "Pop", R.drawable.artist, R.raw.sakura));
                songs.add(new Song("Pop Song 2", "Pop Artist 2", "Pop", R.drawable.artist, R.raw.sakura));
                break;
            case "Rock":
                songs.add(new Song("Rock Song 1", "Rock Artist 1", "Rock", R.drawable.artist, R.raw.sakura));
                songs.add(new Song("Rock Song 2", "Rock Artist 2", "Rock", R.drawable.artist, R.raw.sakura));
                break;
            case "Jazz":
                songs.add(new Song("Jazz Song 1", "Jazz Artist 1", "Jazz", R.drawable.artist, R.raw.sakura));
                songs.add(new Song("Jazz Song 2", "Jazz Artist 2", "Jazz", R.drawable.artist, R.raw.sakura));
                break;
            case "Hip Hop":
                songs.add(new Song("Hip Hop Song 1", "Hip Hop Artist 1", "Hip Hop", R.drawable.artist, R.raw.sakura));
                songs.add(new Song("Hip Hop Song 2", "Hip Hop Artist 2", "Hip Hop", R.drawable.artist, R.raw.sakura));
                break;
            default:
                songs.add(new Song("Unknown Category Song", "Unknown Artist", "Unknown", R.drawable.artist, R.raw.sakura));
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

        // Thiết lập Adapter cho RecyclerView
        songAdapter = new SongAdapter(getContext(), filteredSongList, song -> {
            // Xử lý sự kiện khi chọn bài hát
            int position = filteredSongList.indexOf(song); // Lấy vị trí của bài hát
            Bundle songBundle = new Bundle();
            songBundle.putInt("currentSongIndex", position);
            songBundle.putSerializable("songList", new ArrayList<>(filteredSongList));

            MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();
            musicPlayerFragment.setArguments(songBundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, musicPlayerFragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerViewSongs.setAdapter(songAdapter);

        return view;
    }
}
