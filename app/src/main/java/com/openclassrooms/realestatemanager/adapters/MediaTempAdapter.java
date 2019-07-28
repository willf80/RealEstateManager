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
import com.openclassrooms.realestatemanager.models.MediaTemp;
import com.openclassrooms.realestatemanager.utils.FileHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MediaTempAdapter extends RecyclerView.Adapter<MediaTempAdapter.MediaViewHolder> {

    public interface MediaClickListener{
        void onDeleteMedia(MediaTemp mediaTemp, int position);
        void onEditMedia(MediaTemp mediaTemp, int position);
    }

    private List<MediaTemp> mMediaTempList;
    private MediaClickListener mMediaClickListener;

    public MediaTempAdapter(List<MediaTemp> mediaTempList, MediaClickListener mediaClickListener) {
        mMediaTempList = mediaTempList;
        mMediaClickListener = mediaClickListener;
    }

    public void setMediaTempList(List<MediaTemp> mediaTempList) {
        mMediaTempList = mediaTempList;
        updateMedia();
    }

    public void updateMedia() {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_edit_item_view, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        MediaTemp mediaTemp = mMediaTempList.get(position);

        holder.descriptionTextView.setText(mediaTemp.label);

        Picasso.get()
                .load(FileHelper.getFile(holder.context, mediaTemp.fileName))
                .resize(200, 200)
                .into(holder.photoImageView);

        if(mediaTemp.isCover) {
            holder.useAsCoverImageView.setVisibility(View.VISIBLE);
        }else {
            holder.useAsCoverImageView.setVisibility(View.GONE);
        }

        holder.deleteMediaImageView.setOnClickListener(
                v -> mMediaClickListener.onDeleteMedia(mediaTemp, position));

        holder.itemView.setOnClickListener(
                v -> mMediaClickListener.onEditMedia(mediaTemp, position));
    }

    @Override
    public int getItemCount() {
        return mMediaTempList.size();
    }

    class MediaViewHolder extends RecyclerView.ViewHolder {

        ImageView photoImageView;
        TextView descriptionTextView;
        ImageView deleteMediaImageView;
        ImageView useAsCoverImageView;
        Context context;

        MediaViewHolder(@NonNull View itemView) {
            super(itemView);

            photoImageView = itemView.findViewById(R.id.photoImageView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            deleteMediaImageView = itemView.findViewById(R.id.deleteMediaImageView);
            useAsCoverImageView = itemView.findViewById(R.id.useAsCoverImageView);

            context = itemView.getContext();
        }
    }
}
