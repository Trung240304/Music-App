package com.example.musicapp.Artist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.musicapp.R;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private List<Artist> artistList;
    private OnItemClickListener onItemClickListener;

    // Interface để lắng nghe sự kiện click
    public interface OnItemClickListener {
        void onItemClick(Artist artist);
    }

    public ArtistAdapter(List<Artist> artistList, OnItemClickListener onItemClickListener) {
        this.artistList = artistList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = artistList.get(position);
        holder.imageViewArtist.setImageResource(artist.getImageResId());
        holder.textViewArtistName.setText(artist.getName());

        // Set sự kiện khi click vào item
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(artist));
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    // Phương thức cập nhật dữ liệu
    public void updateData(List<Artist> newArtistList) {
        this.artistList = newArtistList;
        notifyDataSetChanged();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewArtist;
        TextView textViewArtistName;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewArtist = itemView.findViewById(R.id.imageViewArtist);
            textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
        }
    }
}
