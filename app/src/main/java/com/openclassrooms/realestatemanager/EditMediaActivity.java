package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.openclassrooms.realestatemanager.views.MediaBoxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditMediaActivity extends BaseActivity {

    public static String MEDIA_EXTRA_KEY = "media_data";
    public static String DESCRIPTION_EXTRA_KEY = "description_data";

    Bitmap mMediaData;

    @BindView(R.id.mediaImageView)
    ImageView mMediaImageView;

    @BindView(R.id.descriptionEditText)
    EditText mDescriptionEditText;

    private byte[] mBytes;

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

        mMediaImageView.setImageBitmap(mMediaData);
    }

    @OnClick(R.id.btnValidate)
    void onClickValidate(){
        String description = mDescriptionEditText.getText().toString();

        Intent intent = new Intent();
        intent.putExtra(DESCRIPTION_EXTRA_KEY, description);
        intent.putExtra(MEDIA_EXTRA_KEY, mBytes);

        setResult(MediaBoxView.RESULT_MEDIA_EDIT, intent);
        finish();
    }
}
