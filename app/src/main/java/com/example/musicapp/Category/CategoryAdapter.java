package com.example.musicapp.Category;

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
import com.example.musicapp.Data.GenreTable;
import com.example.musicapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final Context context;
    private final List<Category> categoryList;
    private OnCategoryClickListener onCategoryClickListener;
    private final boolean isCrud;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.onCategoryClickListener = listener;
    }

    public CategoryAdapter(Context context, List<Category> categoryList, boolean isCrud) {
        this.context = context;
        this.categoryList = categoryList;
        this.isCrud = isCrud;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = isCrud ? R.layout.item_category_crud : R.layout.item_category;
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(view, isCrud);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getName());

        // Sử dụng Glide để tải hình ảnh
        Glide.with(context)
                .load(category.getImageResource())
                .placeholder(R.drawable.artist)
                .error(R.drawable.artist)
                .into(holder.categoryImage);

        holder.itemView.setOnClickListener(v -> {
            if (onCategoryClickListener != null) {
                onCategoryClickListener.onCategoryClick(category);
            }
        });

        // Hiển thị hoặc ẩn các nút chỉnh sửa và xóa dựa trên trạng thái isCrud
        if (isCrud) {
            if (holder.editButton != null && holder.deleteButton != null) {
                holder.editButton.setVisibility(View.VISIBLE);
                holder.deleteButton.setVisibility(View.VISIBLE);

                holder.editButton.setOnClickListener(v -> {
                    Toast.makeText(context, "Edit category: " + category.getName(), Toast.LENGTH_SHORT).show();
                    // Thực hiện hành động chỉnh sửa tại đây
                });

                holder.deleteButton.setOnClickListener(v -> deleteCategory(category, holder.getAdapterPosition()));
            }
        } else {
            if (holder.editButton != null && holder.deleteButton != null) {
                holder.editButton.setVisibility(View.GONE);
                holder.deleteButton.setVisibility(View.GONE);
            }
        }
    }

    private void deleteCategory(Category category, int position) {
        try (DatabaseHelper dbHelper = new DatabaseHelper(context);
             SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            boolean isDeleted = GenreTable.deleteGenre(db, category.getName());
            if (isDeleted) {
                Toast.makeText(context, "Category deleted successfully!", Toast.LENGTH_SHORT).show();
                categoryList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, categoryList.size());
            } else {
                Toast.makeText(context, "Failed to delete category.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView categoryName;
        final CircleImageView categoryImage;
        final ImageView editButton;
        final ImageView deleteButton;

        public ViewHolder(@NonNull View itemView, boolean isCrud) {
            super(itemView);
            categoryName = itemView.findViewById(isCrud ? R.id.category_name_crud : R.id.category_name);
            categoryImage = itemView.findViewById(isCrud ? R.id.category_image_crud : R.id.category_image);

            if (isCrud) {
                editButton = itemView.findViewById(R.id.edit_icon);
                deleteButton = itemView.findViewById(R.id.delete_icon);
            } else {
                editButton = null; // Không sử dụng nút chỉnh sửa trong chế độ không phải CRUD
                deleteButton = null; // Không sử dụng nút xóa trong chế độ không phải CRUD
            }
        }
    }
}
