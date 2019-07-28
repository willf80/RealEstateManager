package com.openclassrooms.realestatemanager.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.EditMediaActivity;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.MediaTempAdapter;
import com.openclassrooms.realestatemanager.models.MediaTemp;
import com.openclassrooms.realestatemanager.utils.FileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class MediaBoxView extends LinearLayout implements MediaTempAdapter.MediaClickListener {

    public static final int RESULT_MEDIA_EDIT = 9001;
    public static final int RESULT_ACTION_IMAGE_CAPTURE = 0;
    public static final int RESULT_ACTION_PICK= 1;

    public static final int PHOTO_MAX = 5;

    final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

    Button btnAddMedia;
    Activity mActivity;

    RecyclerView mRecyclerView;
    MediaTempAdapter mMediaTempAdapter;

    List<MediaTemp> mMediaTempList = new ArrayList<>();

    public MediaBoxView(Context context) {
        super(context);

        init();
    }

    public MediaBoxView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public MediaBoxView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.media_box_view, this);

        mRecyclerView = view.findViewById(R.id.mediaRecyclerView);
        btnAddMedia = view.findViewById(R.id.btnAddMedia);

        mMediaTempAdapter = new MediaTempAdapter(mMediaTempList, this);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mRecyclerView.setAdapter(mMediaTempAdapter);

        listeners();
    }

    private void listeners() {
        btnAddMedia.setOnClickListener(v -> {
            if(mMediaTempList.size() >= PHOTO_MAX){
                Toast.makeText(getContext(),
                        String.format(Locale.getDefault(), "Media is limited to %d max", PHOTO_MAX),
                        Toast.LENGTH_LONG)
                    .show();
                return;
            }
            addMediaDialog();
        });
    }

    private void addMediaDialog() {
        if(mActivity == null) {
            throw new NullPointerException("Please use setActivity to attach current activity to this view");
        }

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Choose type of action")
                .setItems(options, (dialog, which) -> {
                    switch (which){
                        case 0:
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            mActivity.startActivityForResult(takePicture, RESULT_ACTION_IMAGE_CAPTURE);
                            break;

                        case 1:
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            mActivity.startActivityForResult(pickPhoto , RESULT_ACTION_PICK);
                            break;

                        default:
                            break;
                    }
                })
                .create();
        alertDialog.show();
    }

    public void addMedia(MediaTemp mediaTemp, int position){

        if(mediaTemp.getId() <= 0 && mediaTemp.getFileName() == null) {
            mediaTemp.setFileName("temp_" + UUID.randomUUID().toString());

            FileHelper.saveToInternalStorage(getContext(), mediaTemp.getPhoto(), mediaTemp.getFileName());
        }

        if(mediaTemp.isCover()) {
            for (MediaTemp temp : mMediaTempList) {
                temp.setCover(false);
            }
        }

        if(position < 0){
            // If it is the first media, set isCover to selected
            if(mMediaTempList.size() <= 0) mediaTemp.setCover(true);
            mMediaTempList.add(mediaTemp);
        }else{

            // If it is the first media, set isCover to selected
            if(mMediaTempList.size() <= 1) mediaTemp.setCover(true);
            mMediaTempList.set(position, mediaTemp);
        }

        mMediaTempAdapter.updateMedia();
    }

    public void addMedia(MediaTemp mediaTemp) {
        addMedia(mediaTemp, -1);
    }

//    public void addMedia(List<MediaTemp> mediaTempList) {
//        for (MediaTemp mediaTemp : mediaTempList) {
//            addMedia(mediaTemp, -1);
//        }
//    }
//
//

    @Override
    public void onDeleteMedia(MediaTemp mediaTemp, int position) {

        if(mediaTemp.getId() <= 0 && mediaTemp.getFileName() != null) {
            FileHelper.deleteFile(getContext(), mediaTemp.getFileName());
        }

        mMediaTempList.remove(mediaTemp);

        if(mediaTemp.isCover() && mMediaTempList.size() > 0) {
            mMediaTempList.get(0).setCover(true);
        }

        mMediaTempAdapter.setMediaTempList(mMediaTempList);
    }

    @Override
    public void onEditMedia(MediaTemp mediaTemp, int position) {
        Intent intent = new Intent(getContext(), EditMediaActivity.class);

//        intent.putExtra(EditMediaActivity.MEDIA_EXTRA_KEY, mediaTemp.fileName);
        intent.putExtra(EditMediaActivity.FILE_NAME_EXTRA_KEY, mediaTemp.getFileName());
        intent.putExtra(EditMediaActivity.LABEL_EXTRA_KEY, mediaTemp.getLabel());
        intent.putExtra(EditMediaActivity.USE_AS_COVER_PHOTO_EXTRA_KEY, mediaTemp.isCover());
        intent.putExtra(EditMediaActivity.EDIT_DATA_POSITION_EXTRA_KEY, position);

        mActivity.startActivityForResult(intent, MediaBoxView.RESULT_MEDIA_EDIT);
    }

    public List<MediaTemp> getMediaTempList() {

        for (MediaTemp mediaTemp : mMediaTempList) {
            if (mediaTemp.getPhoto() != null) continue;

            mediaTemp.setPhoto(FileHelper.loadImageFromStorage(getContext(), mediaTemp.getFileName()));
        }

        return mMediaTempList;
    }

}
