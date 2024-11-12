package com.example.musicapp.fragmentAdmin;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.example.musicapp.Data.DatabaseHelper;
import com.example.musicapp.Data.GenreTable;
import com.example.musicapp.Category.Category;
import com.example.musicapp.Category.CategoryAdapter;
import com.example.musicapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AlbumAFragment extends Fragment {
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categoryList;
    private FloatingActionButton fabCreate; // Thêm biến cho FAB

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_a, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        fabCreate = view.findViewById(R.id.fad); // Khởi tạo FAB
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadCategories(); // Tải danh sách thể loại từ cơ sở dữ liệu

        // Thiết lập sự kiện cho FAB
        fabCreate.setOnClickListener(v -> {
            // Chuyển đến CreateCategoryFragment
            Fragment createCategoryFragment = new CreateCategoryFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, createCategoryFragment); // 'fragment_container' là ID của container chứa fragment
            transaction.addToBackStack(null); // Để có thể quay lại fragment trước đó
            transaction.commit();
        });

        return view;
    }

    private void loadCategories() {
        try (DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
             SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            categoryList = GenreTable.getAllGenres(db);
            categoryAdapter = new CategoryAdapter(getActivity(), categoryList, true);
            recyclerView.setAdapter(categoryAdapter);
            categoryAdapter.setOnCategoryClickListener(category -> {
                // Xử lý sự kiện nhấn vào category nếu cần
            });
        }
    }
}
