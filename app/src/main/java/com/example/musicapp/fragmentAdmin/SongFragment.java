package com.example.musicapp.fragmentAdmin;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Data.DatabaseHelper;
import com.example.musicapp.Data.SongTable;
import com.example.musicapp.R;
import com.example.musicapp.Song.Song;
import com.example.musicapp.Song.SongAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SongFragment extends Fragment {

    private EditText searchBar;
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private List<Song> songList;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(getContext());

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_song_crud);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize search bar and its key listener for searching songs
        searchBar = view.findViewById(R.id.search_bar);
        searchBar.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                String query = searchBar.getText().toString().trim();
                searchSongs(query);
                return true;
            }
            return false;
        });

        // Set FloatingActionButton to open CreateSongFragment
        FloatingActionButton fab = view.findViewById(R.id.fad);
        fab.setOnClickListener(v -> openCreateSongFragment());

        // Load the list of songs
        loadSongs();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSongs(); // Reload songs when the fragment is shown again
    }

    private void loadSongs() {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            songList = SongTable.getAllSongs(db); // Retrieve all songs from the database
            songAdapter = new SongAdapter(getContext(), songList, true, (song, position) -> {
                // Handle song click event
                Toast.makeText(getContext(), "Clicked on: " + song.getName(), Toast.LENGTH_SHORT).show();
                // You can add code to open the song detail page here
            });
            recyclerView.setAdapter(songAdapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error loading song list.", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchSongs(String query) {
        // Filter songs based on the search query
        List<Song> filteredSongs = new ArrayList<>();
        for (Song song : songList) {
            if (song.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredSongs.add(song);
            }
        }
        songAdapter.updateSongs(filteredSongs); // Update the adapter with the filtered list
    }

    private void openCreateSongFragment() {
        // Open the CreateSongFragment when FAB is clicked
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new CreateSongFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close(); // Close the database helper when fragment is destroyed
        }
    }
}
