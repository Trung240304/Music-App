package com.example.musicapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Category.CategoryAdapter;
import com.example.musicapp.Category.Category;
import com.example.musicapp.Data.DatabaseHelper;
import com.example.musicapp.Data.GenreTable;
import com.example.musicapp.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends Fragment {
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        categoryList = new ArrayList<>();
        loadCategories(); // Load list of genres
        setupRecyclerView(); // Setup RecyclerView
        return view;
    }

    private void loadCategories() {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        categoryList.clear();
        categoryList.addAll(GenreTable.getAllGenres(dbHelper.getReadableDatabase()));
        dbHelper.close();
    }

    private void setupRecyclerView() {
        // Set up GridLayoutManager with 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        categoryAdapter = new CategoryAdapter(requireContext(), categoryList, false);
        recyclerView.setAdapter(categoryAdapter);

        categoryAdapter.setOnCategoryClickListener(category -> {
            // Handle genre click to open SongListFragment
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

    public void updateCategories() {
        loadCategories(); // Reload categories
        categoryAdapter.notifyDataSetChanged(); // Update RecyclerView
    }
}
