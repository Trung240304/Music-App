package com.example.musicapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.Category.Category;
import com.example.musicapp.Category.CategoryAdapter;
import com.example.musicapp.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        categoryList = new ArrayList<>();
        categoryList.add(new Category("Classical", R.drawable.category));
        categoryList.add(new Category("Indie", R.drawable.category));
        categoryList.add(new Category("Pop", R.drawable.category));
        categoryList.add(new Category("Hip Hop", R.drawable.category));
        categoryList.add(new Category("Rock", R.drawable.category));
        categoryList.add(new Category("Jazz", R.drawable.category));

        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        recyclerView.setAdapter(categoryAdapter);

        categoryAdapter.setOnCategoryClickListener(category -> {
            // Chuyển tới SongListFragment khi nhấn vào thể loại
            SongListFragment songListFragment = new SongListFragment();
            Bundle args = new Bundle();
            args.putString("categoryName", category.getName()); // Gửi tên thể loại
            songListFragment.setArguments(args);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, songListFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
