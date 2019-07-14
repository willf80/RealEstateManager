package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.openclassrooms.realestatemanager.views.MediaBoxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class EditMediaActivity extends BaseActivity {

    public static String MEDIA_EXTRA_KEY = "media_data";
    public static String DESCRIPTION_EXTRA_KEY = "description_data";
    public static String USE_AS_COVER_PHOTO_EXTRA_KEY = "cover_photo";
    public static String EDIT_DATA_POSITION_EXTRA_KEY = "data_position";

    Bitmap mMediaData;

    @BindView(R.id.mediaImageView)
    ImageView mMediaImageView;

    @BindView(R.id.descriptionEditText)
    EditText mDescriptionEditText;

    @BindView(R.id.isCoverPhotoSwitch)
    Switch mCoverPhotoSwitch;

    private byte[] mBytes;
    private boolean mIsUseAsCover;
    private int mDataPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_media);
        ButterKnife.bind(this);

        showReturnHome();

        setTitle("Edit Media");

        getLoadMediaData();
    }

    private void getLoadMediaData() {
        mBytes = getIntent().getByteArrayExtra(MEDIA_EXTRA_KEY);
        mMediaData = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);

        String description = getIntent().getStringExtra(DESCRIPTION_EXTRA_KEY);
        boolean isUseAsCover = getIntent().getBooleanExtra(USE_AS_COVER_PHOTO_EXTRA_KEY, false);
        mDataPosition = getIntent().getIntExtra(EDIT_DATA_POSITION_EXTRA_KEY, -1);

        mMediaImageView.setImageBitmap(mMediaData);
        mDescriptionEditText.setText(description);
        mCoverPhotoSwitch.setChecked(isUseAsCover);
    }

    @OnCheckedChanged(R.id.isCoverPhotoSwitch)
    void onSwith(CompoundButton buttonView, boolean isChecked) {
        mIsUseAsCover = isChecked;
    }

    @OnClick(R.id.btnValidate)
    void onClickValidate(){
        String description = mDescriptionEditText.getText().toString();

        Intent intent = new Intent();
        intent.putExtra(DESCRIPTION_EXTRA_KEY, description);
        intent.putExtra(MEDIA_EXTRA_KEY, mBytes);
        intent.putExtra(USE_AS_COVER_PHOTO_EXTRA_KEY, mIsUseAsCover);

        if(mDataPosition >= 0) {
            intent.putExtra(EDIT_DATA_POSITION_EXTRA_KEY, mDataPosition);
        }

        setResult(MediaBoxView.RESULT_MEDIA_EDIT, intent);
        finish();
    }
}
