package com.example.musicapp.Song;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.musicapp.R;
import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private ArrayList<Song> songList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Song song, int position);
    }

    public SongAdapter(ArrayList<Song> songList, OnItemClickListener listener) {
        this.songList = songList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout file for each item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.bind(song, listener);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        private ImageView songImage;
        private TextView tvSongName;
        private TextView tvArtistName;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            // Connect the views from the layout
            songImage = itemView.findViewById(R.id.songImage);
            tvSongName = itemView.findViewById(R.id.tvSongName);
            tvArtistName = itemView.findViewById(R.id.tvArtistName);
        }

        public void bind(Song song, OnItemClickListener listener) {
            // Set the song details in the respective views
            tvSongName.setText(song.getSongName());
            tvArtistName.setText(song.getArtistName());
            songImage.setImageResource(song.getArtistImageResource()); // Set artist image
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(song, getAdapterPosition());
                }
            });
        }
    }
}
