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
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.Data.DatabaseHelper;
import com.example.musicapp.Data.SingerTable;
import com.example.musicapp.R;

public class CreateArtistFragment extends Fragment {

    private EditText artistNameEditText;
    private EditText artistImageUrlEditText;
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
        View view = inflater.inflate(R.layout.fragment_create_artist, container, false);

        // Khởi tạo các thành phần UI
        artistNameEditText = view.findViewById(R.id.category_name); // Sử dụng ID trong XML
        artistImageUrlEditText = view.findViewById(R.id.category_image_url); // Sử dụng ID trong XML
        updateButton = view.findViewById(R.id.update_button);
        backButton = view.findViewById(R.id.back_button);

        // Xử lý sự kiện cho nút quay lại
        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Xử lý sự kiện cho nút cập nhật
        updateButton.setOnClickListener(v -> updateArtist());

        return view;
    }

    private void updateArtist() {
        String artistName = artistNameEditText.getText().toString().trim();
        String artistImageUrl = artistImageUrlEditText.getText().toString().trim();

        if (artistName.isEmpty() || artistImageUrl.isEmpty()) {
            Toast.makeText(getContext(), "Please enter both name and image URL.", Toast.LENGTH_SHORT).show();
            return;
        }

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            boolean isInserted = SingerTable.insertSinger(db, artistName, artistImageUrl); // Thay đổi thành insertSinger

            if (isInserted) {
                Toast.makeText(getContext(), "Artist added successfully!", Toast.LENGTH_SHORT).show();
                artistNameEditText.setText("");
                artistImageUrlEditText.setText("");

                // Quay lại ArtistAFragment
                navigateToArtistAFragment();
            } else {
                Toast.makeText(getContext(), "Failed to add artist. It might already exist.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToArtistAFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ArtistAFragment())
                .addToBackStack(null)
                .commit();
    }
}
