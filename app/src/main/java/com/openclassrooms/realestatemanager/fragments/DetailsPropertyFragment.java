package com.openclassrooms.realestatemanager.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.EditPropertyActivity;
import com.openclassrooms.realestatemanager.PropertyDetailsActivity;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.MediaAdapter;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.Media;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyDisplayAllInfo;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.viewmodels.ViewModelFactory;
import com.openclassrooms.realestatemanager.views.PropertyOptionView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;

public class DetailsPropertyFragment extends Fragment {
    public static final String ARG_PROPERTY_ID = "propertyId";

    @BindView(R.id.mediaRecyclerView)
    RecyclerView mMediaRecyclerView;

    @BindView(R.id.editPropertyFab)
    FloatingActionButton mEditPropertyFab;

    @BindView(R.id.buyPropertyFab)
    FloatingActionButton mBuyPropertyFab;

    @BindView(R.id.descriptionTextView)
    TextView descriptionTextView;

    @BindView(R.id.titleTextView)
    TextView titleTextView;

    @BindView(R.id.surfacePropertyOptionView)
    PropertyOptionView surfacePropertyOptionView;

    @BindView(R.id.numberOfRoomsPropertyOptionView)
    PropertyOptionView numberOfRoomsPropertyOptionView;

    @BindView(R.id.numberOfBathroomsPropertyOptionView)
    PropertyOptionView numberOfBathroomsPropertyOptionView;

    @BindView(R.id.numberOfBedroomsPropertyOptionView)
    PropertyOptionView numberOfBedroomsPropertyOptionView;

    @BindView(R.id.locationPropertyOptionView)
    PropertyOptionView locationPropertyOptionView;

    @BindView(R.id.tagContainerLayout)
    TagContainerLayout tagContainerLayout;

    private long mPropertyId;

    private OnDetailsDispatchListener mListener;
    private MediaAdapter mMediaAdapter;
    private List<Media> mMediaList;
    private PropertyViewModel mPropertyViewModel;

    public static DetailsPropertyFragment newInstance(long propertyId) {
        DetailsPropertyFragment fragment = new DetailsPropertyFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PROPERTY_ID, propertyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPropertyId = getArguments().getLong(ARG_PROPERTY_ID);
        }
    }

    private void configureViewModels() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());

        this.mPropertyViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PropertyViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details_property, container, false);
        ButterKnife.bind(this, view);

        mMediaList = new ArrayList<>();

        configureViewModels();

        mMediaAdapter = new MediaAdapter(mMediaList);
        mMediaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mMediaRecyclerView.setAdapter(mMediaAdapter);

        loadData();

        listeners();

        return view;
    }

    private void loadData() {
        mPropertyViewModel.getPropertyDisplayedInfo(mPropertyId)
                .observe(this, this::onPropertyLoaded);
    }

    private void loadMediaList(List<Media> mediaList) {
        mMediaList = mediaList;
        mMediaAdapter.setMediaList(mMediaList);
    }

    private void onPropertyLoaded(PropertyDisplayAllInfo info) {
        Property property = info.getProperty();
        assert property != null;

        descriptionTextView.setText(property.getDescription());
        surfacePropertyOptionView.setDescription(String.format(Locale.getDefault(),
                "%.0f sq m", property.getArea()));

        numberOfRoomsPropertyOptionView.setDescription(String.format(Locale.getDefault(),
                "%d", property.getNumberOfRooms()));

        numberOfBathroomsPropertyOptionView.setDescription(String.format(Locale.getDefault(),
                "%d", property.getNumberOfBathrooms()));

        numberOfBedroomsPropertyOptionView.setDescription(String.format(Locale.getDefault(),
                "%d", property.getNumberOfBedrooms()));

        loadMediaList(info.getMediaList());

        mPropertyViewModel.getPropertyAddress(property.getId())
                .observe(this, addressDisplayedInfo -> {
                    if(addressDisplayedInfo != null) {
                        Iterator<Address> it = addressDisplayedInfo.getAddress().iterator();
                        Address address = it.next();
                        info.setAddress(address);

                        String location = Utils.getPropertyCompleteAddress(property, address);

                        locationPropertyOptionView.setDescription(location);
                    }
                });

        mPropertyViewModel.getPropertyType(property.getPropertyTypeId())
                .observe(this, propertyType -> {

                    String title = Utils.getPropertyTitle(property, propertyType);

                    titleTextView.setText(title);

                    info.setPropertyType(propertyType);
                });

        mPropertyViewModel.getPropertyInterestPointsIds(property.getId())
                .observe(this, this::fetchPropertyInterestPointsIds);
    }

    private void fetchPropertyInterestPointsIds(List<Long> propertyInterestPointIds) {
        mPropertyViewModel.getInterestPoints(propertyInterestPointIds)
                .observe(this, this::fetchInterestPoints);
    }

    private void fetchInterestPoints(List<InterestPoint> interestPointList) {
        List<String> tagList = new ArrayList<>();
        for (InterestPoint interestPoint : interestPointList) {
            tagList.add(interestPoint.getLabel());
        }

        tagContainerLayout.setTags(tagList);
    }

    private void listeners() {
        mEditPropertyFab.setOnClickListener(v -> showEditActivity());
        mBuyPropertyFab.setOnClickListener(v -> mListener.onPropertyMarkAsSell(mPropertyId));
    }

    private void showEditActivity() {
        Intent intent = new Intent(getContext(), EditPropertyActivity.class);
        intent.putExtra(PropertyDetailsActivity.EXTRA_PROPERTY_ID, mPropertyId);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailsDispatchListener) {
            mListener = (OnDetailsDispatchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailsDispatchListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDetailsDispatchListener {
        void onPropertyMarkAsSell(long propertyId);
    }
}
