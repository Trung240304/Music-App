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
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.Artist.Artist;
import com.example.musicapp.Artist.ArtistAdapter;
import com.example.musicapp.Category.Category;
import com.example.musicapp.Category.CategoryAdapter;
import com.example.musicapp.Song.Song;
import com.example.musicapp.Song.SongAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvSearch;
    private ImageButton imgBtn1, imgBtn2, imgBtn3, imgBtn4;
    private RecyclerView recyclerViewArtist, recyclerViewCategory, recyclerViewAllSongs;
    private ArtistAdapter artistAdapter;
    private CategoryAdapter categoryAdapter;
    private SongAdapter songAdapter;
    private List<Artist> artistList;
    private List<Category> categoryList;
    private List<Song> songList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ các ImageButton
        imgBtn1 = view.findViewById(R.id.imgBtn1);
        imgBtn2 = view.findViewById(R.id.imgBtn2);
        imgBtn3 = view.findViewById(R.id.imgBtn3);
        imgBtn4 = view.findViewById(R.id.imgBt4);
        tvSearch = view.findViewById(R.id.search_bar);
        tvSearch.setOnClickListener(v -> {
            Fragment allSongFragment = new AllSongFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, allSongFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

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

        // Thiết lập layout cho các RecyclerView
        recyclerViewArtist.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewCategory.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewAllSongs.setLayoutManager(new LinearLayoutManager(getContext()));

        // Dữ liệu giả cho Artist
        artistList = new ArrayList<>();
        artistList.add(new Artist("Artist 1", R.drawable.artist));
        artistList.add(new Artist("Artist 2", R.drawable.artist));

        // Thiết lập Adapter cho Artist
        artistAdapter = new ArtistAdapter(artistList, artist -> {
            Bundle bundle = new Bundle();
            bundle.putString("artistName", artist.getName());

            ArtistSongFragment artistSongFragment = new ArtistSongFragment();
            artistSongFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, artistSongFragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerViewArtist.setAdapter(artistAdapter);

        // Dữ liệu giả cho Category
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Pop", R.drawable.category));
        categoryList.add(new Category("Rock", R.drawable.category));
        categoryList.add(new Category("Jazz", R.drawable.category));
        categoryList.add(new Category("Hip Hop", R.drawable.category));

        // Thiết lập Adapter cho Category và thiết lập sự kiện click
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        categoryAdapter.setOnCategoryClickListener(category -> {
            Bundle bundle = new Bundle();
            bundle.putString("categoryName", category.getName());

            // Chuyển đến CategorySongFragment
            CategorySongFragment categorySongFragment = new CategorySongFragment();
            categorySongFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, categorySongFragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerViewCategory.setAdapter(categoryAdapter);

        // Dữ liệu giả cho All Songs
        songList = new ArrayList<>();
        songList.add(new Song("Song 1", "Artist 1", "Pop", R.drawable.artist, R.raw.sakura));
        songList.add(new Song("Song 2", "Artist 2", "Rock", R.drawable.artist, R.raw.sakura));

        // Thiết lập Adapter cho All Songs và thiết lập sự kiện click
        songAdapter = new SongAdapter(getContext(), songList, song -> {
            // Chuyển đến MusicPlayerFragment khi ấn vào bài hát
            Bundle bundle = new Bundle();
            bundle.putInt("currentSongIndex", songList.indexOf(song));  // Truyền vị trí bài hát hiện tại

            // Sử dụng putSerializable để truyền danh sách bài hát
            bundle.putSerializable("songList", new ArrayList<>(songList));  // Truyền danh sách bài hát

            MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();
            musicPlayerFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, musicPlayerFragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerViewAllSongs.setAdapter(songAdapter);

        return view;
    }
}
