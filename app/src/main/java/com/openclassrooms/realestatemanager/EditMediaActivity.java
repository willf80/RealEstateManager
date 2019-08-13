package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.openclassrooms.realestatemanager.utils.FileHelper;
import com.openclassrooms.realestatemanager.views.MediaBoxView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class EditMediaActivity extends BaseActivity {

    public static String MEDIA_ID_EXTRA_KEY = "media_id";
    public static String FILE_NAME_EXTRA_KEY = "file_name_data";
    public static String MEDIA_EXTRA_KEY = "media_data";
    public static String LABEL_EXTRA_KEY = "label";
    public static String USE_AS_COVER_PHOTO_EXTRA_KEY = "cover_photo";
    public static String EDIT_DATA_POSITION_EXTRA_KEY = "data_position";

    @BindView(R.id.mediaImageView)
    ImageView mMediaImageView;

    @BindView(R.id.descriptionEditText)
    EditText mDescriptionEditText;

    @BindView(R.id.isCoverPhotoSwitch)
    Switch mCoverPhotoSwitch;

    private byte[] mBytes;
    private boolean mIsUseAsCover;
    private int mDataPosition;
    private String mFileName;
    private long mediaId;

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
        String description = getIntent().getStringExtra(LABEL_EXTRA_KEY);
        boolean isUseAsCover = getIntent().getBooleanExtra(USE_AS_COVER_PHOTO_EXTRA_KEY, false);

        mBytes = getIntent().getByteArrayExtra(MEDIA_EXTRA_KEY);
        mediaId = getIntent().getLongExtra(MEDIA_ID_EXTRA_KEY, 0);
        mFileName = getIntent().getStringExtra(FILE_NAME_EXTRA_KEY);
        mDataPosition = getIntent().getIntExtra(EDIT_DATA_POSITION_EXTRA_KEY, -1);

        if(mBytes != null) {
            Bitmap mediaDataBitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);
            mMediaImageView.setImageBitmap(mediaDataBitmap);
        }else {
            Picasso.get()
                    .load(FileHelper.getFile(this, mFileName))
                    .resize(400, 400)
                    .into(mMediaImageView);
        }

        mDescriptionEditText.setText(description);
        mCoverPhotoSwitch.setChecked(isUseAsCover);
    }

    @OnCheckedChanged(R.id.isCoverPhotoSwitch)
    void onSwitch(CompoundButton buttonView, boolean isChecked) {
        mIsUseAsCover = isChecked;
    }

    @OnClick(R.id.btnValidate)
    void onClickValidate(){
        String description = mDescriptionEditText.getText().toString();

        Intent intent = new Intent();
        intent.putExtra(LABEL_EXTRA_KEY, description);
        intent.putExtra(MEDIA_ID_EXTRA_KEY, mediaId);
        intent.putExtra(MEDIA_EXTRA_KEY, mBytes);
        intent.putExtra(FILE_NAME_EXTRA_KEY, mFileName);
        intent.putExtra(USE_AS_COVER_PHOTO_EXTRA_KEY, mIsUseAsCover);

        if(mDataPosition >= 0) {
            intent.putExtra(EDIT_DATA_POSITION_EXTRA_KEY, mDataPosition);
        }

        setResult(MediaBoxView.RESULT_MEDIA_EDIT, intent);
        finish();
    }
}
