package com.example.musicapp.Song;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.Data.DatabaseHelper;
import com.example.musicapp.Data.FavoriteTable;
import com.example.musicapp.Data.SongTable;
import com.example.musicapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private final Context context;
    private List<Song> songList;
    private final boolean isCrud;
    private final OnSongClickListener listener;

    public SongAdapter(Context context, List<Song> songList, boolean isCrud, OnSongClickListener listener) {
        this.context = context;
        this.songList = songList;
        this.isCrud = isCrud;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = isCrud ? R.layout.item_song_crud : R.layout.item_song;
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new SongViewHolder(view, isCrud);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.songName.setText(song.getName());
        holder.artistName.setText(song.getSinger());

        Glide.with(context)
                .load(song.getImageUrl())
                .placeholder(R.drawable.artist)
                .error(R.drawable.artist)
                .into(holder.songImage);

        // Kiểm tra xem bài hát có trong danh sách yêu thích hay không
        if (!isCrud) {
            try (DatabaseHelper dbHelper = new DatabaseHelper(context);
                 SQLiteDatabase db = dbHelper.getReadableDatabase()) {
                boolean isFavorite = FavoriteTable.isFavorite(db, song.getName());
                // Cập nhật biểu tượng yêu thích dựa trên trạng thái yêu thích
                holder.favoriteIcon.setImageResource(isFavorite ? R.drawable.liked : R.drawable.likesong);
            }
        }

        // Thiết lập sự kiện khi nhấp vào biểu tượng yêu thích
        if (!isCrud) {
            holder.favoriteIcon.setOnClickListener(v -> {
                try (DatabaseHelper dbHelper = new DatabaseHelper(context);
                     SQLiteDatabase db = dbHelper.getWritableDatabase()) {
                    if (FavoriteTable.isFavorite(db, song.getName())) {
                        FavoriteTable.removeFromFavorites(db, song.getName());
                        holder.favoriteIcon.setImageResource(R.drawable.likesong); // Biểu tượng không yêu thích
                        Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        FavoriteTable.addToFavorites(db, song.getName());
                        holder.favoriteIcon.setImageResource(R.drawable.liked); // Biểu tượng yêu thích
                        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        holder.itemView.setOnClickListener(v -> listener.onSongClick(song, position)); // Trigger song click event

        // Code xử lý CRUD nếu `isCrud` là true
        if (isCrud) {
            if (holder.editButton != null && holder.deleteButton != null) {
                holder.editButton.setVisibility(View.VISIBLE);
                holder.deleteButton.setVisibility(View.VISIBLE);

                holder.editButton.setOnClickListener(v -> {
                    Toast.makeText(context, "Edit song: " + song.getName(), Toast.LENGTH_SHORT).show();
                    // Add code to edit song
                });

                holder.deleteButton.setOnClickListener(v -> deleteSong(song, holder.getAdapterPosition()));
            }
        } else {
            if (holder.editButton != null && holder.deleteButton != null) {
                holder.editButton.setVisibility(View.GONE);
                holder.deleteButton.setVisibility(View.GONE);
            }
        }
    }

    private void deleteSong(Song song, int position) {
        try (DatabaseHelper dbHelper = new DatabaseHelper(context);
             SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            boolean isDeleted = SongTable.deleteSong(db, song.getName());
            if (isDeleted) {
                Toast.makeText(context, "Song deleted successfully!", Toast.LENGTH_SHORT).show();
                songList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, songList.size());
            } else {
                Toast.makeText(context, "Failed to delete song.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public void updateSongs(List<Song> newSongList) {
        this.songList = newSongList;
        notifyDataSetChanged(); // Update the adapter with new list
    }

    public interface OnSongClickListener {
        void onSongClick(Song song, int position); // Update this to receive position
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        final TextView songName;
        final TextView artistName;
        final CircleImageView songImage;
        final ImageView editButton;
        final ImageView deleteButton;
        final ImageView favoriteIcon; // Thêm biến cho biểu tượng yêu thích

        public SongViewHolder(@NonNull View itemView, boolean isCrud) {
            super(itemView);

            songName = itemView.findViewById(isCrud ? R.id.tvSongName_crud : R.id.tvSongName);
            artistName = itemView.findViewById(isCrud ? R.id.tvArtistName_crud : R.id.tvArtistName);
            songImage = itemView.findViewById(isCrud ? R.id.songImage_crud : R.id.songImage);

            if (isCrud) {
                editButton = itemView.findViewById(R.id.edit_icon2);
                deleteButton = itemView.findViewById(R.id.delete_icon2);
                favoriteIcon = null; // Không hiển thị biểu tượng yêu thích khi ở chế độ CRUD
            } else {
                editButton = null;
                deleteButton = null;
                favoriteIcon = itemView.findViewById(R.id.favoriteIcon); // Hiển thị biểu tượng yêu thích
            }
        }
    }
}
