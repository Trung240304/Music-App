package com.example.musicapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.Song.Song;
import com.example.musicapp.Song.SongAdapter;
import com.example.musicapp.Data.DatabaseHelper;
import com.example.musicapp.Data.SongTable;

import java.util.ArrayList;
import java.util.List;

public class ArtistSongFragment extends Fragment implements SongAdapter.OnSongClickListener {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private List<Song> songList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_song, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewSongs);

        // Retrieve artist name passed from another fragment
        String artistName = getArguments() != null ? getArguments().getString("artist_name") : null;

        if (artistName != null) {
            loadSongsForArtist(artistName);
            setupRecyclerView();
        } else {
            Toast.makeText(requireContext(), "Artist not found", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void loadSongsForArtist(String artistName) {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        try {
            songList = SongTable.getSongsBySinger(dbHelper.getReadableDatabase(), artistName);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error loading songs: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbHelper.close();
        }

        // Check if song list is empty
        if (songList == null || songList.isEmpty()) {
            Toast.makeText(requireContext(), "No songs found for this artist", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        songAdapter = new SongAdapter(requireContext(), songList, false, this);
        recyclerView.setAdapter(songAdapter);
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
