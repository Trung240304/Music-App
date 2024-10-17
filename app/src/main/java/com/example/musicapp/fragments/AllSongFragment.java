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

public class AllSongFragment extends Fragment {
    private ArrayList<Song> songList;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_song, container, false);

        // Khởi tạo danh sách bài hát
        songList = new ArrayList<>();
        // Thêm một số bài hát mẫu
        songList.add(new Song("Song 1", "Artist 1", "Genre 1", R.drawable.artist, R.raw.sakura));
        songList.add(new Song("Song 2", "Artist 2", "Genre 2", R.drawable.category, R.raw.sakura));
        // Thêm các bài hát khác vào danh sách...

        recyclerView = view.findViewById(R.id.recycler_view_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SongAdapter adapter = new SongAdapter(songList, new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Song song, int position) {
                playSong(position);
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void playSong(int position) {
        // Mở MusicPlayerFragment
        MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("songList", songList); // Truyền danh sách bài hát
        args.putInt("currentSongIndex", position); // Truyền chỉ số bài hát hiện tại
        musicPlayerFragment.setArguments(args); // Thiết lập Bundle cho fragment

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, musicPlayerFragment)
                .addToBackStack(null)
                .commit();
    }
}
