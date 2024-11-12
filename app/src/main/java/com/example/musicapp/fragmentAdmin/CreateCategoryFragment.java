package com.example.musicapp.fragmentAdmin;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicapp.Data.DatabaseHelper;
import com.example.musicapp.Data.GenreTable;
import com.example.musicapp.R;

public class CreateCategoryFragment extends Fragment {

    private EditText categoryNameEditText;
    private EditText categoryImageUrlEditText;
    private Button updateButton;
    private ImageView backButton;
    private DatabaseHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_category, container, false);

        categoryNameEditText = view.findViewById(R.id.category_name);
        categoryImageUrlEditText = view.findViewById(R.id.category_image_url);
        updateButton = view.findViewById(R.id.update_button);
        backButton = view.findViewById(R.id.back_button);

        // Xử lý khi nhấn nút update
        updateButton.setOnClickListener(v -> updateCategory());

        // Xử lý nút back
        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    private void updateCategory() {
        String categoryName = categoryNameEditText.getText().toString().trim();
        String categoryImageUrl = categoryImageUrlEditText.getText().toString().trim();

        if (categoryName.isEmpty() || categoryImageUrl.isEmpty()) {
            Toast.makeText(getContext(), "Please enter both name and image URL.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu vào cơ sở dữ liệu
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            boolean isInserted = GenreTable.insertGenre(db, categoryName, categoryImageUrl);

            if (isInserted) {
                Toast.makeText(getContext(), "Category added successfully!", Toast.LENGTH_SHORT).show();
                categoryNameEditText.setText("");
                categoryImageUrlEditText.setText("");

                // Quay lại AlbumAFragment
                navigateToAlbumAFragment();
            } else {
                Toast.makeText(getContext(), "Failed to add category. It might already exist.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToAlbumAFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AlbumAFragment())
                .addToBackStack(null)
                .commit();
    }
}
