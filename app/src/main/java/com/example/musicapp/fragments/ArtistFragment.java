package com.example.musicapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.Artist.Artist;
import com.example.musicapp.Artist.ArtistAdapter;
import com.example.musicapp.Data.DatabaseHelper;
import com.example.musicapp.Data.SingerTable;
import com.example.musicapp.R;

import java.util.ArrayList;

public class ArtistFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArtistAdapter artistAdapter;
    private ArrayList<Artist> artistList;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // Display in 2 columns

        // Initialize database helper
        dbHelper = new DatabaseHelper(getContext());

        // Load artists into the list
        loadArtists();

        // Initialize Adapter without CRUD
        artistAdapter = new ArtistAdapter(getContext(), artistList, false); // `isCrud` is false
        recyclerView.setAdapter(artistAdapter);

        // Handle item click event
        artistAdapter.setOnArtistClickListener(artist -> {
            ArtistSongFragment artistSongFragment = new ArtistSongFragment();

            // Pass artist name to the next fragment
            Bundle bundle = new Bundle();
            bundle.putString("artist_name", artist.getName());
            artistSongFragment.setArguments(bundle);

            // Replace current fragment with ArtistSongFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, artistSongFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    // Method to load the artist list
    private void loadArtists() {
        artistList = SingerTable.getAllSingers(dbHelper.getReadableDatabase());
        // Check if artist list is empty
        if (artistList.isEmpty()) {
            // Optionally, show a message or handle empty list
        }
    }

    // This method can be called from other parts of the app when the database changes
    public void refreshArtists() {
        loadArtists(); // Reload the artist list
        artistAdapter.notifyDataSetChanged(); // Notify adapter of data changes
    }
}
