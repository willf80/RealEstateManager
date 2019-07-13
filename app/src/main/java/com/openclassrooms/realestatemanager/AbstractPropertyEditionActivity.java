package com.openclassrooms.realestatemanager;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.openclassrooms.realestatemanager.adapters.PropertyTypeSpinnerAdapter;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.MediaTemp;
import com.openclassrooms.realestatemanager.models.PropertyType;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.viewmodels.ViewModelFactory;
import com.openclassrooms.realestatemanager.views.InterestPointsAddingView;
import com.openclassrooms.realestatemanager.views.MediaBoxView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class AbstractPropertyEditionActivity extends BaseActivity{

    public enum Mode {
        Creation, Modification
    }

    private Mode mMode;

    // For data
//    private UserViewModel mUserViewModel;
    private PropertyViewModel mPropertyViewModel;

    private PropertyTypeSpinnerAdapter mTypeSpinnerAdapter;

    @BindView(R.id.btnCreateProperty)
    Button mCreatePropertyButton;

    @BindView(R.id.btnEditProperty)
    Button mEditPropertyButton;

    @BindView(R.id.addPointOfInterestTagButton)
    Button mBtnAddPointOfInterestTag;

    @BindView(R.id.propertyTypeSpinner)
    Spinner mPropertyTypeSpinner;

    @BindView(R.id.interestPointsAddingView)
    InterestPointsAddingView mInterestPointsAddingView;

    @BindView(R.id.addressLine1EditText)
    TextInputEditText addressLine1EditText;

    @BindView(R.id.addressLine2EditText)
    TextInputEditText addressLine2EditText;

    @BindView(R.id.postalCodeEditText)
    TextInputEditText postalCodeEditText;

    @BindView(R.id.staticMapImageView)
    ImageView staticMapImageView;

    @BindView(R.id.invalidAddressTextView)
    TextView invalidAddressTextView;

    @BindView(R.id.mediaBoxView)
    MediaBoxView mMediaBoxView;

    protected String addressLine1;
    protected String addressLine2;
    protected String postalCode;

    protected PropertyType mCurrentPropertyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_edition);
        ButterKnife.bind(this);
        showReturnHome();

        mMode = definedMode();
        if(mMode == null) {
            throw new RuntimeException(toString() + " mode cannot be null.");
        }

        setPermissions();

        // 1.Configuration
        configureViews();

        // 2. Configure ViewModels
        configureViewModels();

        // 3. Load all data
        loadAllData();
    }

    public abstract Mode definedMode();

    public abstract void save();

    private void configureViews() {
        if(mMode == Mode.Creation) {
            mCreatePropertyButton.setVisibility(View.VISIBLE);
            mEditPropertyButton.setVisibility(View.GONE);
        } else {
            mCreatePropertyButton.setVisibility(View.GONE);
            mEditPropertyButton.setVisibility(View.VISIBLE);
        }

        // Attach Media view with this activity
        mMediaBoxView.setActivity(this);

        adapterInit();
        listeners();
    }

    private void adapterInit() {
        mTypeSpinnerAdapter = new PropertyTypeSpinnerAdapter(this, new ArrayList<>());
        mPropertyTypeSpinner.setAdapter(mTypeSpinnerAdapter);

        mPropertyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPropertyType = (PropertyType) mTypeSpinnerAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setPermissions(){
//        PermissionListener dialogPermissionListener =
//                DialogOnDeniedPermissionListener.Builder
//                        .withContext(this)
//                        .withTitle("Camera permission")
//                        .withMessage("Camera permission is needed to take pictures")
//                        .withButtonText(android.R.string.ok)
//                        .withIcon(R.mipmap.ic_launcher)
//                        .build();

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                })
                .withErrorListener(error -> {

                })
                .check();

