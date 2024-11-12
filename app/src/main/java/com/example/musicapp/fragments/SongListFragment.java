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

public class SongListFragment extends Fragment implements SongAdapter.OnSongClickListener {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private List<Song> songList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        // Retrieve category name passed from AlbumFragment
        String categoryName = getArguments() != null ? getArguments().getString("categoryName") : null;
        if (categoryName != null) {
            loadSongsByCategory(categoryName);
            setupRecyclerView();
        } else {
            Toast.makeText(requireContext(), "Category not found", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void loadSongsByCategory(String categoryName) {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        songList = SongTable.getSongsByGenre(dbHelper.getReadableDatabase(), categoryName);
        dbHelper.close();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize the adapter with a click listener
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
