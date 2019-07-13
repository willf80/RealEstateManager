package com.openclassrooms.realestatemanager.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.MediaTempAdapter;
import com.openclassrooms.realestatemanager.models.MediaTemp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MediaBoxView extends LinearLayout {

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

        mMediaTempAdapter = new MediaTempAdapter(mMediaTempList);
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
                .setTitle("Choose your profile picture")
                .setItems(options, (dialog, which) -> {
                    switch (which){
                        case 0:
                            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            mActivity.startActivityForResult(takePicture, RESULT_ACTION_IMAGE_CAPTURE);
                            break;

                        case 1:
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            mActivity.startActivityForResult(pickPhoto , RESULT_ACTION_PICK);
                            break;

                        default:
                            break;
                    }
                })
                .create();
        alertDialog.show();
    }

    public void addMedia(MediaTemp mediaTemp){
        mMediaTempList.add(mediaTemp);
        mMediaTempAdapter.setMediaTempList(mMediaTempList);
    }
}
