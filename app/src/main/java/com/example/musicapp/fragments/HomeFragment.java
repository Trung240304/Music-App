package com.example.musicapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Artist.Artist;
import com.example.musicapp.Artist.ArtistAdapter;
import com.example.musicapp.Category.Category;
import com.example.musicapp.Category.CategoryAdapter;
import com.example.musicapp.Data.DatabaseHelper;
import com.example.musicapp.Data.GenreTable;
import com.example.musicapp.Data.SingerTable;
import com.example.musicapp.Data.SongTable;
import com.example.musicapp.R;
import com.example.musicapp.Song.Song;
import com.example.musicapp.Song.SongAdapter;

import java.util.ArrayList;
import java.util.List;
public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewArtist, recyclerViewCategory, recyclerViewAllSongs;
    private ArtistAdapter artistAdapter;
    private CategoryAdapter categoryAdapter;
    private SongAdapter songAdapter;

    private List<Artist> artistList;
    private List<Category> categoryList;
    private List<Song> songList;
    private List<Song> filteredList; // Danh sách bài hát đã lọc

    private DatabaseHelper dbHelper;

    private ImageView imgBtn1, imgBtn2, imgBtn3, imgBt4;
    private TextView searchBar;  // Khai báo search bar

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ các RecyclerView và ImageButton
        recyclerViewArtist = view.findViewById(R.id.recyclerViewArtist);
        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory);
        recyclerViewAllSongs = view.findViewById(R.id.recyclerViewAllSongs);

        imgBtn1 = view.findViewById(R.id.imgBtn1);
        imgBtn2 = view.findViewById(R.id.imgBtn2);
        imgBtn3 = view.findViewById(R.id.imgBtn3);
        imgBt4 = view.findViewById(R.id.imgBt4);
        searchBar = view.findViewById(R.id.search_bar);  // Ánh xạ search bar

        // Khởi tạo database helper
        dbHelper = new DatabaseHelper(requireContext());

        // Load dữ liệu từ database
        loadArtists();
        loadCategories();
        loadSongs();

        // Thiết lập RecyclerView cho từng danh sách
        setupRecyclerViewArtist();
        setupRecyclerViewCategory();
        setupRecyclerViewAllSongs();

        // Thiết lập sự kiện click cho các ImageButton
        imgBtn1.setOnClickListener(v -> {
            // Chuyển đến AllSongFragment
            AllSongFragment allSongFragment = new AllSongFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, allSongFragment)
                    .addToBackStack(null)
                    .commit();
        });

        imgBtn2.setOnClickListener(v -> {
            // Chuyển đến ArtistFragment
            ArtistFragment artistFragment = new ArtistFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, artistFragment)
                    .addToBackStack(null)
                    .commit();
        });

        imgBtn3.setOnClickListener(v -> {
            // Chuyển đến AlbumFragment
            AlbumFragment albumFragment = new AlbumFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, albumFragment)
                    .addToBackStack(null)
                    .commit();
        });

        imgBt4.setOnClickListener(v -> {
            // Tạm thời không có chức năng gì
            // Bạn có thể thêm một thông báo hoặc hành động khác nếu cần
        });

        // Sự kiện click cho search bar
        searchBar.setOnClickListener(v -> {
            // Chuyển đến AllSongFragment khi nhấn vào search bar
            AllSongFragment allSongFragment = new AllSongFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, allSongFragment)
                    .addToBackStack(null)  // Để có thể quay lại khi bấm nút back
                    .commit();
        });

        return view;
    }

    private void loadArtists() {
        artistList = SingerTable.getAllSingers(dbHelper.getReadableDatabase());
    }

    private void loadCategories() {
        categoryList = GenreTable.getAllGenres(dbHelper.getReadableDatabase());
    }

    private void loadSongs() {
        songList = SongTable.getAllSongs(dbHelper.getReadableDatabase());
        filteredList = new ArrayList<>(songList); // Khởi tạo filteredList với toàn bộ bài hát
    }

    private void setupRecyclerViewArtist() {
        // Set up GridLayoutManager với 2 cột
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerViewArtist.setLayoutManager(layoutManager);

        // Hạn chế chỉ hiển thị 4 nghệ sĩ gần nhất
        List<Artist> limitedArtistList = artistList.size() > 4 ? artistList.subList(0, 4) : artistList;

        // Khởi tạo ArtistAdapter với danh sách nghệ sĩ giới hạn
        artistAdapter = new ArtistAdapter(requireContext(), limitedArtistList, false);
        recyclerViewArtist.setAdapter(artistAdapter);

        artistAdapter.setOnArtistClickListener(artist -> {
            // Xử lý khi người dùng chọn nghệ sĩ
            ArtistSongFragment artistSongFragment = new ArtistSongFragment();
            Bundle bundle = new Bundle();
            bundle.putString("artist_name", artist.getName());
            artistSongFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, artistSongFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void setupRecyclerViewCategory() {
        // Set up GridLayoutManager với 2 cột
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerViewCategory.setLayoutManager(layoutManager);

        // Hạn chế chỉ hiển thị 4 thể loại gần nhất
        List<Category> limitedCategoryList = categoryList.size() > 4 ? categoryList.subList(0, 4) : categoryList;

        // Khởi tạo CategoryAdapter với danh sách thể loại giới hạn
        categoryAdapter = new CategoryAdapter(requireContext(), limitedCategoryList, false);
        recyclerViewCategory.setAdapter(categoryAdapter);

        categoryAdapter.setOnCategoryClickListener(category -> {
            // Xử lý khi người dùng chọn thể loại
            SongListFragment songListFragment = new SongListFragment();
            Bundle args = new Bundle();
            args.putString("categoryName", category.getName());
            songListFragment.setArguments(args);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, songListFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void setupRecyclerViewAllSongs() {
        recyclerViewAllSongs.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Khởi tạo SongAdapter và truyền các tham số cần thiết
        songAdapter = new SongAdapter(requireContext(), filteredList, false, new SongAdapter.OnSongClickListener() {
            @Override
            public void onSongClick(Song song, int position) {
                // Xử lý khi chọn một bài hát
                MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("CURRENT_SONG_INDEX", position);  // Truyền index bài hát hiện tại
                bundle.putSerializable("SONG_LIST", new ArrayList<>(songList));  // Truyền danh sách bài hát
                musicPlayerFragment.setArguments(bundle);

                // Thay thế fragment hiện tại bằng MusicPlayerFragment
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, musicPlayerFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Đặt adapter cho RecyclerView
        recyclerViewAllSongs.setAdapter(songAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
