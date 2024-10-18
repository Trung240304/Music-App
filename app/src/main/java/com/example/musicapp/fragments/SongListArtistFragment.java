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
                songs.add(new Song("Classical Song 1", "Nadia Sitova", "Classical", R.drawable.artist, R.raw.sakura));
                songs.add(new Song("Classical Song 2", "Nadia Sitova", "Classical", R.drawable.artist, R.raw.sakura));
                break;
            case "Original":
                songs.add(new Song("Original Song 1", "Original", "Indie", R.drawable.artist, R.raw.sakura));
                songs.add(new Song("Original Song 2", "Original", "Indie", R.drawable.artist, R.raw.sakura));
                break;
            case "Jonathan Grado":
                songs.add(new Song("Grado Song 1", "Jonathan Grado", "Rock", R.drawable.artist, R.raw.sakura));
                songs.add(new Song("Grado Song 2", "Jonathan Grado", "Rock", R.drawable.artist, R.raw.sakura));
                break;
            case "Puk Khantho":
                songs.add(new Song("Khantho Song 1", "Puk Khantho", "Pop", R.drawable.artist, R.raw.sakura));
                songs.add(new Song("Khantho Song 2", "Puk Khantho", "Pop", R.drawable.artist, R.raw.sakura));
                break;
            case "Caio Henrique":
                songs.add(new Song("Henrique Song 1", "Caio Henrique", "Jazz", R.drawable.artist, R.raw.sakura));
                songs.add(new Song("Henrique Song 2", "Caio Henrique", "Jazz", R.drawable.artist, R.raw.sakura));
                break;
            case "Alice Moore":
                songs.add(new Song("Moore Song 1", "Alice Moore", "Hip Hop", R.drawable.artist, R.raw.sakura));
                songs.add(new Song("Moore Song 2", "Alice Moore", "Hip Hop", R.drawable.artist, R.raw.sakura));
                break;
            // Tiếp tục thêm nghệ sĩ khác nếu cần...
            default:
                songs.add(new Song("Unknown Artist Song", "Unknown Artist", "Unknown", R.drawable.artist, R.raw.sakura));
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
        songAdapter = new SongAdapter(getContext(), songList, song -> {
            // Xử lý sự kiện khi chọn bài hát
            Bundle bundle = new Bundle();
            bundle.putInt("currentSongIndex", songList.indexOf(song));
            bundle.putSerializable("songList", new ArrayList<>(songList));

            MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();
            musicPlayerFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, musicPlayerFragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(songAdapter);

        return view;
    }
}
