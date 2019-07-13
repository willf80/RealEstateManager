package com.openclassrooms.realestatemanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.MediaTemp;

import java.util.List;

public class MediaTempAdapter extends RecyclerView.Adapter<MediaTempAdapter.MediaViewHolder> {

    List<MediaTemp> mMediaTempList;

    public MediaTempAdapter(List<MediaTemp> mediaTempList) {
        mMediaTempList = mediaTempList;
    }

    public void setMediaTempList(List<MediaTemp> mediaTempList) {
        mMediaTempList = mediaTempList;
        notifyDataSetChanged();
    }

//    public void addMediaTemp(MediaTemp mediaTemp){
//        mMediaTempList.add(mediaTemp);
//        notifyItemInserted(mMediaTempList.size());
//    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_item_view, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        MediaTemp mediaTemp = mMediaTempList.get(position);

        holder.descriptionTextView.setText(mediaTemp.description);
        holder.photoImageView.setImageBitmap(mediaTemp.photo);
    }

    @Override
    public int getItemCount() {
        return mMediaTempList.size();
    }

    class MediaViewHolder extends RecyclerView.ViewHolder {

        ImageView photoImageView;
        TextView descriptionTextView;

        MediaViewHolder(@NonNull View itemView) {
            super(itemView);

            photoImageView = itemView.findViewById(R.id.photoImageView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
