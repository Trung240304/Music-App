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

public class ArtistSongFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private List<Song> songList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_song, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSongs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy tên nghệ sĩ từ bundle
        if (getArguments() != null) {
            String artistName = getArguments().getString("artistName");

            // Lấy danh sách bài hát của nghệ sĩ đó (dữ liệu mẫu)
            songList = getSongsForArtist(artistName);
        }

        // Khởi tạo adapter và thiết lập sự kiện click cho bài hát
        songAdapter = new SongAdapter(getContext(), songList, song -> {
            // Khi click vào bài hát, chuyển tới MusicPlayerFragment
            Bundle bundle = new Bundle();
            bundle.putInt("currentSongIndex", songList.indexOf(song));  // Lưu chỉ số bài hát
            bundle.putSerializable("songList", new ArrayList<>(songList));  // Lưu danh sách bài hát

            MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();
            musicPlayerFragment.setArguments(bundle);

            // Thay thế fragment hiện tại bằng MusicPlayerFragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, musicPlayerFragment)  // Thay thế với MusicPlayerFragment
                    .addToBackStack(null)  // Thêm vào BackStack để quay lại được
                    .commit();
        });
        recyclerView.setAdapter(songAdapter);

        return view;
    }

    // Dữ liệu mẫu cho bài hát của nghệ sĩ
    private List<Song> getSongsForArtist(String artistName) {
        List<Song> songs = new ArrayList<>();
        if (artistName.equals("Artist 1")) {
            songs.add(new Song("Song 1", "Artist 1", "Pop", R.drawable.artist, R.raw.sakura));
            songs.add(new Song("Song 2", "Artist 1", "Pop", R.drawable.artist, R.raw.sakura));
        } else if (artistName.equals("Artist 2")) {
            songs.add(new Song("Song 3", "Artist 2", "Rock", R.drawable.artist, R.raw.sakura));
            songs.add(new Song("Song 4", "Artist 2", "Rock", R.drawable.artist, R.raw.sakura));
        }
        return songs;
    }
}
