package com.openclassrooms.realestatemanager.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.PropertyAdapter;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.MediaTemp;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyDisplayAllInfo;
import com.openclassrooms.realestatemanager.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.viewmodels.ViewModelFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PropertyListFragment extends Fragment implements PropertyAdapter.OnDispatchListener {

    private PropertyAdapter mPropertyAdapter;
    private List<PropertyDisplayAllInfo> mPropertyList;

    private OnFragmentDispatchListener mDispatchListener;

//    private UserViewModel mUserViewModel;
    private PropertyViewModel mPropertyViewModel;

    private boolean addressLoaded = false;
    private boolean propertyTypeLoaded = false;

    public static PropertyListFragment newInstance() {
        PropertyListFragment fragment = new PropertyListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void configureViewModels() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());

//        this.mUserViewModel = ViewModelProviders
//                .of(this, viewModelFactory)
//                .get(UserViewModel.class);

        this.mPropertyViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PropertyViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        mPropertyList = new ArrayList<>();

        configureViewModels();

        mPropertyAdapter = new PropertyAdapter(getContext(), mPropertyList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mPropertyAdapter);

        loadData();

        return view;
    }

    private void loadData() {
        mPropertyViewModel.getAllPropertyDisplayedInfo()
                .observe(this, this::onPropertyListLoaded);
    }

    public void updateCurrencyShowed() {
        mPropertyAdapter.updateCurrency();
    }

    private void onPropertyListLoaded(List<PropertyDisplayAllInfo> propertyList) {

        for (PropertyDisplayAllInfo padi : propertyList) {
            Property property = padi.getProperty();
            assert property != null;

            mPropertyViewModel.getSelectedMedia(property.getId())
                .observe(this, media -> {
                    if(media != null) {
                        MediaTemp mediaTemp = new MediaTemp();
                        mediaTemp.setId(media.getId());
                        mediaTemp.setCover(media.isCover());
                        mediaTemp.setLabel(media.getLabel());
                        mediaTemp.setFileName(media.getFileName());

                        padi.setMediaTemp(mediaTemp);
                    }
                });

            mPropertyViewModel.getPropertyAddress(property.getId())
                    .observe(this, addressDisplayedInfo ->
                    {
                        if(addressDisplayedInfo != null) {
                            Iterator<Address> it = addressDisplayedInfo.getAddress().iterator();
                            Address address = it.next();
                            padi.setAddress(address);

                            addressLoaded = true;
                            reloadData();
                        }
                    });

            mPropertyViewModel.getPropertyType(property.getPropertyTypeId())
                    .observe(this, propertyType ->
                    {
                        padi.setPropertyType(propertyType);
                        propertyTypeLoaded = true;
                        reloadData();
                    });

        }

        mPropertyList = propertyList;
    }

    public void reloadData() {
        if(propertyTypeLoaded && addressLoaded)
            mPropertyAdapter.setPropertyList(mPropertyList);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentDispatchListener){
            mDispatchListener = (OnFragmentDispatchListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentDispatchListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDispatchListener =  null;
    }

    @Override
    public void onItemClick(Property property) {
        mDispatchListener.onItemClick(property);
    }

    public interface OnFragmentDispatchListener {
        void onItemClick(Property property);
    }

}
