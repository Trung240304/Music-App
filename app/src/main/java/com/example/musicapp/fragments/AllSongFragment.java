package com.example.musicapp.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
    private List<Song> filteredList; // Danh sách bài hát đã được lọc
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_song, container, false);

        // Ánh xạ RecyclerView và SearchView
        recyclerView = view.findViewById(R.id.recycler_view_songs);
        searchView = view.findViewById(R.id.search_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        songList = new ArrayList<>();
        // Thêm dữ liệu mẫu
        songList.add(new Song("Song 1", "Artist 1", "Pop", R.drawable.artist, R.raw.sakura));
        songList.add(new Song("Song 2", "Artist 2", "Rock", R.drawable.category, R.raw.sakura));

        // Sao chép danh sách ban đầu vào danh sách lọc
        filteredList = new ArrayList<>(songList);

        // Khởi tạo adapter cho RecyclerView
        songAdapter = new SongAdapter(getContext(), filteredList, song -> {
            // Chuyển đến MusicPlayerFragment khi ấn vào bài hát
            Bundle bundle = new Bundle();
            bundle.putInt("currentSongIndex", songList.indexOf(song));  // Truyền vị trí bài hát hiện tại
            bundle.putSerializable("songList", new ArrayList<>(songList));  // Truyền danh sách bài hát

            MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();
            musicPlayerFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, musicPlayerFragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(songAdapter);

        // Lắng nghe sự thay đổi của văn bản trong SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;  // Không cần xử lý khi nhấn "submit"
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Gọi phương thức lọc danh sách khi văn bản tìm kiếm thay đổi
                filter(newText);
                return true;
            }
        });

        return view;
    }

    // Phương thức lọc danh sách bài hát
    private void filter(String text) {
        filteredList.clear();
        if (TextUtils.isEmpty(text)) {
            // Nếu không có gì trong ô tìm kiếm, hiển thị tất cả bài hát
            filteredList.addAll(songList);
        } else {
            // Lọc danh sách dựa trên văn bản tìm kiếm
            for (Song song : songList) {
                if (song.getSongName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(song);
                }
            }
        }
        // Cập nhật adapter
        songAdapter.notifyDataSetChanged();
    }
}
