package com.example.musicapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.musicapp.R;
import com.example.musicapp.Song.Song;
import com.example.musicapp.Song.SongAdapter;

import java.util.ArrayList;
import java.util.List;

public class AllSongFragment extends Fragment {
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private List<Song> songList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_song, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        songList = new ArrayList<>();
        // Thêm dữ liệu mẫu
        songList.add(new Song("Song 1", "Artist 1", "Pop", R.drawable.artist, R.raw.sakura));
        songList.add(new Song("Song 2", "Artist 2", "Rock", R.drawable.category, R.raw.sakura));

        // Khởi tạo adapter cho RecyclerView và xử lý sự kiện khi chọn bài hát
        songAdapter = new SongAdapter(getContext(), songList, song -> {
            // Chuyển đến MusicPlayerFragment khi ấn vào bài hát
            Bundle bundle = new Bundle();
            bundle.putInt("currentSongIndex", songList.indexOf(song));  // Truyền vị trí bài hát hiện tại
            bundle.putParcelableArrayList("songList", new ArrayList<>(songList));  // Truyền danh sách bài hát

            MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();
            musicPlayerFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, musicPlayerFragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(songAdapter);
        return view;
    }
}
