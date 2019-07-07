package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.adapters.PropertyTypeSpinnerAdapter;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.PropertyType;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.viewmodels.ViewModelFactory;
import com.openclassrooms.realestatemanager.views.InterestPointsAddingView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
}
