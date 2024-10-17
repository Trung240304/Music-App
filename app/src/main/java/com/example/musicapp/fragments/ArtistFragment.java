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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        // Thiết lập RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 cột

        // Tạo danh sách nghệ sĩ
        List<Artist> artistList = new ArrayList<>();
        artistList.add(new Artist("Nadia Sitova", R.drawable.artist)); // Thay thế bằng ID hình ảnh thực
        artistList.add(new Artist("Original", R.drawable.artist));
        artistList.add(new Artist("Jonathan Grado", R.drawable.artist));
        artistList.add(new Artist("Puk Khantho", R.drawable.artist));
        artistList.add(new Artist("Caio Henrique", R.drawable.artist));
        artistList.add(new Artist("Alice Moore", R.drawable.artist));

        // Khởi tạo Adapter và gán cho RecyclerView
        artistAdapter = new ArtistAdapter(artistList);
        recyclerView.setAdapter(artistAdapter);

        return view;
    }
}