//        Dexter.withActivity(this)
//                .withPermission(Manifest.permission.CAMERA)
//                .withListener(dialogPermissionListener).check();


    }

    private void listeners() {
        mCreatePropertyButton.setOnClickListener(v -> save());
        mEditPropertyButton.setOnClickListener(v -> save());

        addressLine1EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addressLine1 = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                makeFullAddressAndGetMapImage();
            }
        });

        addressLine2EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addressLine2 = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                makeFullAddressAndGetMapImage();
            }
        });

        postalCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                postalCode = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                makeFullAddressAndGetMapImage();
            }
        });
    }

    private void makeFullAddressAndGetMapImage() {
        if(addressLine1 == null || postalCode == null) {
            return;
        }

        if(addressLine1.isEmpty() || postalCode.isEmpty()){
            return;
        }

        String fullAddress = addressLine1 + ", " + postalCode + ", New York";
        try {
            fullAddress = URLEncoder.encode(fullAddress, "utf-8");
            getAddressMapImage(fullAddress);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getAddressMapImage(String fullAddress) {
        // 1. Check if user are connected

        // 2. Call google map static maps API
        // 3. Show Map image
        Picasso.get()
                .load(Utils.buildFullAddressMapImageUrl(this, fullAddress))
                .resize(400, 400)
                .centerCrop()
                .into(staticMapImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        invalidAddressTextView.setVisibility(View.GONE);
                        staticMapImageView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        staticMapImageView.setVisibility(View.GONE);
                        invalidAddressTextView.setVisibility(View.VISIBLE);

                        // Invalid address
                        // TODO : show standard message error for invalid address
                        invalidAddressTextView.setText(e.getLocalizedMessage());
                    }
                });
    }

    private void configureViewModels() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);

//        this.mUserViewModel = ViewModelProviders
//                .of(this, viewModelFactory)
//                .get(UserViewModel.class);

        this.mPropertyViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PropertyViewModel.class);
    }

    private void loadAllData() {
        //Load property type list
        getPropertyTypeList();
        getInterestPointList();
    }

    private void getPropertyTypeList() {
        this.mPropertyViewModel.getPropertyTypes()
                .observe(this, this::loadSpinner);
    }

    private void getInterestPointList() {
        this.mPropertyViewModel.getInterestPoints()
                .observe(this, this::loadInterestPointAutoCompleteList);
    }

    private void loadSpinner(List<PropertyType> propertyTypeList) {
        mTypeSpinnerAdapter.setPropertyTypes(propertyTypeList);
    }

    public void loadInterestPointAutoCompleteList(List<InterestPoint> interestPointList){
        mInterestPointsAddingView.loadInterestPointAutoCompleteList(interestPointList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data == null) return;

        if(resultCode == RESULT_OK) {

            switch (requestCode) {
                case MediaBoxView.RESULT_ACTION_IMAGE_CAPTURE:
                    Bitmap tempSelectedBitmap = (Bitmap) data.getExtras().get("data");
                    showEditMediaActivity(tempSelectedBitmap);
                    break;

                case MediaBoxView.RESULT_ACTION_PICK:
                    showEditMediaActivity(getBitmapFromGallery(data));
                    break;
            }

            return;
        }

        if(resultCode == MediaBoxView.RESULT_MEDIA_EDIT) {
            getMediaEditedData(data);
        }
    }

    private void showEditMediaActivity(Bitmap tempSelectedBitmap) {
        if(tempSelectedBitmap == null ) return;

        //Compress image
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        tempSelectedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

        //show edit media activity
        Intent intent = new Intent(this, EditMediaActivity.class);
        intent.putExtra(EditMediaActivity.MEDIA_EXTRA_KEY, stream.toByteArray());
        startActivityForResult(intent, MediaBoxView.RESULT_MEDIA_EDIT);
    }

    private Bitmap getBitmapFromGallery(Intent data){
        Uri selectedImageUri = data.getData();

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        if(selectedImageUri == null) return null;

        Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn,
                null, null, null);

        if(cursor == null) return null;
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);

        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

        cursor.close();

        return bitmap;
    }

    private void getMediaEditedData(Intent data) {
        byte[] photoBytes = data.getByteArrayExtra(EditMediaActivity.MEDIA_EXTRA_KEY);
        String description = data.getStringExtra(EditMediaActivity.DESCRIPTION_EXTRA_KEY);

        Bitmap photo = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);

        MediaTemp mediaTemp = new MediaTemp();
        mediaTemp.description = description;
        mediaTemp.photo = photo;

        mMediaBoxView.addMedia(mediaTemp);
    }

}
