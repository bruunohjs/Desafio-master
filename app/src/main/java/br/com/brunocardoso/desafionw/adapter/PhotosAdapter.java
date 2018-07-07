package br.com.brunocardoso.desafionw.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.brunocardoso.desafionw.R;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private List<Uri> photoList;

    public PhotosAdapter() {
    }

    public PhotosAdapter(List<Uri> photoList) {
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public PhotosAdapter.PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_avatar, parent, false);

        PhotosAdapter.PhotoViewHolder vh = new PhotosAdapter.PhotoViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosAdapter.PhotoViewHolder holder, int position) {
        Uri photo = photoList.get( position );

        Glide.with(holder.ivHeroeAvatar.getContext()).load(
                photo
            ).into( holder.ivHeroeAvatar );
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivHeroeAvatar;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            ivHeroeAvatar = itemView.findViewById(R.id.iv_avatar2_id);
        }
    }
}