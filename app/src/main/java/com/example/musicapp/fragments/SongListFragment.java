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

public class SongListFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private List<Song> songList;

    // Tạo một danh sách bài hát mẫu cho từng thể loại
    private List<Song> getSongsForCategory(String category) {
        List<Song> songs = new ArrayList<>();
        switch (category) {
            case "Classical":
                songs.add(new Song("Symphony No. 5", "Beethoven", "Classical", R.drawable.artist, R.raw.sakura));
                break;
            case "Indie":
                songs.add(new Song("Indie Song 1", "Indie Artist", "Indie", R.drawable.artist, R.raw.sakura));
                break;
            case "Pop":
                songs.add(new Song("Pop Song 1", "Pop Artist", "Pop", R.drawable.artist, R.raw.sakura));
                break;
            case "Hip Hop":
                songs.add(new Song("Hip Hop Song 1", "Hip Hop Artist", "Hip Hop", R.drawable.artist, R.raw.sakura));
                break;
            case "Rock":
                songs.add(new Song("Rock Song 1", "Rock Artist", "Rock", R.drawable.artist, R.raw.sakura));
                break;
            case "Jazz":
                songs.add(new Song("Jazz Song 1", "Jazz Artist", "Jazz", R.drawable.artist, R.raw.sakura));
                break;
        }
        return songs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Nhận tên thể loại từ đối số
        if (getArguments() != null) {
            String categoryName = getArguments().getString("categoryName");
            songList = getSongsForCategory(categoryName); // Lấy danh sách bài hát theo thể loại
        }

        // Xử lý sự kiện khi chọn bài hát
        songAdapter = new SongAdapter(getContext(), songList, song -> {
            Bundle bundle = new Bundle();
            bundle.putInt("currentSongIndex", songList.indexOf(song)); // Truyền vị trí bài hát hiện tại
            bundle.putSerializable("songList", new ArrayList<>(songList)); // Truyền danh sách bài hát

            // Chuyển sang MusicPlayerFragment
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
