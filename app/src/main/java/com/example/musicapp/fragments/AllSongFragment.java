package com.example.musicapp.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Data.DatabaseHelper;
import com.example.musicapp.Data.SongTable;
import com.example.musicapp.R;
import com.example.musicapp.Song.Song;
import com.example.musicapp.Song.SongAdapter;

import java.util.ArrayList;
import java.util.List;

public class AllSongFragment extends Fragment implements SongAdapter.OnSongClickListener {
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private List<Song> songList;
    private List<Song> filteredList;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_song, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_songs);
        searchView = view.findViewById(R.id.search_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        songList = new ArrayList<>();
        loadSongs(); // Load all songs into songList

        filteredList = new ArrayList<>(songList);
        songAdapter = new SongAdapter(getContext(), filteredList, false, this); // No CRUD functionality
        recyclerView.setAdapter(songAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText); // Filter songs based on search text
                return true;
            }
        });

        return view;
    }

    private void loadSongs() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            songList.clear();
            songList.addAll(SongTable.getAllSongs(db)); // Load songs from database
        }
    }

    private void filter(String text) {
        filteredList.clear();
        if (TextUtils.isEmpty(text)) {
            filteredList.addAll(songList); // If search is empty, show all songs
        } else {
            for (Song song : songList) {
                // Filter based on song name or singer
                if (song.getName().toLowerCase().contains(text.toLowerCase()) ||
                        song.getSinger().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(song);
                }
            }
        }
        songAdapter.notifyDataSetChanged(); // Notify adapter of the filtered list
    }

    @Override
    public void onSongClick(Song song, int position) {
        // Chuyển đến MusicPlayerFragment với thông tin bài hát
        FragmentTransaction transaction = requireFragmentManager().beginTransaction();
        MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();

        // Chuyển thông tin bài hát đã chọn và danh sách bài hát
        Bundle args = new Bundle();
        args.putSerializable("SONG_LIST", new ArrayList<>(songList));
        args.putInt("CURRENT_SONG_INDEX", position);
        musicPlayerFragment.setArguments(args);

        transaction.replace(R.id.fragment_container, musicPlayerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
