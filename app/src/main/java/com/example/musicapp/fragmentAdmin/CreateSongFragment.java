package com.example.musicapp.fragmentAdmin;

import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.musicapp.Artist.Artist;
import com.example.musicapp.Category.Category;
import com.example.musicapp.Data.DatabaseHelper;
import com.example.musicapp.Data.GenreTable;
import com.example.musicapp.Data.SingerTable;
import com.example.musicapp.Data.SongTable;
import com.example.musicapp.R;

import java.util.ArrayList;
import java.util.List;

public class CreateSongFragment extends Fragment {

    private EditText songNameEditText, songImageUrlEditText, songFileNameEditText;
    private Spinner spinnerSingers, spinnerGenres;
    private Button saveButton;
    private SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_song, container, false);

        // Initialize fields
        songNameEditText = view.findViewById(R.id.song_name);
        songImageUrlEditText = view.findViewById(R.id.song_image_url);
        songFileNameEditText = view.findViewById(R.id.song_url); // Added song file name field
        spinnerSingers = view.findViewById(R.id.spinnerSingers);
        spinnerGenres = view.findViewById(R.id.spinnerGenres);
        saveButton = view.findViewById(R.id.update_button);

        // Open the database
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        db = dbHelper.getReadableDatabase();

        setupSpinners(); // Set up the spinners for singers and genres

        saveButton.setOnClickListener(v -> createSong()); // Call createSong on button click

        return view;
    }

    private void setupSpinners() {
        // Fetch singers from the database
        List<String> singerList = new ArrayList<>();
        ArrayList<Artist> singers = SingerTable.getAllSingers(db);
        for (Artist artist : singers) {
            singerList.add(artist.getName()); // Add artist names to the list
        }
        ArrayAdapter<String> singerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, singerList);
        singerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSingers.setAdapter(singerAdapter);

        // Fetch genres from the database
        List<String> genreList = new ArrayList<>();
        ArrayList<Category> genres = GenreTable.getAllGenres(db);
        for (Category category : genres) {
            genreList.add(category.getName()); // Add genre names to the list
        }
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, genreList);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenres.setAdapter(genreAdapter);
    }

    private void createSong() {
        // Retrieve song details from input fields
        String songName = songNameEditText.getText().toString().trim();
        String songImageUrl = songImageUrlEditText.getText().toString().trim();
        String songFileName = songFileNameEditText.getText().toString().trim(); // Retrieve song file name
        String singer = spinnerSingers.getSelectedItem().toString();
        String genre = spinnerGenres.getSelectedItem().toString();

        // Validate fields
        if (songName.isEmpty() || songImageUrl.isEmpty() || songFileName.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new song and save it in the database
        if (SongTable.createSong(db, songName, singer, genre, songFileName, songImageUrl)) {
            Toast.makeText(getActivity(), "Song created successfully", Toast.LENGTH_SHORT).show();

            // Navigate to SongFragment
            Fragment songFragment = new SongFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, songFragment) // Replace with the ID of your fragment container
                    .addToBackStack(null) // Add to back stack if needed
                    .commit();
        } else {
            Toast.makeText(getActivity(), "Error creating song", Toast.LENGTH_SHORT).show();
        }
    }

    public void playSong(String songFileName) {
        // Get the resource ID of the music file in the raw folder
        int resId = getResources().getIdentifier(songFileName, "raw", getActivity().getPackageName());
        if (resId != 0) { // Check if the file exists
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), resId);
            mediaPlayer.start();
        } else {
            Toast.makeText(getActivity(), "File not found in raw folder", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close(); // Close the database when no longer in use
        }
    }
}
