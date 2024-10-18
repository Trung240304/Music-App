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
import com.example.musicapp.R;

import java.util.ArrayList;
import java.util.List;

public class ArtistFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArtistAdapter artistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho fragment này
        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        // Thiết lập RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // Hiển thị theo 2 cột

        // Tạo danh sách nghệ sĩ
        List<Artist> artistList = new ArrayList<>();
        artistList.add(new Artist("Nadia Sitova", R.drawable.artist));
        artistList.add(new Artist("Original", R.drawable.artist));
        artistList.add(new Artist("Jonathan Grado", R.drawable.artist));
        artistList.add(new Artist("Puk Khantho", R.drawable.artist));
        artistList.add(new Artist("Caio Henrique", R.drawable.artist));
        artistList.add(new Artist("Alice Moore", R.drawable.artist));

        // Khởi tạo Adapter và xử lý khi click vào item
        artistAdapter = new ArtistAdapter(artistList, artist -> {
            SongListArtistFragment songListArtistFragment = new SongListArtistFragment();

            // Truyền tên nghệ sĩ qua fragment khác
            Bundle bundle = new Bundle();
            bundle.putString("artist_name", artist.getName());
            songListArtistFragment.setArguments(bundle);

            // Thay thế fragment hiện tại bằng SongListArtistFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, songListArtistFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Gán Adapter cho RecyclerView
        recyclerView.setAdapter(artistAdapter);

        return view;
    }
}
