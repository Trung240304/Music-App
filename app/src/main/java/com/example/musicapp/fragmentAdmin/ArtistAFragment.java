package com.example.musicapp.fragmentAdmin;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.musicapp.Artist.ArtistAdapter;
import com.example.musicapp.Artist.Artist;
import com.example.musicapp.Data.DatabaseHelper;
import com.example.musicapp.Data.SingerTable;
import com.example.musicapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class ArtistAFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArtistAdapter artistAdapter;
    private ArrayList<Artist> artistList;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_a, container, false);

        dbHelper = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.recyclerViewArtistCRUD);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadArtists();

        FloatingActionButton fab = view.findViewById(R.id.fad);
        fab.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CreateArtistFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loadArtists() {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            artistList = SingerTable.getAllSingers(db);
            artistAdapter = new ArtistAdapter(getContext(), artistList, true);
            recyclerView.setAdapter(artistAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
