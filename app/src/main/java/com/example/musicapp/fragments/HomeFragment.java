package com.example.musicapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.musicapp.R;
import com.example.musicapp.Artist.Artist;
import com.example.musicapp.Artist.ArtistAdapter;
import com.example.musicapp.Category.Category;
import com.example.musicapp.Category.CategoryAdapter;
import com.example.musicapp.Song.Song;
import com.example.musicapp.Song.SongAdapter;
import com.example.musicapp.fragments.AlbumFragment;
import com.example.musicapp.fragments.AllSongFragment;
import com.example.musicapp.fragments.ArtistFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ImageButton imgBtn1, imgBtn2, imgBtn3, imgBtn4;
    private RecyclerView recyclerViewArtist, recyclerViewCategory, recyclerViewAllSongs;
    private ArtistAdapter artistAdapter;
    private CategoryAdapter categoryAdapter;
    private SongAdapter songAdapter;
    private List<Artist> artistList;
    private List<Category> categoryList;
    private List<Song> songList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ các ImageButton
        imgBtn1 = view.findViewById(R.id.imgBtn1);
        imgBtn2 = view.findViewById(R.id.imgBtn2);
        imgBtn3 = view.findViewById(R.id.imgBtn3);
        imgBtn4 = view.findViewById(R.id.imgBt4);

        // Thiết lập sự kiện cho imgBtn1 (chuyển đến AllSongFragment)
        imgBtn1.setOnClickListener(v -> {
            Fragment allSongFragment = new AllSongFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, allSongFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Thiết lập sự kiện cho imgBtn2 (chuyển đến ArtistFragment)
        imgBtn2.setOnClickListener(v -> {
            Fragment artistFragment = new ArtistFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, artistFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Thiết lập sự kiện cho imgBtn3 (chuyển đến AlbumFragment)
        imgBtn3.setOnClickListener(v -> {
            Fragment albumFragment = new AlbumFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, albumFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // imgBtn4 chỉ ánh xạ nhưng không thực hiện chức năng gì
        imgBtn4.setOnClickListener(v -> {
            // Không thực hiện gì khi ấn imgBtn4
        });

        // Ánh xạ các RecyclerView
        recyclerViewArtist = view.findViewById(R.id.recyclerViewArtist);
        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory);
        recyclerViewAllSongs = view.findViewById(R.id.recyclerViewAllSongs);

        // Thiết lập layout cho các RecyclerView với GridLayoutManager
        recyclerViewArtist.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 cột cho artist
        recyclerViewCategory.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 cột cho category
        recyclerViewAllSongs.setLayoutManager(new LinearLayoutManager(getContext())); // Đổi sang chiều dọc cho All Songs

        // Dữ liệu giả cho Artist
        artistList = new ArrayList<>();
        artistList.add(new Artist("Artist 1", R.drawable.artist)); // Thay R.drawable.artist bằng hình ảnh thực tế
        artistList.add(new Artist("Artist 2", R.drawable.artist)); // Thay R.drawable.artist bằng hình ảnh thực tế
        artistAdapter = new ArtistAdapter(artistList, artist -> {
            // Xử lý sự kiện click vào artist nếu cần
        });
        recyclerViewArtist.setAdapter(artistAdapter);

        // Dữ liệu giả cho Category
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Pop", R.drawable.category)); // Thay R.drawable.category bằng hình ảnh thực tế
        categoryList.add(new Category("Rock", R.drawable.category)); // Thay R.drawable.category bằng hình ảnh thực tế
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        recyclerViewCategory.setAdapter(categoryAdapter);

        // Dữ liệu giả cho All Songs
        songList = new ArrayList<>();
        songList.add(new Song("Song 1", "Artist 1", "Pop", R.drawable.artist, R.raw.sakura)); // Thay R.drawable.artist và R.raw.sakura bằng dữ liệu thực tế
        songList.add(new Song("Song 2", "Artist 2", "Rock", R.drawable.artist, R.raw.sakura)); // Thay R.drawable.artist và R.raw.sakura bằng dữ liệu thực tế
        songAdapter = new SongAdapter(getContext(), songList, song -> {
            // Xử lý sự kiện click bài hát
        });
        recyclerViewAllSongs.setAdapter(songAdapter);

        return view;
    }
}
