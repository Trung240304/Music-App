package com.example.musicapp.Artist;

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
import com.example.musicapp.Data.SingerTable;
import com.example.musicapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private final Context context;
    private final List<Artist> artistList;
    private OnArtistClickListener onArtistClickListener;
    private final boolean isCrud;

    public interface OnArtistClickListener {
        void onArtistClick(Artist artist);
    }

    public void setOnArtistClickListener(OnArtistClickListener listener) {
        this.onArtistClickListener = listener;
    }

    public ArtistAdapter(Context context, List<Artist> artistList, boolean isCrud) {
        this.context = context;
        this.artistList = artistList;
        this.isCrud = isCrud;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Chọn layout dựa trên trạng thái CRUD
        int layout = isCrud ? R.layout.item_artist_crud : R.layout.item_artist;
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ArtistViewHolder(view, isCrud);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = artistList.get(position);
        holder.textViewArtistName.setText(artist.getName());

        // Tải hình ảnh bằng Glide
        Glide.with(context)
                .load(artist.getImageUrl())
                .placeholder(R.drawable.artist) // Hình ảnh placeholder
                .error(R.drawable.artist) // Hình ảnh khi có lỗi
                .into(holder.imageViewArtist);

        // Xử lý sự kiện click cho item artist
        holder.itemView.setOnClickListener(v -> {
            if (onArtistClickListener != null) {
                onArtistClickListener.onArtistClick(artist);
            }
        });

        // Hiển thị hoặc ẩn các nút chỉnh sửa và xóa dựa trên trạng thái isCrud
        if (isCrud) {
            if (holder.editButtonArtist != null && holder.deleteButtonArtist != null) {
                holder.editButtonArtist.setVisibility(View.VISIBLE);
                holder.deleteButtonArtist.setVisibility(View.VISIBLE);

                holder.editButtonArtist.setOnClickListener(v -> {
                    Toast.makeText(context, "Edit artist: " + artist.getName(), Toast.LENGTH_SHORT).show();
                    // Thực hiện hành động chỉnh sửa tại đây
                });

                holder.deleteButtonArtist.setOnClickListener(v -> deleteArtist(artist, holder.getAdapterPosition()));
            }
        } else {
            if (holder.editButtonArtist != null && holder.deleteButtonArtist != null) {
                holder.editButtonArtist.setVisibility(View.GONE);
                holder.deleteButtonArtist.setVisibility(View.GONE);
            }
        }
    }

    private void deleteArtist(Artist artist, int position) {
        try (DatabaseHelper dbHelper = new DatabaseHelper(context);
             SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            boolean isDeleted = SingerTable.deleteSinger(db, artist.getName());
            if (isDeleted) {
                Toast.makeText(context, "Artist deleted successfully!", Toast.LENGTH_SHORT).show();
                artistList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, artistList.size());
            } else {
                Toast.makeText(context, "Failed to delete artist.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        final CircleImageView imageViewArtist;
        final TextView textViewArtistName;
        final ImageView editButtonArtist;
        final ImageView deleteButtonArtist;

        public ArtistViewHolder(@NonNull View itemView, boolean isCrud) {
            super(itemView);
            // Khởi tạo các thành phần item
            imageViewArtist = itemView.findViewById(isCrud ? R.id.artist_image_crud : R.id.artist_image);
            textViewArtistName = itemView.findViewById(isCrud ? R.id.artist_name_crud : R.id.artist_name);

            // Chỉ khởi tạo các nút nếu ở chế độ CRUD
            editButtonArtist = itemView.findViewById(R.id.edit_icon1);
            deleteButtonArtist = itemView.findViewById(R.id.delete_icon1);
        }
    }
}
