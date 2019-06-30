package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.realestatemanager.adapters.PropertyTypeSpinnerAdapter;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.PropertyType;
import com.openclassrooms.realestatemanager.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.viewmodels.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public abstract class AbstractPropertyEditionActivity extends BaseActivity{

    public enum Mode {
        Creation, Modification
    }

    private Mode mMode;

    // For data
//    private UserViewModel mUserViewModel;
    private PropertyViewModel mPropertyViewModel;

    private PropertyTypeSpinnerAdapter mTypeSpinnerAdapter;
    private ArrayAdapter<String> mPointOfInterestAdapter;

    @BindView(R.id.btnCreateProperty)
    Button mCreatePropertyButton;

    @BindView(R.id.btnEditProperty)
    Button mEditPropertyButton;

    @BindView(R.id.addPointOfInterestTagButton)
    Button mBtnAddPointOfInterestTag;

    @BindView(R.id.propertyTypeSpinner)
    Spinner mPropertyTypeSpinner;

    @BindView(R.id.pointsOfInterestAutoComplete)
    AutoCompleteTextView mPointsOfInterestAutoComplete;

    @BindView(R.id.tagContainerLayout)
    TagContainerLayout mTagContainerLayout;

    protected PropertyType mCurrentPropertyType;
    protected List<String> mPointOfInterestList;

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

        mPointOfInterestList = new ArrayList<>();
        mPointOfInterestAdapter = new ArrayAdapter<>(this,
                android.R.layout.select_dialog_item, mPointOfInterestList);
        mPointsOfInterestAutoComplete.setThreshold(1);
        mPointsOfInterestAutoComplete.setAdapter(mPointOfInterestAdapter);
    }

    private void listeners() {
        mCreatePropertyButton.setOnClickListener(v -> save());
        mEditPropertyButton.setOnClickListener(v -> save());

        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                mTagContainerLayout.removeTag(position);
            }
        });

        mBtnAddPointOfInterestTag.setOnClickListener((v) -> {
            String tag = mPointsOfInterestAutoComplete.getText().toString();

            mTagContainerLayout.addTag(tag);
            mPointsOfInterestAutoComplete.setText("");
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

    private void loadInterestPointAutoCompleteList(List<InterestPoint> interestPointList) {
        mPointOfInterestList = new ArrayList<>();
        for (InterestPoint point :
                interestPointList) {
            mPointOfInterestList.add(point.getLabel());
        }

        mPointOfInterestAdapter.clear();
        mPointOfInterestAdapter.addAll(mPointOfInterestList);
        mPointOfInterestAdapter.notifyDataSetChanged();
    }

}
