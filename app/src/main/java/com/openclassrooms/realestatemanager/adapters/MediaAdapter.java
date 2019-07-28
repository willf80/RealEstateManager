package com.openclassrooms.realestatemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Media;
import com.openclassrooms.realestatemanager.utils.FileHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    List<Media> mMediaList;

    public MediaAdapter(List<Media> mediaList) {
        mMediaList = mediaList;
    }

    public void setMediaList(List<Media> mediaList) {
        mMediaList = mediaList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_showing_item_view, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        Media media = mMediaList.get(position);

        holder.descriptionTextView.setText(media.getLabel());

        Picasso.get()
                .load(FileHelper.getFile(holder.mContext, media.getFileName()))
                .centerCrop()
                .error(R.drawable.maison1)
                .placeholder(R.drawable.maison1)
                .resize(200, 200)
                .into(holder.photoImageView);
    }

    @Override
    public int getItemCount() {
        return mMediaList.size();
    }

    class MediaViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photoImageView)
        ImageView photoImageView;

        @BindView(R.id.descriptionTextView)
        TextView descriptionTextView;

        Context mContext;

        MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }
    }
}
