package spb.android.academy.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import spb.android.academy.fragments.domain.Photo;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    @NonNull
    private final Picasso picasso;

    @NonNull
    private final List<Photo> photos;

    public PhotosAdapter(@NonNull Picasso picasso, @NonNull List<Photo> photos) {
        this.picasso = picasso;
        this.photos = photos;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        final Photo photo = photos.get(position);
        picasso.load(photo.getUrlForSmall()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView;
        }
    }
}
