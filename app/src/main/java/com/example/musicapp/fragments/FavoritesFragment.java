package com.example.musicapp.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Song.SongAdapter;
import com.example.musicapp.Data.DatabaseHelper;
import com.example.musicapp.Data.FavoriteTable;
import com.example.musicapp.Song.Song;
import com.example.musicapp.R;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements SongAdapter.OnSongClickListener {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private List<Song> favoriteSongs;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        // Initialize the RecyclerView
        recyclerView = rootView.findViewById(R.id.recycler_favorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load favorite songs from the database
        loadFavoriteSongs();

        return rootView;
    }

    private void loadFavoriteSongs() {
        try (DatabaseHelper dbHelper = new DatabaseHelper(getContext());
             SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            // Get a list of Song objects with details from the favorites
            favoriteSongs = FavoriteTable.getFavoriteSongs(db);
            // Pass 'getContext()' for the context and 'this' for the fragment instance
            songAdapter = new SongAdapter(getContext(), favoriteSongs, false, this);
            recyclerView.setAdapter(songAdapter);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error loading favorites: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to remove a song from favorites
    public void removeFromFavorites(Song song) {
        try (DatabaseHelper dbHelper = new DatabaseHelper(getContext());
             SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            // Remove song from the favorites table
            boolean success = FavoriteTable.removeFromFavorites(db, song.getName());
            if (success) {
                // Update the UI by removing the song from the list
                favoriteSongs.remove(song);
                songAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), song.getName() + " removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSongClick(Song song, int position) {
        // Chuyển đến MusicPlayerFragment với thông tin bài hát
        FragmentTransaction transaction = requireFragmentManager().beginTransaction();
        MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();

        // Chuyển thông tin bài hát đã chọn và danh sách bài hát
        Bundle args = new Bundle();
        args.putSerializable("SONG_LIST", new ArrayList<>(favoriteSongs));
        args.putInt("CURRENT_SONG_INDEX", position);
        musicPlayerFragment.setArguments(args);

        transaction.replace(R.id.fragment_container, musicPlayerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
